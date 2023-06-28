package top.dabaibai.user.biz.enums;

/**
 * @description: 在职状态枚举类
 * @author: 白剑民
 * @date : 2022/7/29 10:27
 */
public enum WorkingStateEnum {
    /**
     * 在职
     */
    ON_JOB(1),
    /**
     * 离职
     */
    QUIT_JOB(2),
    ;

    public Integer getCode() {
        return code;
    }

    private final Integer code;

    WorkingStateEnum(int code) {
        this.code = code;
    }


}
