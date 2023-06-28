package top.dabaibai.web.commons.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 分页查询基础类
 * @author: 白剑民
 * @date : 2022/7/8 14:44
 */
@Data
public class PageDTO {

    @Schema(description = "当前页", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long current;

    @Schema(description = "每页大小", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long size;

    public PageDTO() {
        this.current = 1L;
        this.size = 10L;
    }
}
