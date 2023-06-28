package com.gientech.iot.user.api.pojo.dto;

import com.gientech.iot.web.commons.model.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 搜索企业/机构信息传参
 * @author: 白剑民
 * @dateTime: 2022/10/21 14:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "企业/机构搜索DTO")
public class EnterpriseSearchDTO extends PageDTO {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "社会统一信用代码")
    private String uniqueCode;

    @Schema(description = "所在地址")
    private String address;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "联系方式")
    private String mobile;

}
