package top.dabaibai.user.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.dabaibai.user.biz.entity.SysDepartmentUser;
import top.dabaibai.user.biz.mapper.DepartmentUserMapper;
import top.dabaibai.user.biz.service.DepartmentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 部门信息实现类
 * @author: 白剑民
 * @dateTime: 2022/10/21 16:16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DepartmentUserServiceImpl extends ServiceImpl<DepartmentUserMapper, SysDepartmentUser> implements DepartmentUserService {

    private final DepartmentUserMapper departmentUserMapper;

    @Override
    public List<Long> getUserIdsByDeptIds(List<Long> deptIds) {
        if (CollectionUtils.isNotEmpty(deptIds)) {
            return departmentUserMapper.getUserIdsByDeptIds(deptIds);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Long> getDeptIdsByUserIds(List<Long> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            return departmentUserMapper.getDeptIdsByUserIds(userIds);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysDepartmentUser> getListByDeptIds(List<Long> deptIds) {
        if (CollectionUtils.isNotEmpty(deptIds)) {
            return departmentUserMapper.getListByDeptIds(deptIds);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysDepartmentUser> getListByUserIds(List<Long> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            return departmentUserMapper.getListByUserIds(userIds);
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteByDeptIds(List<Long> deptIds) {
        if (CollectionUtils.isNotEmpty(deptIds)) {
            departmentUserMapper.deleteByDeptIds(deptIds);
        }
    }

    @Override
    public void deleteByUserIds(List<Long> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            departmentUserMapper.deleteByUserIds(userIds);
        }
    }
}
