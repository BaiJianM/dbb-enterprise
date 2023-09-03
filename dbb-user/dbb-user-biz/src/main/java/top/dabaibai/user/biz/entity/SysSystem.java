package top.dabaibai.user.biz.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.dabaibai.database.entity.BaseEntity;

/**
 * @description: 业务子系统信息注册表
 * @author: 白剑民
 * @dateTime: 2022/10/17 16:20
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SysSystem extends BaseEntity {
    /**
     * 子系统名称
     */
    private String systemName;
    /**
     * 子系统服务名（nacos注册名称）
     */
    private String serviceId;
    /**
     * 是否可用(0: 不可用, 1: 可用)
     */
    private Boolean isEnable;
    /**
     * 备注
     */
    private String remark;
}
