package top.dabaibai.user.api.pojo.dto;

import top.dabaibai.log.annotations.LogDiff;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 用户注册传参
 * @author: 白剑民
 * @dateTime: 2022/10/28 16:20
 */
@Data
@Schema(description = "用户注册DTO")
public class UserRegisterDTO {

    @Schema(description = "用户账号(若不传此字段，则默认使用联系方式作为账号)")
    @NotBlank(message = "用户账号，username不能为null且字符串长度必须大于0")
    @LogDiff(alias = "用户账号")
    private String username;

    @Schema(description = "用户密码")
    @NotBlank(message = "用户密码，password不能为null且字符串长度必须大于0")
    private String password;

    @Schema(description = "用户姓名")
    @NotBlank(message = "用户姓名，realName不能为null且字符串长度必须大于0")
    @LogDiff(alias = "用户姓名")
    private String realName;

    @Schema(description = "性别")
    @LogDiff(alias = "性别")
    private Integer gender;

    @Schema(description = "编号/工号")
    @LogDiff(alias = "编号")
    private String code;

    @Schema(description = "邮箱")
    @LogDiff(alias = "邮箱")
    private String email;

    @Schema(description = "手机号")
    @NotBlank(message = "手机号，phone不能为null且字符串长度必须大于0")
    @LogDiff(alias = "手机号")
    private String phone;

    @Schema(description = "所属部门id")
    @NotNull(message = "部门id，departmentId不能为null")
    @Min(value = 1, message = "部门id，departmentId数值必须大于0")
    private Long departmentId;

    @Schema(description = "身份证号")
    private String idCardNo;

    @Schema(description = "备注")
    private String remark;

}
