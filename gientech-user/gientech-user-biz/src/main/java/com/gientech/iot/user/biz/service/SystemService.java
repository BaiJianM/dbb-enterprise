package com.gientech.iot.user.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gientech.iot.user.api.pojo.dto.SystemCreateDTO;
import com.gientech.iot.user.api.pojo.dto.SystemSearchDTO;
import com.gientech.iot.user.api.pojo.dto.SystemUpdateDTO;
import com.gientech.iot.user.api.pojo.vo.SystemCreateResultVO;
import com.gientech.iot.user.api.pojo.vo.SystemDetailResultVO;
import com.gientech.iot.user.api.pojo.vo.SystemSearchResultVO;
import com.gientech.iot.user.biz.entity.SysSystem;
import com.gientech.iot.web.commons.model.PageResultVO;

import java.util.List;

/**
 * @description: 角色信息接口类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
public interface SystemService extends IService<SysSystem> {

    /**
     * @param dto 创建子系统传参
     * @description: 创建子系统
     * @author: 白剑民
     * @date: 2022-10-31 16:27:00
     * @return: com.gientech.iot.user.entity.vo.RoleCreateResultVO
     * @version: 1.0
     */
    SystemCreateResultVO create(SystemCreateDTO dto);

    /**
     * @param systemId 子系统id
     * @description: 子系统详情
     * @author: 白剑民
     * @date: 2022-10-31 15:16:29
     * @version: 1.0
     */
    SystemDetailResultVO detail(Long systemId);

    /**
     * @param dto 子系统信息更新传参
     * @description: 子系统信息更新
     * @author: 白剑民
     * @date: 2022-10-31 15:16:29
     * @version: 1.0
     */
    void update(SystemUpdateDTO dto);

    /**
     * @param systemIds 子系统id列表
     * @description: 删除子系统信息
     * @author: 白剑民
     * @date: 2022-10-31 15:22:14
     * @version: 1.0
     */
    void delete(List<Long> systemIds);

    /**
     * @param dto 入参
     * @description: 子系统信息列表
     * @author: 白剑民
     * @date: 2023-05-22 17:32:05
     * @return: List<RoleSearchResultVO>
     * @version: 1.0
     */
    List<SystemSearchResultVO> list(SystemSearchDTO dto);

    /**
     * @param dto 入参
     * @description: 子系统信息分页
     * @author: 白剑民
     * @date: 2023-05-22 17:32:05
     * @return: PageResultVO<RoleSearchResultVO>
     * @version: 1.0
     */
    PageResultVO<SystemSearchResultVO> page(SystemSearchDTO dto);

    /**
     * @param systemId 子系统id
     * @param isEnable 启用或禁用
     * @description: 启用或禁用子系统
     * @author: 白剑民
     * @date: 2022-10-31 17:24:28
     * @version: 1.0
     */
    void changeStatus(Long systemId, Boolean isEnable);
}
