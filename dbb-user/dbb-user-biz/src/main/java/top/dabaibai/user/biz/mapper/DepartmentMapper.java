package top.dabaibai.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.dabaibai.user.api.pojo.dto.DepartmentSearchDTO;
import top.dabaibai.user.biz.entity.SysDepartment;

import java.util.List;

/**
 * @description: 部门信息Mapper接口
 * @author: 白剑民
 * @dateTime: 2022/10/21 15:55
 */
@Repository
public interface DepartmentMapper extends BaseMapper<SysDepartment> {

    /**
     * @param dto 入参
     * @description: 列表
     * @author: 白剑民
     * @date: 2023-05-29 10:23:49
     * @return: List<SysDepartment>
     * @version: 1.0
     */
    List<SysDepartment> list(@Param("dto") DepartmentSearchDTO dto);

    /**
     * @param enterpriseId 企业/机构id
     * @description: 根据企业/机构id获取部门列表
     * @author: 白剑民
     * @date: 2022-10-21 16:39:40
     * @return: java.util.List<top.dabaibai.user.entity.SysDepartment>
     * @version: 1.0
     */
    List<SysDepartment> getDeptListByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    /**
     * @param parentIds     父级部门id
     * @param showParent 是否展示父级部门
     * @description: 获取父级部门底下的子部门列表(包含父级部门)
     * @author: 白剑民
     * @date: 2022-10-25 10:56:00
     * @return: java.util.List<top.dabaibai.user.entity.DepartmentTreeVO>
     * @version: 1.0
     */
    List<SysDepartment> getDeptListByParentIds(@Param("parentIds") List<Long> parentIds, @Param("showParent") boolean showParent);

}
