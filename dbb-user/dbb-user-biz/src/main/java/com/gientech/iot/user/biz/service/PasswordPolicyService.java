package com.gientech.iot.user.biz.service;

import com.gientech.iot.user.api.pojo.dto.PasswordPolicyDTO;
import com.gientech.iot.user.biz.entity.SysPasswordPolicy;

import java.util.List;

/**
 * @description: 密码策略信息接口类
 * @author: 白剑民
 * @dateTime: 2022/10/14 10:37
 */
public interface PasswordPolicyService {

    /**
     * @param passwordPolicyId 密码策略id
     * @description: 根据id获取密码策略
     * @author: 白剑民
     * @date: 2022-10-24 10:17:38
     * @return: com.gientech.iot.user.entity.PasswordPolicy
     * @version: 1.0
     */
    SysPasswordPolicy getPasswordPolicyById(Long passwordPolicyId);

    /**
     * @param enterpriseId 企业/机构id
     * @description: 根据企业/机构id获取密码策略
     * @author: 白剑民
     * @date: 2022-10-24 10:17:38
     * @return: com.gientech.iot.user.entity.PasswordPolicy
     * @version: 1.0
     */
    SysPasswordPolicy getPolicyByEnterpriseId(Long enterpriseId);

    /**
     * @description: 获取所有密码策略列表
     * @author: 白剑民
     * @date: 2022-10-24 15:31:58
     * @return: java.util.List<com.gientech.iot.user.entity.PasswordPolicy>
     * @version: 1.0
     */
    List<SysPasswordPolicy> getPolicyList();

    /**
     * @param dto 修改密码策略信息传参
     * @description: 密码策略修改
     * @author: 白剑民
     * @date: 2022-10-24 15:55:58
     * @version: 1.0
     */
    void update(PasswordPolicyDTO dto);

}
