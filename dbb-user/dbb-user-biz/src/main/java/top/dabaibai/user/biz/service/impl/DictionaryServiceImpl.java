package top.dabaibai.user.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.dabaibai.core.utils.BeanConvertUtils;
import top.dabaibai.core.utils.TreeUtils;
import top.dabaibai.redis.utils.RedisUtils;
import top.dabaibai.thread.pool.DbbThreadPool;
import top.dabaibai.user.api.pojo.dto.DictCreateDTO;
import top.dabaibai.user.api.pojo.dto.DictSearchDTO;
import top.dabaibai.user.api.pojo.dto.DictUpdateDTO;
import top.dabaibai.user.api.pojo.vo.DictCreateResultVO;
import top.dabaibai.user.api.pojo.vo.DictInfoVO;
import top.dabaibai.user.api.pojo.vo.DictTreeVO;
import top.dabaibai.user.biz.constant.Constants;
import top.dabaibai.user.biz.entity.SysDictionary;
import top.dabaibai.user.biz.mapper.DictionaryMapper;
import top.dabaibai.user.biz.service.DictionaryService;
import top.dabaibai.web.commons.model.PageResultVO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 数据字典实现类
 * @author: 白剑民
 * @dateTime: 2023/5/2 20:38
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, SysDictionary> implements DictionaryService {

    private final DictionaryMapper dictionaryMapper;

    private final RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictCreateResultVO createDict(DictCreateDTO dto) {
        SysDictionary dictionary = BeanConvertUtils.convert(dto, SysDictionary::new, (s, t) -> {
            if (s.getDictParentId() != null && s.getDictParentId() > 0) {
                SysDictionary parentDict = dictionaryMapper.selectById(s.getDictParentId());
                t.setLevel(parentDict.getLevel() + 1);
                t.setParentId(s.getDictParentId());
            }
        }).orElse(new SysDictionary());
        dictionaryMapper.insert(dictionary);
        DictCreateResultVO resultVO = new DictCreateResultVO();
        resultVO.setDictId(dictionary.getId());
        // 异步处理字典缓存
        this.dictCache(false, Constants.RedisConfig.ALL_DICT_CACHE);
        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictById(List<Long> dictIds) {
        dictIds.forEach(dictId -> {
            List<SysDictionary> dictList = dictionaryMapper.getDictEntityListByParentId(dictId, true);
            dictionaryMapper.deleteBatchIds(dictList.stream().map(SysDictionary::getId).collect(Collectors.toList()));
        });
        // 异步处理字典缓存
        this.dictCache(false, Constants.RedisConfig.ALL_DICT_CACHE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(DictUpdateDTO dto) {
        SysDictionary dictionary = dictionaryMapper.selectById(dto.getDictId());
        BeanConvertUtils.convert(dto, () -> dictionary);
        dictionaryMapper.updateById(dictionary);
        // 异步处理字典缓存
        this.dictCache(false, Constants.RedisConfig.ALL_DICT_CACHE);
    }

    @Override
    public SysDictionary getDictById(Long dictId) {
        return dictionaryMapper.selectById(dictId);
    }

    @Override
    public PageResultVO<DictTreeVO> search(DictSearchDTO dto) {
        // 先按条件查出数据，不分页
        List<DictTreeVO> dictPages = dictionaryMapper.getDictList(dto);
        List<DictTreeVO> allDict;
        // 是否存在字典缓存
        boolean hasCache = false;
        // 从缓存取出所有字典数据，如果没有命中缓存则从数据库直接读取
        if (redisUtils.hasKey(Constants.RedisConfig.ALL_DICT_CACHE).orElse(false)) {
            allDict = JSON.parseArray(redisUtils.get(Constants.RedisConfig.ALL_DICT_CACHE).orElse("").toString(), DictTreeVO.class);
            hasCache = true;
        } else {
            allDict = dictionaryMapper.getDictList(new DictSearchDTO());
        }
        // 待匹配字典代码列表
        List<String> codes = new ArrayList<>();
        // 一级字典代码列表
        List<String> topDict = new ArrayList<>();
        // 按规则切分数据库搜索结果中字典代码的等级，每三个数字作一级
        dictPages.forEach(d -> {
            // 按三个数字切分，得到从一级字典开始的字典代码
            String[] dictCodes = d.getDictCode().split("(?<=\\G\\d{3})");
            topDict.add(dictCodes[0]);
            StringBuilder parentDictCode = new StringBuilder();
            for (String dictCode : dictCodes) {
                parentDictCode.append(dictCode);
                // 从一级开始每拼接一次就是一个父级字典，直到自身，然后将其放入字典代码列表
                codes.add(parentDictCode.toString());
            }
        });
        IPage<DictTreeVO> topDictList;
        if (topDict.size() > 0) {
            // 从所有字典中过滤出指定的代码列表数据
            List<DictTreeVO> matchResult = allDict.stream().filter(d -> codes.contains(d.getDictCode()))
                    .collect(Collectors.toList());
            // 转换树结构
            List<DictTreeVO> dicTree = TreeUtils.listToTree(matchResult);
            // 按一级字典分页
            topDictList = dictionaryMapper.getTopDictList(new Page<>(dto.getCurrent(), dto.getSize()), topDict);
            // 由于转换成树结构之后，顶级节点就是一级字典，所以直接替换一级字典分页后的实际数据
            topDictList.setRecords(dicTree);
        } else {
            topDictList = new Page<>(dto.getCurrent(), dto.getSize());
        }
        this.dictCache(hasCache, Constants.RedisConfig.ALL_DICT_CACHE);
        return BeanConvertUtils.convert(topDictList, PageResultVO<DictTreeVO>::new).orElse(new PageResultVO<>());
    }

    /**
     * @param hasCache 是否存在缓存
     * @param cacheKey 缓存key
     * @description: 生成字典缓存
     * @author: 白剑民
     * @date: 2023-05-28 22:20:05
     * @version: 1.0
     */
    private void dictCache(boolean hasCache, String cacheKey) {
        // 异步线程缓存所有字典数据
        DbbThreadPool.init().execute(() -> {
            // 没有缓存的时候才生成
            if (!hasCache) {
                String requestId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                boolean lock = redisUtils.tryLock(Constants.RedisConfig.DICT_LOCK, requestId, 0L, 60_000);
                if (lock) {
                    try {
                        List<DictTreeVO> dictList = dictionaryMapper.getDictList(new DictSearchDTO());
                        if (dictList.size() > 0) {
                            redisUtils.set(cacheKey, JSON.toJSONString(dictList));
                        }
                    } catch (Exception e) {
                        log.error("字典缓存生成出错，错误信息: {}", e.getMessage());
                    } finally {
                        redisUtils.tryUnlock(Constants.RedisConfig.DICT_LOCK, requestId);
                    }
                }
            }
        });
    }

    @Override
    public List<DictInfoVO> listAll() {
        return dictionaryMapper.listAll();
    }

    @Override
    public List<DictInfoVO> listType() {
        return dictionaryMapper.listType();
    }

    @Override
    public List<DictTreeVO> getDictByType(String dictType) {
        List<DictTreeVO> dictList = dictionaryMapper.getDictByType(dictType);
        return TreeUtils.listToTree(dictList, DictTreeVO::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editStatus(Long dictId) {
        List<SysDictionary> dictList = dictionaryMapper.getDictEntityListByParentId(dictId, true);
        dictList.forEach(d -> d.setIsEnable(!d.getIsEnable()));
        this.updateBatchById(dictList);
        // 异步处理字典缓存
        this.dictCache(false, Constants.RedisConfig.ALL_DICT_CACHE);
    }

    @Override
    public Integer getMaxDictCode(Long dictParentId) {
        dictParentId = dictParentId == null ? 0L : dictParentId;
        return dictionaryMapper.getMaxDictCode(dictParentId);
    }
}
