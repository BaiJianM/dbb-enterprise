package top.dabaibai.user.biz.entity;

import top.dabaibai.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 密码策略信息表
 * @author: 白剑民
 * @dateTime: 2022/10/17 16:37
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SysPasswordPolicy extends BaseEntity {
    /**
     * 密码复杂类型（数据字典表枚举，默认：0，简单类型）
     */
    private Integer complexType;
    /**
     * 密码最小长度(默认6位)
     */
    private Integer minLength;
    /**
     * 密码有效期(单位：天，默认：0，永不过期)
     */
    private Integer validityPeriod;
    /**
     * 过期提醒阈值(单位：天，默认：0，不提醒)
     */
    private Integer remindThreshold;
    /**
     * 重试次数(默认3次)
     */
    private Integer retryNum;
    /**
     * 冻结时长(单位: 小时, 默认24小时)
     */
    private Integer freezeTime;
    /**
     * 备注
     */
    private String remark;
}
