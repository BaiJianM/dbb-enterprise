package com.gientech.iot.core.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @description: 文件上传出参
 * @author: 王强
 * @dateTime: 2023-04-19 11:12:46
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadVO {

    @Schema(description = "上传文件路径")
    private String path;

    @Schema(description = "文件标识")
    private String fileId;

    @Schema(description = "编号")
    private Integer code;

    @Schema(description = "当前时间戳")
    private Long mtime;

    @Schema(description = "是否上传完成")
    private Boolean uploadComplete;

    @Schema(description = "分片md5信息")
    private Map<Integer, String> chunkMd5Info;

    @Schema(description = "缺少的分片")
    private List<Integer> missChunks;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "文件类型")
    private String fileExt;

}
