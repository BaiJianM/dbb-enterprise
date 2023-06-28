package com.gientech.iot.database.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.gientech.iot.core.pojo.vo.BaseUserInfoVO;
import com.gientech.iot.core.utils.UserInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @description: 自动填充数据库字段
 * @author: 白剑民
 * @dateTime: 2022-08-10 14:36:43
 */
@Slf4j
public class FillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("start insert fill ....");
        // 获取当前用户信息
        BaseUserInfoVO userInfo = UserInfoUtils.getUserInfo();
        Long userId = userInfo.getUserId() == null ? 0L : userInfo.getUserId();
        String code = userInfo.getCode() == null ? "" : userInfo.getCode();
        String realName = userInfo.getRealName() == null ? "" : userInfo.getRealName();
        // 对新增数据时需要填充的字段进行赋值
        this.strictInsertFill(metaObject, "createUserId", Long.class, userId);
        this.strictInsertFill(metaObject, "createUserCode", String.class, code);
        this.strictInsertFill(metaObject, "createUserName", String.class, realName);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateUserId", Long.class, userId);
        this.strictInsertFill(metaObject, "updateUserCode", String.class, code);
        this.strictInsertFill(metaObject, "updateUserName", String.class, realName);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("start update fill ....");
        // 获取当前用户信息
        BaseUserInfoVO userInfo = UserInfoUtils.getUserInfo();
        Long userId = userInfo.getUserId() == null ? 0L : userInfo.getUserId();
        String code = userInfo.getCode() == null ? "" : userInfo.getCode();
        String realName = userInfo.getRealName() == null ? "" : userInfo.getRealName();
        // 对修改数据时需要填充的字段进行赋值
        this.strictInsertFill(metaObject, "updateUserId", Long.class, userId);
        this.strictInsertFill(metaObject, "updateUserCode", String.class, code);
        this.strictInsertFill(metaObject, "updateUserName", String.class, realName);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}