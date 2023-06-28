package top.dabaibai.user.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.dabaibai.core.utils.BeanConvertUtils;
import top.dabaibai.core.utils.TreeUtils;
import top.dabaibai.redis.utils.RedisUtils;
import top.dabaibai.user.api.pojo.dto.DepartmentCreateDTO;
import top.dabaibai.user.api.pojo.dto.DepartmentSearchDTO;
import top.dabaibai.user.api.pojo.dto.DepartmentUpdateDTO;
import top.dabaibai.user.api.pojo.vo.DepartmentCreateResultVO;
import top.dabaibai.user.api.pojo.vo.DepartmentDetailResultVO;
import top.dabaibai.user.api.pojo.vo.DepartmentSearchResultVO;
import top.dabaibai.user.api.pojo.vo.DepartmentTreeVO;
import top.dabaibai.user.biz.constant.Constants;
import top.dabaibai.user.biz.entity.SysDepartment;
import top.dabaibai.user.biz.entity.SysDepartmentUser;
import top.dabaibai.user.biz.enums.CustomErrorCodeEnum;
import top.dabaibai.user.biz.mapper.DepartmentMapper;
import top.dabaibai.user.biz.service.DepartmentService;
import top.dabaibai.user.biz.service.DepartmentUserService;
import top.dabaibai.web.commons.http.DbbException;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: 部门信息实现类
 * @author: 白剑民
 * @dateTime: 2022/10/21 16:16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, SysDepartment> implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    private final DepartmentUserService departmentUserService;

    private final RedisUtils redisUtils;


    @Override
    public String getNo() {
        // 部门编码生成前缀
        String prefix = Constants.CodeRule.DEPT_CODE_PREFIX;
        // 部门可用编码对应的redis中的key
        String codeKey = String.format(Constants.RedisConfig.BASE_FORMAT, prefix);
        // 部门编码锁对应的redis中的key
        String lockKey = String.format(Constants.RedisConfig.LOCK_PREFIX, prefix);
        String val = redisUtils.get(codeKey).orElse("").toString();
        // 如果没有，就生成缓存
        if (val.length() == 0) {
            // 加锁
            boolean lock = redisUtils.tryReentrantLock(lockKey, "", 200, 5000);
            try {
                if (lock) {
                    // 双重判断
                    val = redisUtils.get(codeKey).orElse("").toString();
                    if (val.length() == 0) {
                        // 部门编码，倒序取最大的编码
                        LambdaQueryWrapper<SysDepartment> group = new LambdaQueryWrapper<SysDepartment>()
                                .eq(SysDepartment::getIsDelete, false)
                                .orderByDesc(SysDepartment::getDepartmentCode);
                        List<SysDepartment> childList = departmentMapper.selectList(group);
                        // 如果存在部门编码
                        if (childList.size() > 0) {
                            // 取最大编码
                            SysDepartment firstChild = departmentMapper.selectList(group).get(0);
                            String newCode = firstChild.getDepartmentCode();
                            // 截取部门编码中表示数值的字符
                            String code = newCode.substring(prefix.length());
                            if (StringUtils.isNumeric(code)) {
                                // 部门编码数值递增1，并按3位字符补0
                                val = prefix + String.format("%03d", Integer.parseInt(code) + 1);
                            }
                        } else {
                            // 不存在就直接拼接001作为第一位部门编码
                            val = prefix + "001";
                        }
                        // 生成的部门编码存入redis
                        redisUtils.set(codeKey, val);
                    }
                }
            } catch (Exception e) {
                log.error("部门编码缓存生成出错,错误信息: {}", e.getMessage());
                throw new DbbException(CustomErrorCodeEnum.CODE_CACHE_ERROR);
            } finally {
                // 如果加锁成功则进行解锁
                if (lock) {
                    redisUtils.tryReentrantUnlock(lockKey, "");
                }
            }
        }
        return val;
    }

    @Override
    public DepartmentCreateResultVO create(DepartmentCreateDTO dto) {
        SysDepartment parent = dto.getParentId() == 0 ? new SysDepartment() : departmentMapper.selectById(dto.getParentId());
        if (parent == null) {
            throw new DbbException(CustomErrorCodeEnum.PARENT_NOT_EXIT);
        }
        // 部门编码生成前缀
        String prefix = Constants.CodeRule.DEPT_CODE_PREFIX;
        // 部门可用编码对应的redis中的key
        String codeKey = String.format(Constants.RedisConfig.BASE_FORMAT, prefix);
        // 部门编码锁对应的redis中的key
        String lockKey = String.format(Constants.RedisConfig.LOCK_PREFIX, prefix);
        boolean lock = redisUtils.tryReentrantLock(lockKey, "", 400, 5000);
        try {
            if (lock) {
                AtomicInteger index = new AtomicInteger(Integer.parseInt(getNo().substring(prefix.length())));
                // 将传参字段转换赋值成部门实体属性
                SysDepartment sysDepartment = BeanConvertUtils.convert(dto, SysDepartment::new, (s, t) -> {
                    t.setId(IdWorker.getId());
                    t.setDepartmentCode(prefix + String.format("%03d", index.getAndIncrement()));
                    t.setParentId(s.getParentId());
                    t.setIds(parent.getIds() == null ? String.valueOf(t.getId()) : parent.getIds() + "," + t.getId());
                    t.setLevel(t.getIds().split(",").length);
                }).orElseThrow(() -> new DbbException(CustomErrorCodeEnum.DEPARTMENT_CREATE_ERROR));
                departmentMapper.insert(sysDepartment);
                // 入库成功后，将缓存值递增并刷新缓存
                redisUtils.set(codeKey, prefix + String.format("%03d", index.get()));
                // 封装返回内容
                DepartmentCreateResultVO resultVO = new DepartmentCreateResultVO();
                resultVO.setDepartmentId(sysDepartment.getId());
                return resultVO;
            } else {
                throw new RedisException("部门锁获取失败");
            }
        } catch (Exception e) {
            log.error("部门创建失败，错误信息: {}", e.getMessage());
            throw new DbbException(CustomErrorCodeEnum.DEPARTMENT_CREATE_ERROR);
        } finally {
            // 如果加锁成功则进行解锁
            if (lock) {
                redisUtils.tryReentrantUnlock(lockKey, "");
            }
        }
    }

    @Override
    public DepartmentDetailResultVO detail(Long departmentId) {
        SysDepartment department = departmentMapper.selectById(departmentId);
        return BeanConvertUtils.convert(department, DepartmentDetailResultVO::new, (s, t) -> {
            t.setDepartmentId(s.getId());
        }).orElse(new DepartmentDetailResultVO());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DepartmentUpdateDTO dto) {
        // 根据部门id获取部门实体信息
        SysDepartment department = departmentMapper.selectById(dto.getDepartmentId());
        // 判断是否为迁移部门操作（变更父级部门id）
        if (StringUtils.isEmpty(department.getIds()) || !Objects.equals(department.getParentId(), dto.getParentId())) {
            SysDepartment parent = dto.getParentId() == 0 ? new SysDepartment() : departmentMapper.selectById(dto.getParentId());
            if (parent == null) {
                throw new DbbException(CustomErrorCodeEnum.PARENT_NOT_EXIT);
            }
            // 更新部门信息
            SysDepartment dept = BeanConvertUtils.convert(dto, SysDepartment::new, (s, t) -> {
                t.setId(s.getDepartmentId());
                t.setParentId(dto.getParentId());
                t.setIds(parent.getIds() == null ? String.valueOf(t.getId()) : parent.getIds() + "," + t.getId());
                t.setLevel(t.getIds().split(",").length);
            }).orElseThrow(() -> new DbbException(CustomErrorCodeEnum.DEPARTMENT_UPDATE_ERROR));
            // 获取部门下的子部门列表
            List<SysDepartment> childDeptList =
                    departmentMapper.getDeptListByParentIds(Collections.singletonList(dto.getDepartmentId()), false);
            // 更新子部门ids
            for (SysDepartment child : childDeptList) {
                List<String> split = Arrays.asList(child.getIds() == null ? new String[]{""} : child.getIds().split(","));
                List<String> strings = split.subList(split.indexOf(String.valueOf(dept.getId())), split.size());
                strings.set(0, dept.getIds());
                child.setIds(strings.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining(",")));
                child.setLevel(child.getIds().split(",").length);
            }
            childDeptList.add(dept);
            // 批量更新部门信息
            updateBatchById(childDeptList);
        } else {
            // 将传参字段转换赋值成部门实体属性
            SysDepartment dept = BeanConvertUtils.convert(dto, SysDepartment::new, (s, t) -> {
                t.setId(s.getDepartmentId());
            }).orElseThrow(() -> new DbbException(CustomErrorCodeEnum.DEPARTMENT_UPDATE_ERROR));
            // 更新单个部门信息
            departmentMapper.updateById(dept);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> departmentIds) {
        // 获取目标逻辑删除的部门
        List<SysDepartment> deptList = departmentMapper.getDeptListByParentIds(departmentIds, true);
        List<Long> deptIdList = deptList.stream().map(SysDepartment::getId).collect(Collectors.toList());
        // 判断部门是否存在关联用户
        if (CollectionUtils.isNotEmpty(departmentUserService.getUserIdsByDeptIds(deptIdList))) {
            throw new DbbException(CustomErrorCodeEnum.DEPARTMENT_HAS_USER);
        }
        departmentMapper.deleteBatchIds(deptIdList);
    }

    @Override
    public List<DepartmentSearchResultVO> list(DepartmentSearchDTO dto) {
        List<SysDepartment> list = departmentMapper.list(dto);
        return (List<DepartmentSearchResultVO>) BeanConvertUtils.convertCollection(list, DepartmentSearchResultVO::new, (s, t) -> {
            t.setDepartmentId(s.getId());
        }).orElse(new ArrayList<>());
    }

    @Override
    public List<DepartmentSearchResultVO> listByUserIds(List<Long> userIds) {
        List<Long> deptIds = departmentUserService.getDeptIdsByUserIds(userIds);
        if (CollectionUtils.isNotEmpty(deptIds)) {
            List<SysDepartment> list = departmentMapper.selectBatchIds(deptIds);
            return (List<DepartmentSearchResultVO>) BeanConvertUtils.convertCollection(list, DepartmentSearchResultVO::new, (s, t) -> {
                t.setDepartmentId(s.getId());
            }).orElse(new ArrayList<>());
        }
        return new ArrayList<>();
    }

    @Override
    public Map<Long, DepartmentSearchResultVO> mapByUserIds(List<Long> userIds) {
        List<SysDepartmentUser> deptUserList = departmentUserService.getListByUserIds(userIds);
        Map<Long, Long> deptUserMap = deptUserList.stream().collect(Collectors.toMap(SysDepartmentUser::getDepartmentId, SysDepartmentUser::getUserId, (k1, k2) -> k1));
        List<DepartmentSearchResultVO> deptList = listByUserIds(userIds);
        return deptList.stream().collect(Collectors.toMap(p -> deptUserMap.get(p.getDepartmentId()),
                Function.identity(), (k1, k2) -> k1));
    }

    @Override
    public List<DepartmentTreeVO> tree(DepartmentSearchDTO dto) {
        List<SysDepartment> list = departmentMapper.list(dto);
        // 返回树状列表
        return TreeUtils.listToTree(list, DepartmentTreeVO::new, (s, t) -> t.setDepartmentId(s.getId()));
    }

    @Override
    public List<DepartmentSearchResultVO> getListByEnterpriseId(Long enterpriseId) {
        List<SysDepartment> list = departmentMapper.getDeptListByEnterpriseId(enterpriseId);
        return (List<DepartmentSearchResultVO>) BeanConvertUtils.convertCollection(list, DepartmentSearchResultVO::new, (s, t) -> {
            t.setDepartmentId(s.getId());
        }).orElse(new ArrayList<>());
    }

    @Override
    public List<DepartmentTreeVO> getTreeByEnterpriseId(Long enterpriseId) {
        // 根据企业/机构id获取部门列表
        List<SysDepartment> deptList = departmentMapper.getDeptListByEnterpriseId(enterpriseId);
        // 返回树状列表
        return TreeUtils.listToTree(deptList, DepartmentTreeVO::new, (s, t) -> {
            t.setDepartmentId(s.getId());
        });
    }

    @Override
    public List<DepartmentTreeVO> getTreeByDepartmentId(Long departmentId) {
        // 获取指定父级部门的节点下的数据(含父级部门)
        List<SysDepartment> deptList =
                departmentMapper.getDeptListByParentIds(Collections.singletonList(departmentId), true);
        // 返回树状列表
        return TreeUtils.listToTree(deptList, DepartmentTreeVO::new, (s, t) -> {
            t.setDepartmentId(s.getId());
        });
    }

    /**
     * @description: 初始化数据
     * @author: 白剑民
     * @date: 2023-05-04 13:00:56
     * @return: void
     * @version: 1.0
     */
    @PostConstruct
    public void initData() {
        log.info("=====================初始化部门数据=====================");
        List<SysDepartment> allDeptList = departmentMapper.selectList(new LambdaQueryWrapper<SysDepartment>()
                .select(SysDepartment::getId, SysDepartment::getIds, SysDepartment::getParentId));
        Map<Long, String> deptIdsMap = new HashMap<>(allDeptList.size());
        for (SysDepartment dept : allDeptList) {
            String parentIds = deptIdsMap.get(dept.getParentId());
            dept.setIds(parentIds == null ? String.valueOf(dept.getId()) : parentIds + "," + dept.getId());
            dept.setLevel(dept.getIds().split(",").length);
            deptIdsMap.put(dept.getId(), dept.getIds());
        }
        updateBatchById(allDeptList);
    }
}
