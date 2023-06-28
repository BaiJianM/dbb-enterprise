package com.gientech.iot.user.biz.service;

import com.gientech.iot.user.api.pojo.dto.EnterpriseRegisterDTO;
import com.gientech.iot.user.api.pojo.dto.EnterpriseSearchDTO;
import com.gientech.iot.user.api.pojo.dto.EnterpriseUpdateDTO;
import com.gientech.iot.user.api.pojo.vo.EnterpriseCountVO;
import com.gientech.iot.user.api.pojo.vo.EnterpriseRegisterResultVO;
import com.gientech.iot.user.api.pojo.vo.EnterpriseSearchResultVO;
import com.gientech.iot.user.biz.entity.SysEnterprise;
import com.gientech.iot.web.commons.model.PageResultVO;

/**
 * @description: 企业/机构信息接口类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
public interface EnterpriseService {

    /**
     * @param dto 企业/机构注册传参
     * @description: 企业/机构注册
     * @author: 白剑民
     * @date: 2022-10-18 17:47:15
     * @return: com.gientech.iot.user.entity.vo.EnterpriseRegisterResultVO
     * @version: 1.0
     */
    EnterpriseRegisterResultVO register(EnterpriseRegisterDTO dto);

    /**
     * @param dto 企业/机构信息更新传参
     * @description: 企业/机构信息更新
     * @author: 白剑民
     * @date: 2022-10-24 10:39:44
     * @version: 1.0
     */
    void update(EnterpriseUpdateDTO dto);

    /**
     * @param enterpriseId 企业/机构id
     * @description: 企业/机构信息删除
     * @author: 白剑民
     * @date: 2022-10-24 15:06:50
     * @version: 1.0
     */
    void delete(Long enterpriseId);

    /**
     * @param enterpriseId 企业/机构id
     * @description: 根据企业/机构id获取企业/机构信息
     * @author: 白剑民
     * @date: 2022-10-21 13:45:23
     * @return: com.gientech.iot.user.entity.Enterprise
     * @version: 1.0
     */
    SysEnterprise getEnterpriseInfoById(Long enterpriseId);

    /**
     * @param uniqueCode 企业/机构社会统一信用代码
     * @description: 根据企业/机构社会统一信用代码获取企业/机构信息
     * @author: 白剑民
     * @date: 2022-10-21 13:45:23
     * @return: com.gientech.iot.user.entity.Enterprise
     * @version: 1.0
     */
    SysEnterprise getEnterpriseInfoByUniqueCode(String uniqueCode);

    /**
     * @param dto 搜索企业/机构信息列表传参
     * @description: 模糊搜索企业/机构信息列表
     * @author: 白剑民
     * @date: 2022-10-21 14:30:58
     * @return: com.gientech.iot.web.commons.model.PageResultVO<
            * * com.gientech.iot.user.api.pojo.vo.EnterpriseSearchResultVO>
     * @version: 1.0
     */
    PageResultVO<EnterpriseSearchResultVO> search(EnterpriseSearchDTO dto);

    /**
     * @description: 统计所有企业/机构数量
     * @author: 白剑民
     * @date: 2022-10-26 17:16:53
     * @return: java.lang.Integer
     * @version: 1.0
     */
    long count();

    /**
     * @param enterpriseId 企业/机构id
     * @description: 统计企业/机构下的所有部门数和用户数
     * @author: 白剑民
     * @date: 2022-10-26 17:22:26
     * @return: com.gientech.iot.user.entity.vo.EnterpriseCountVO
     * @version: 1.0
     */
    EnterpriseCountVO countDepartmentAndUser(Long enterpriseId);

}
