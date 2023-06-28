package com.gientech.iot.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @description: 系统监控统计信息
 * @author: 白剑民
 * @dateTime: 2023/5/29 19:05
 */
@Data
@Schema(description = "系统监控统计信息")
public class SystemStatisticsVO {

    @Schema(description = "应用子系统id")
    private Long systemId;

    @Schema(description = "应用子系统名称")
    private String systemName;

    @Schema(description = "登录统计")
    private List<CountInfo> countInfoList;

    @Schema(description = "系统健康")
    private List<HealthyInfo> healthyInfoList;

    @Data
    @Schema(description = "日期与数据映射")
    public static class CountInfo {

        @Schema(description = "日期")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;

        @Schema(description = "统计值")
        private Long count;
    }

    @Data
    public static class HealthyInfo {

        @Schema(description = "节点名称")
        private String node;

        @Schema(description = "是否在线")
        private Boolean isOnline;

    }
}
