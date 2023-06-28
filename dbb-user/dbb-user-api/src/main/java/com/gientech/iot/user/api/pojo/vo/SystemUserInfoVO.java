package com.gientech.iot.user.api.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gientech.iot.core.pojo.vo.BaseUserInfoVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 系统用户信息VO
 * @author: 白剑民
 * @dateTime: 2023/04/27 10:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "系统用户信息VO")
public class SystemUserInfoVO extends BaseUserInfoVO {

    @Schema(description = "用户头像地址")
    private String avatar;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "身份证号")
    private String idCardNo;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "生日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "学历")
    private Integer education;

    @Schema(description = "籍贯")
    private String nativePlace;

    @Schema(description = "职业")
    private String occupation;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "工作地")
    private String workPlace;

    @Schema(description = "在职状态")
    private Integer workingState;

    @Schema(description = "入职日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime onboardDate;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "角色id列表")
    protected List<Long> roleIdList;

    @Schema(description = "角色编号列表")
    private List<String> roleCodeList;

    @Schema(description = "权限编号列表")
    private List<String> permCodeList;

    @Schema(description = "路由信息列表")
    private List<RouterTreeResultVO> routerList;

    @Schema(description = "过期提醒阈值(单位：天，默认：0，不提醒)")
    private Integer remindThreshold;

    @Schema(description = "密码过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime passwordExpireTime;

    @Schema(description = "密码到期天数")
    private Long pwdExpireDays;

}
