package top.dabaibai.web.commons.http;

/**
 * @description: 全局状态枚举类
 * @author: 白剑民
 * @dateTime: 2022/7/8 19:05
 */
public enum SystemErrorCode implements ErrorResponse<Integer> {

    // 前端控制器响应成功
    SUCCESS(20000, "success"),

    // 前端控制器响应失败
    FAIL(10000, "fail"),

    // 服务端未知异常
    SERVER_EXCEPTION(30001, "服务端异常"),
    CONNECTION_TIMEOUT(30002, "连接超时"),

    // 接口传递参数异常
    PARAMETER_ABNORMALITY(40001, "参数异常，请查验swagger接口参数"),
    PARAMETER_MISSING(40002, "参数缺失，请查验swagger接口参数"),
    ILLEGAL_JSON(40003, "非法的JSON格式，请查验swagger接口参数格式"),
    NOT_SUPPORTED_METHOD(40004, "请求方法不匹配"),

    // 文件类型异常
    FILE_UPLOAD_NOT_EXIST(50001, "上传文件不存在或为空"),
    FILE_DELETE_NOT_EXIST(50002, "删除文件不存在"),
    ILLEGAL_FILE_TYPE(50003, "文件类型异常，不受支持的文件类型"),
    WEBSOCKET_FILE_UPLOAD_ARGUMENT_MISSING(50004, "缺少请求头中的WebSocket连接标识信息，请查阅接口文档"),

    // 请求资源类型异常
    DATA_NOT_EXIST(60001, "数据不存在"),
    GATEWAY_UN_PASS(60002, "请通过网关访问"),
    TOKEN_FAIL(60003, "请求头无Authorization或Authorization值错误"),
    TOKEN_WRONG(60004, "令牌错误或已过期"),
    AUTHORIZATION_SERVER_BROKEN(60005, "授权服务异常"),
    FORBIDDEN_FAIL(60006, "没有访问该资源的权限"),
    SETTING_CENTER_ERROR(60007, "无此用户信息"),
    USER_NAME_NOT_NULL(60008, "账号不能为空"),
    PASSWORD_NOT_NULL(60009, "密码不能为空"),
    LOGIN_FAIL(60010, "用户名或密码错误"),
    DATA_ACCESS_DENIED(600011, "用户暂无该数据权限"),
    UNSUPPORTED_WEBSOCKET_MESSAGE_TYPE(600012, "当前WebSocket模块暂不支持非字符串类型的消息"),

    // 系统级别资源请求错误
    ACCOUNT_NOT_EXIST(70001, "账号不存在"),
    LOGIN_FAILED(70002, "密码错误，登录失败"),
    USER_NAME_TEL_ONLY_ONE(70003, "用户名或手机号只能传一个"),
    USER_TEL_NOT_EXIST(70004, "手机号不存在"),
    LOGIN_TYPE_NO_NULL(70005, "loginType不能为空,0：app登陆,1：web登陆"),
    UNKNOWN_LOGIN_TYPE(70006, "未知的登陆方式"),
    USER_DO_NOT_EXIST(70007, "查无数据"),
    USER_CAN_NOT_LOGIN(70008, "该用户无权限登陆"),
    TOKEN_USERNAME_NOT_NULL(70009, "username不能为空"),
    TOKEN_PASSWORD_NOT_NULL(70010, "password不能为空"),
    USER_NOT_PERMISSION(70011, "该用户暂无权限"),

    // 系统运行异常
    MESSAGE_BUILD_ERROR(80001, "消息体为空或缺少业务标识"),
    ;

    /**
     * 异常状态码
     */
    private final Integer code;

    /**
     * 异常描述
     */
    private final String describe;

    SystemErrorCode(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDescribe() {
        return describe;
    }


}
