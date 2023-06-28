package top.dabaibai.core.utils;

import top.dabaibai.core.pojo.vo.BaseUserInfoVO;
import org.springframework.core.NamedThreadLocal;

/**
 * @description: 获取当前用户信息
 * @author: 白剑民
 * @dateTime: 2022-09-15 09:12:51
 */
public class UserInfoUtils {

    /**
     * 当前线程存储登录用户信息
     */
    private static final ThreadLocal<BaseUserInfoVO> USER_LOCAL = new NamedThreadLocal<>("ThreadLocal UserInfoUtils");

    /**
     * @description: 获取当前登录用户信息
     * @author: 白剑民
     * @date: 2023-02-14 17:14:10
     * @return: UserLoginVO
     * @version: 1.0
     */
    public static BaseUserInfoVO getUserInfo() {
        return USER_LOCAL.get() == null ? new BaseUserInfoVO(): USER_LOCAL.get();
    }

    /**
     * @param vo 用户信息
     * @description: 设置当前登录用户信息
     * @author: 白剑民
     * @date: 2023-02-14 17:14:13
     * @return: void
     * @version: 1.0
     */
    public static void setUserInfo(BaseUserInfoVO vo) {
        USER_LOCAL.set(vo);
    }

    /**
     * @description: 清除当前登录用户信息（防止内存泄露）
     * @author: 白剑民
     * @date: 2023-02-14 17:14:22
     * @return: void
     * @version: 1.0
     */
    public static void clearUserInfo() {
        USER_LOCAL.remove();
    }

    /**
     * @description: 获取登录用户id
     * @author: 白剑民
     * @date: 2023-02-14 17:14:26
     * @return: Long
     * @version: 1.0
     */
    public static Long getUserId() {
        return getUserInfo().getUserId();
    }

    /**
     * @description: 获取登录用户子系统id
     * @author: 白剑民
     * @date: 2023-02-14 17:14:26
     * @return: Long
     * @version: 1.0
     */
    public static Long getSystemId() {
        return getUserInfo().getSystemId();
    }

    /**
     * @description: 获取登录用户ip
     * @author: 白剑民
     * @date: 2023-02-14 17:14:27
     * @return: String
     * @version: 1.0
     */
    public static String getIpAddress() {
        return getUserInfo().getIpAddress();
    }
}
