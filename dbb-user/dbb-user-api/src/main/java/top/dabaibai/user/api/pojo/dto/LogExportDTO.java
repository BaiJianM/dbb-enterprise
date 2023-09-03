package top.dabaibai.user.api.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.dabaibai.log.core.enums.LogTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 系统操作日志导出传参
 * @author: 白剑民
 * @dateTime: 2022/11/24 15:26
 */
@Data
@Schema(description = "系统操作日志导出传参DTO")
public class LogExportDTO {

    @Schema(description = "日志类型")
    private LogTypeEnum logType;

    @Schema(description = "当前页")
    private Long current;

    @Schema(description = "每页大小")
    private Long size;

    @Schema(description = "勾选的操作记录id列表")
    private List<Long> logIds;

    @Schema(description = "操作人")
    private Long createUserId;

    @Schema(description = "起始操作时间(yyyy-MM-dd HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    @Schema(description = "截止操作时间(yyyy-MM-dd HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

}
