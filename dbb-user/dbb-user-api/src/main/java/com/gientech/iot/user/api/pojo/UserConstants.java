package com.gientech.iot.user.api.pojo;


import com.gientech.iot.user.api.pojo.vo.RedisCacheVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 常量属性类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
public interface UserConstants {

    /**
     * 调试时为不影响本地接口，配置服务器只访问本地的服务
     * 若部署多节点负载均衡时，需要去除feign client的url属性，使用服务名name属性进行负载均衡
     */
    String USER_FEIGN_URL = "http://172.17.0.1:7772/user";

    /**
     * @description: 锁相关常量
     * @author: 白剑民
     * @dateTime: 2022/10/17 17:37
     */
    interface RedisLock {
        /**
         * 删除部门操作锁
         */
        String DEL_DEPARTMENT_LOCK = "DEL_DEPARTMENT_LOCK:%s";
    }

    interface RedisCache {

        static List<RedisCacheVO> getCaches() {
            List<RedisCacheVO> caches = new ArrayList<>();
            caches.add(new RedisCacheVO(RedisCache.LOGIN_TOKEN_KEY, "用户信息"));
            caches.add(new RedisCacheVO(RedisCache.SYS_CONFIG_KEY, "配置信息"));
            caches.add(new RedisCacheVO(RedisCache.SYS_DICT_KEY, "数据字典"));
            caches.add(new RedisCacheVO(RedisCache.CAPTCHA_CODE_KEY, "登录验证码"));
            caches.add(new RedisCacheVO(RedisCache.PHONE_KEY, "登录验证码"));
            caches.add(new RedisCacheVO(RedisCache.REPEAT_KEY, "防重提交"));
            caches.add(new RedisCacheVO(RedisCache.RATE_LIMIT_KEY, "限流处理"));
            caches.add(new RedisCacheVO(RedisCache.PWD_ERR_CNT_KEY, "密码错误次数"));
            return caches;
        }

        /**
         * 登录用户 redis key
         */
        String LOGIN_TOKEN_KEY = "token:";

        /**
         * 接口重复请求校验 redis key
         */
        String REPEAT_KEY = "repeat:";

        /**
         * 手机验证码 redis key
         */
        String PHONE_KEY = "phone:";

        /**
         * 登录验证码 redis key
         */
        String CAPTCHA_CODE_KEY = "captcha_codes:";

        /**
         * 参数管理 cache key
         */
        String SYS_CONFIG_KEY = "sys_config:";

        /**
         * 数据字典管理 cache key
         */
        String SYS_DICT_KEY = "sys_dict:";

        /**
         * 限流 redis key
         */
        String RATE_LIMIT_KEY = "rate_limit:";

        /**
         * 登录账户密码错误次数 redis key
         */
        String PWD_ERR_CNT_KEY = "pwd_err_cnt:";
    }

    /**
     * @description: 前端路由组件类型常量
     * @author: 白剑民
     * @dateTime: 2022/10/17 17:37
     */
    interface RouterComponent {
        /**
         * 对应前端Layout组件
         */
        String LAY_OUT = "Layout";
        /**
         * 对应前端ParentView组件
         */
        String PARENT_VIEW = "ParentView";
    }

}
