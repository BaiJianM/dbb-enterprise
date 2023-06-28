package com.gientech.iot.user.api.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gientech.iot.web.commons.model.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @description: 用户信息检索传参
 * @author: 白剑民
 * @dateTime: 2022/10/26 14:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "在线用户检索DTO")
public class OnlineSearchDTO extends PageDTO {

    @Schema(description = "事件", hidden = true)
    private String event;

    @Schema(description = "子系统ID")
    private Long systemId;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "子系统ID")
    private String ipAddress;

    @Schema(description = "起始操作时间", example = "2023-01-01 12:00:00", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startCreateTime;
}
