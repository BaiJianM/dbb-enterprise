package com.gientech.iot.user.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gientech.iot.user.biz.entity.SysUserRole;
import com.gientech.iot.user.biz.mapper.UserRoleMapper;
import com.gientech.iot.user.biz.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 用户与角色关联实现类
 * @author: 白剑民
 * @dateTime: 2022/10/31 16:44
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, SysUserRole> implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

    @Override
    public List<Long> getUserIdsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            return userRoleMapper.getUserIdsByRoleIds(roleIds);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Long> getRoleIdsByUserIds(List<Long> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            return userRoleMapper.getRoleIdsByUserIds(userIds);
        }
        return new ArrayList<>();
    }
}
