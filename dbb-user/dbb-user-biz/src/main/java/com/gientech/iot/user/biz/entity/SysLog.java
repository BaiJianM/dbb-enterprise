package com.gientech.iot.user.biz.entity;

import com.gientech.iot.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @description: 系统日志表
 * @author: 白剑民
 * @dateTime: 2022/11/17 16:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysLog extends BaseEntity {

    private static final long serialVersionUID = 2599924084478361697L;

    /**
     * 功能模块（数据字典表枚举值）
     */
    private String module;

    /**
     * 操作事件（数据字典表枚举值）
     */
    private String event;

    /**
     * 日志描述
     */
    private String msg;

    /**
     * 额外信息（备用）
     */
    private String extra;

    /**
     * 日志类型（1:系统日志，2:操作日志）
     */
    private Integer type;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 应用子系统ID
     */
    private Long systemId;

    /**
     * 是否成功 默认1成功
     */
    private Boolean isSuccess;
}
