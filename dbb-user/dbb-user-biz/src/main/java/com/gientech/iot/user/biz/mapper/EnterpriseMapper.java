package com.gientech.iot.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gientech.iot.user.api.pojo.dto.EnterpriseSearchDTO;
import com.gientech.iot.user.api.pojo.vo.EnterpriseSearchResultVO;
import com.gientech.iot.user.biz.entity.SysEnterprise;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description: 企业/机构信息Mapper接口
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:32
 */
@Repository
public interface EnterpriseMapper extends BaseMapper<SysEnterprise> {

    /**
     * @param dto 搜索企业/机构信息列表传参
     * @description: 模糊搜索企业/机构信息列表
     * @author: 白剑民
     * @date: 2022-10-21 14:37:01
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<
            * * com.gientech.iot.user.api.pojo.vo.EnterpriseSearchResultVO>
     * @version: 1.0
     */
    IPage<EnterpriseSearchResultVO> searchByParam(Page<EnterpriseSearchResultVO> page,
                                                  @Param("dto") EnterpriseSearchDTO dto);
}
