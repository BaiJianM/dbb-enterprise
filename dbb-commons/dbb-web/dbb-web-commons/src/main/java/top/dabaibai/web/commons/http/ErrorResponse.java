package top.dabaibai.web.commons.http;

/**
 * @description: 响应异常接口类
 * @author: 白剑民
 * @dateTime: 2022/10/12 15:59
 */
public interface ErrorResponse<T> {

    /**
     * @description: 返回枚举值对象
     * @author: 白剑民
     * @date: 2022-10-12 16:43:44
     * @return: T
     * @version: 1.0
     */
    T getCode();

    /**
     * @description: 返回枚举描述
     * @author: 白剑民
     * @date: 2022-10-12 16:44:23
     * @return: java.lang.String
     * @version: 1.0
     */
    String getDescribe();

}
