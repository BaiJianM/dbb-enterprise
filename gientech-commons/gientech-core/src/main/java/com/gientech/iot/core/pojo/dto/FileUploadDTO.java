package com.gientech.iot.core.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 文件上传入参
 * @author: 白剑民
 * @dateTime: 2023-04-19 11:12:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDTO {

    @Schema(description = "文件MD5")
    private String md5;

    @Schema(description = "上传文件的文件名称")
    private String fileName;

    @Schema(description = "上传文件的文件大小")
    private Long fileSize;

    @Schema(description = "总分片数量")
    private Integer chunkCount;

    @Schema(description = "当前为第几块分片")
    private Integer chunkIndex;

    @Schema(description = "按多大的文件粒度进行分片")
    private Integer chunkSize;

    @Schema(description = "分片对象")
    private MultipartFile chunkFile;

    @Schema(description = "上传文件到指定目录")
    private String path = "upload";

}
