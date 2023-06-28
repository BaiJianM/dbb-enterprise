package com.gientech.iot.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 系统操作日志搜索回参
 * @author: 白剑民
 * @dateTime: 2022/11/21 09:35
 */
@Data
@Schema(description = "系统操作日志搜索回参VO")
public class LogSearchResultVO {

    @Schema(description = "操作日志id")
    private Long logId;

    @Schema(description = "序号，导出用，前端忽略")
    private Integer serialNo;

    @Schema(description = "操作人编号")
    private String createUserCode;

    @Schema(description = "操作人姓名")
    private String createUserName;

    @Schema(description = "操作时间(yyyy-MM-dd HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "操作事件")
    private String event;

    @Schema(description = "操作明细")
    private String msg;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "子系统名称")
    private String systemName;

}
