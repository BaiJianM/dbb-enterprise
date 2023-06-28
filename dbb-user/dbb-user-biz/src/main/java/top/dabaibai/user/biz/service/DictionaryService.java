package top.dabaibai.user.biz.service;

import top.dabaibai.user.api.pojo.dto.DictCreateDTO;
import top.dabaibai.user.api.pojo.dto.DictSearchDTO;
import top.dabaibai.user.api.pojo.dto.DictUpdateDTO;
import top.dabaibai.user.api.pojo.vo.DictCreateResultVO;
import top.dabaibai.user.api.pojo.vo.DictInfoVO;
import top.dabaibai.user.api.pojo.vo.DictTreeVO;
import top.dabaibai.user.biz.entity.SysDictionary;
import top.dabaibai.web.commons.model.PageResultVO;

import java.util.List;

/**
 * @description: 数据字典接口类
 * @author: 白剑民
 * @dateTime: 2023/5/2 20:38
 */
public interface DictionaryService {

    /**
     * @param dto 创建数据字典传参
     * @description: 创建数据字典
     * @author: 白剑民
     * @date: 2023-05-20 21:08:53
     * @return: top.dabaibai.user.api.pojo.vo.DictCreateResultVO
     * @version: 1.0
     */
    DictCreateResultVO createDict(DictCreateDTO dto);

    /**
     * @param dictIds 字典主键id
     * @description: 根据主键id删除字典
     * @author: 白剑民
     * @date: 2023-05-22 09:31:46
     * @version: 1.0
     */
    void deleteDictById(List<Long> dictIds);

    /**
     * @param dto 数据字典更新传参
     * @description: 更新数据字典
     * @author: 白剑民
     * @date: 2023-05-22 11:04:12
     * @version: 1.0
     */
    void updateDict(DictUpdateDTO dto);

    /**
     * @param dictId 字典主键id
     * @description: 根据主键id获取字典信息
     * @author: 白剑民
     * @date: 2023-05-20 21:19:00
     * @return: top.dabaibai.user.biz.entity.SysDictionary
     * @version: 1.0
     */
    SysDictionary getDictById(Long dictId);

    /**
     * @param dto 搜索传参
     * @description: 数据字典搜索
     * @author: 白剑民
     * @date: 2023-05-22 13:45:52
     * @return: top.dabaibai.web.commons.model.PageResultVO<top.dabaibai.user.api.pojo.vo.DictTreeVO>
     * @version: 1.0
     */
    PageResultVO<DictTreeVO> search(DictSearchDTO dto);

    /**
     * @description: 获取所有数据字典
     * @author: 白剑民
     * @date: 2023-05-22 15:54:34
     * @return: java.util.List<top.dabaibai.user.api.pojo.vo.DictInfoVO>
     * @version: 1.0
     */
    List<DictInfoVO> listAll();

    /**
     * @param dictType 数据字典类型
     * @description: 根据数据字典类型获取字典列表
     * @author: 白剑民
     * @date: 2023-05-22 14:58:20
     * @return: java.util.List<top.dabaibai.user.api.pojo.vo.DictTreeVO>
     * @version: 1.0
     */
    List<DictTreeVO> getDictByType(String dictType);

    /**
     * @description: 获取所有字典类型
     * @author: 白剑民
     * @date: 2023-05-25 10:29:56
     * @return: java.util.List<top.dabaibai.user.api.pojo.vo.DictInfoVO>
     * @version: 1.0
     */
    List<DictInfoVO> listType();

    /**
     * @param dictId 数据字典主键id
     * @description: 启用禁用数据字典项
     * @author: 白剑民
     * @date: 2023-05-25 22:56:04
     * @version: 1.0
     */
    void editStatus(Long dictId);

    /**
     * @param dictParentId 父级字典id
     * @description: 获取当前字典编码的最大编码值
     * @author: 白剑民
     * @date: 2023-05-26 09:42:21
     * @return: java.lang.Integer
     * @version: 1.0
     */
    Integer getMaxDictCode(Long dictParentId);

}
