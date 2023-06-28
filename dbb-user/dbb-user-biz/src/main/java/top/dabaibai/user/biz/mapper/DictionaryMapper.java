package top.dabaibai.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.dabaibai.user.api.pojo.dto.DictSearchDTO;
import top.dabaibai.user.api.pojo.vo.DictInfoVO;
import top.dabaibai.user.api.pojo.vo.DictTreeVO;
import top.dabaibai.user.biz.entity.SysDictionary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: 字典表Mapper接口
 * @author: 白剑民
 * @dateTime: 2022/11/18 15:05
 */
@Repository
public interface DictionaryMapper extends BaseMapper<SysDictionary> {

    /**
     * @param dictParentId 父级字典id
     * @param isShowParent 是否展示父级字典
     * @description: 根据父级字典id获取其下所有数据字典树
     * @author: 白剑民
     * @date: 2023-04-06 16:24:26
     * @return: java.util.List<top.dabaibai.user.api.pojo.vo.DictTreeVO>
     * @version: 1.0
     */
    List<DictTreeVO> getDictListByParentId(@Param("dictParentId") Long dictParentId,
                                           @Param("isShowParent") Boolean isShowParent);

    /**
     * @param dictParentId 父级字典id
     * @param isShowParent 是否展示父级字典
     * @description: 根据父级字典id获取其下所有数据字典列表
     * @author: 白剑民
     * @date: 2023-05-29 17:05:13
     * @return: java.util.List<top.dabaibai.user.biz.entity.SysDictionary>
     * @version: 1.0
     */
    List<SysDictionary> getDictEntityListByParentId(@Param("dictParentId") Long dictParentId,
                                                    @Param("isShowParent") Boolean isShowParent);

    /**
     * @description: 获取所有字典
     * @author: 白剑民
     * @date: 2023-05-25 11:10:05
     * @return: java.util.List<top.dabaibai.user.api.pojo.vo.DictInfoVO>
     * @version: 1.0
     */
    List<DictInfoVO> listAll();

    /**
     * @param dto 字典搜索传参
     * @description: 根据搜索条件获取数据字典列表
     * @author: 白剑民
     * @date: 2023-05-22 13:54:00
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<top.dabaibai.user.api.pojo.vo.DictTreeVO>
     * @version: 1.0
     */
    List<DictTreeVO> getDictList(@Param("dto") DictSearchDTO dto);

    /**
     * @param page    分页
     * @param topDict 一级字典编码列表
     * @description: 获取指定一级字典列表
     * @author: 白剑民
     * @date: 2023-05-29 16:58:33
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<top.dabaibai.user.api.pojo.vo.DictTreeVO>
     * @version: 1.0
     */
    IPage<DictTreeVO> getTopDictList(Page<DictTreeVO> page, @Param("topDict") List<String> topDict);

    /**
     * @param dictType 数据字典类型
     * @description: 根据数据字典类型获取字典列表
     * @author: 白剑民
     * @date: 2023-05-24 14:06:29
     * @return: top.dabaibai.user.api.pojo.vo.DictTreeVO
     * @version: 1.0
     */
    List<DictTreeVO> getDictByType(@Param("dictType") String dictType);

    /**
     * @description: 获取所有字典类型
     * @author: 白剑民
     * @date: 2023-05-25 10:31:30
     * @return: java.util.List<top.dabaibai.user.api.pojo.vo.DictInfoVO>
     * @version: 1.0
     */
    List<DictInfoVO> listType();

    /**
     * @param dictParentId 父级字典id
     * @description: 获取当前字典编码的最大编码值
     * @author: 白剑民
     * @date: 2023-05-26 09:43:16
     * @return: java.lang.Integer
     * @version: 1.0
     */
    Integer getMaxDictCode(@Param("dictParentId") Long dictParentId);

    /**
     * @param dictId 数据字典主键id
     * @description: 根据字典主键id获取其父级路径
     * @author: 白剑民
     * @date: 2023-05-26 09:45:31
     * @return: java.util.List<top.dabaibai.user.biz.entity.SysDictionary>
     * @version: 1.0
     */
    List<SysDictionary> getPathById(@Param("dictId") Long dictId);
}
