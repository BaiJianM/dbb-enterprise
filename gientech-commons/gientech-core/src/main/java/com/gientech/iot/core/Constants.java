package com.gientech.iot.core;

/**
 * @description: 常量属性类接口类
 * @author: 白剑民
 * @dateTime: 2023/04/17 17:37
 */
public interface Constants {

    /**
     * @description: 文件常量
     * @author: 白剑民
     * @dateTime: 2023-04-19 11:18:44
     */
    interface FileConstant {
        /**
         * 保存文件所在路径的key
         */
        String FILE_MD5_KEY = "FILE_MD5:";
        /**
         * 保存上传文件的状态
         */
        String FILE_UPLOAD_STATUS = "FILE_UPLOAD_STATUS";
    }

    /**
     * @description: 用户登录信息缓存常量
     * @author: 白剑民
     * @dateTime: 2023/05/23 10:37
     */
    interface LoginUser {
        /**
         * 用户登录信息缓存前缀
         */
        String LOGIN_USER_PREFIX = "sys_user_info:";
    }

}
