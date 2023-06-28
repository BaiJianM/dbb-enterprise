package com.gientech.iot.user.biz.constant;

/**
 * @description: 用户常量
 * @author: 王强
 * @dateTime: 2023-05-23 20:45:38
 */
public interface Constants {

    // TODO 复制粘贴的注释要修改
    /**
     * @description: 编号规则所用前缀
     * @author: hyj
     * @dateTime: 2022/11/29 17:37
     */
    interface CodeRule {
        // TODO 复制粘贴的代码要修改
        /**
         * 部门规则所用前缀
         */
        String DEPT_CODE_PREFIX = "BM";
    }

    // TODO 复制粘贴的注释要修改
    /**
     * @description: key值相关常量
     * @author: hyj
     * @dateTime: 2022/11/29 17:37
     */
    interface RedisConfig {
        /**
         * 分布式编码生成锁前缀
         */
        String LOCK_PREFIX = "lock:%s";

        /**
         * 基础数据
         **/
        String BASE_FORMAT = "base:%s";

        /**
         * 所有字典的缓存key
         */
        String ALL_DICT_CACHE = "all_dict_cache";

        /**
         * 字典缓存锁
         */
        String DICT_LOCK = "dict_lock";
    }
}
