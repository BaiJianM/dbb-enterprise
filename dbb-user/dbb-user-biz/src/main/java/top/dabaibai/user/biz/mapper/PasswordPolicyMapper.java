package top.dabaibai.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.dabaibai.user.biz.entity.SysPasswordPolicy;

/**
 * @description: 企业/机构信息Mapper接口
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:32
 */
@Repository
public interface PasswordPolicyMapper extends BaseMapper<SysPasswordPolicy> {

    /**
     * @param enterpriseId 企业/机构id
     * @description: 通过企业/机构id获取对应的密码策略
     * @author: 白剑民
     * @date: 2023-02-21 11:54:45
     * @return: top.dabaibai.user.entity.SysPasswordPolicy
     * @version: 1.0
     */
    SysPasswordPolicy getPolicyByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}
