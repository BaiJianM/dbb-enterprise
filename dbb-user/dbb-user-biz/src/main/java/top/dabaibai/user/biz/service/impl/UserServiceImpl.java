package top.dabaibai.user.biz.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.dabaibai.core.Constants;
import top.dabaibai.core.utils.BeanConvertUtils;
import top.dabaibai.core.utils.IdCardUtils;
import top.dabaibai.core.utils.TreeUtils;
import top.dabaibai.core.utils.UserInfoUtils;
import top.dabaibai.redis.utils.RedisUtils;
import top.dabaibai.user.api.pojo.dto.*;
import top.dabaibai.user.api.pojo.vo.*;
import top.dabaibai.user.biz.entity.*;
import top.dabaibai.user.biz.enums.CustomErrorCodeEnum;
import top.dabaibai.user.biz.enums.WorkingStateEnum;
import top.dabaibai.user.biz.mapper.PasswordPolicyMapper;
import top.dabaibai.user.biz.mapper.UserMapper;
import top.dabaibai.web.commons.http.DbbException;
import top.dabaibai.web.commons.model.PageResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.dabaibai.user.biz.service.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: 用户信息实现类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {

    private final UserMapper userMapper;

    private final DepartmentService departmentService;

    private final DepartmentUserService departmentUserService;

    private final PasswordPolicyMapper policyMapper;

    private final RoleService roleService;

    private final UserRoleService userRoleService;

    private final PermissionService permissionService;

    private final PasswordEncoder passwordEncoder;

    private final RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRegisterResultVO register(UserRegisterDTO dto) {
        // 更新部门人员数量
        SysDepartment sysDepartment = departmentService.getById(dto.getDepartmentId());
        // 获取企业配置的密码策略
        SysPasswordPolicy passwordPolicy = policyMapper.getPolicyByEnterpriseId(sysDepartment.getEnterpriseId());
        // 将传参字段转换赋值成用户实体属性
        SysUser user = BeanConvertUtils.convert(dto, SysUser::new, (s, t) -> {
            String idCardNo = s.getIdCardNo();
            t.setPassword(passwordEncoder.encode(s.getPassword()));
            // 如果传入身份证号，则根据身份证号解析生日、年龄、性别
            if (null != idCardNo && idCardNo.length() > 0) {
                t.setBirthday(IdCardUtils.getBirthByIdCard(idCardNo));
                t.setAge(IdCardUtils.getAgeByIdCard(idCardNo));
                t.setGender(IdCardUtils.getGenderByIdCard(idCardNo));
            }
            // 如果企业未配置或配置为永不过期，就赋一个默认超大过期天数
            LocalDateTime expireTime = (passwordPolicy == null || passwordPolicy.getValidityPeriod() == 0) ?
                    LocalDateTime.of(2099, 12, 31, 23, 59) :
                    LocalDateTime.now().plusDays(passwordPolicy.getValidityPeriod());
            t.setPasswordExpireTime(expireTime);
        }).orElseThrow(() -> new DbbException(CustomErrorCodeEnum.USER_CREATE_ERROR));
        userMapper.insert(user);
        if (dto.getDepartmentId() != null) {
            // 保存用户新关联的部门信息
            departmentUserService.save(new SysDepartmentUser(dto.getDepartmentId(), user.getId()));
        }
        // 封装返回内容
        UserRegisterResultVO resultVO = new UserRegisterResultVO();
        resultVO.setUserId(user.getId());
        return resultVO;
    }

    @Override
    public List<UserRegisterResultVO> batchRegister(List<UserRegisterDTO> dtoList) {
        // 执行注册方法
        return dtoList.stream().map(this::register).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> userIds) {
        userMapper.deleteBatchIds(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateDTO dto) {
        // 将传参字段转换赋值成用户实体属性
        SysUser user = BeanConvertUtils.convert(dto, SysUser::new, (s, t) -> t.setId(dto.getUserId())).orElse(new SysUser());
        userMapper.updateById(user);
        if (dto.getDepartmentId() != null) {
            // 删除用户之前关联的部门信息
            departmentUserService.deleteByUserIds(Collections.singletonList(dto.getUserId()));
            // 保存用户新关联的部门信息
            departmentUserService.save(new SysDepartmentUser(dto.getDepartmentId(), dto.getUserId()));
        }
        // 在职状态如修改为离职，则删除用户信息
        if (user.getWorkingState() != null && user.getWorkingState().equals(WorkingStateEnum.QUIT_JOB.getCode())) {
            // TODO 待测试事务传播机制
            this.delete(Collections.singletonList(user.getId()));
        }
    }

    @Override
    public UserLoginVO getUserLoginInfoByUsername(String username, Long systemId) {
        UserLoginVO userLoginVO = userMapper.getLoginInfoByUsernameOrPhone(username, null);
        if (userLoginVO != null) {
            userLoginVO.setSystemId(systemId);
            if (userLoginVO.getIsAdmin()) {
                userLoginVO.setRoleCodeList(Collections.singletonList("Admin"));
            } else {
                List<SysRole> roleList = roleService.listBySystemIdAndUserId(systemId, userLoginVO.getUserId());
                userLoginVO.setRoleCodeList(roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList()));
            }
        }
        return userLoginVO;
    }

    @Override
    public UserLoginVO getUserLoginInfoByPhone(String phone, Long systemId) {
        UserLoginVO userLoginVO = userMapper.getLoginInfoByUsernameOrPhone(null, phone);
        if (userLoginVO != null) {
            userLoginVO.setSystemId(systemId);
            if (userLoginVO.getIsAdmin()) {
                userLoginVO.setRoleCodeList(Collections.singletonList("Admin"));
            } else {
                List<SysRole> roleList = roleService.listBySystemIdAndUserId(systemId, userLoginVO.getUserId());
                userLoginVO.setRoleCodeList(roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList()));
            }
        }
        return userLoginVO;
    }

    @Override
    public PageResultVO<UserSearchResultVO> page(UserSearchDTO dto) {
        Page<UserSearchResultVO> page = userMapper.page(new Page<>(dto.getCurrent(), dto.getSize()), dto);
        return new PageResultVO<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), page.getRecords());
    }

    /**
     * @param userInfo 登录后的用户信息获取
     * @description: 封装角色和权限信息
     * @author: 白剑民
     * @date: 2023-04-27 10:40:02
     * @return: top.dabaibai.user.api.entity.vo.SystemUserInfoVO
     * @version: 1.0
     */
    private void setRoleAndPermission(SystemUserInfoVO userInfo) {
        // 用户不存在就抛出异常
        if (null == userInfo) {
            throw new DbbException(CustomErrorCodeEnum.USER_IS_NOT_EXIST);
        }
        // 获取用户所有角色
        List<SysRole> roles = roleService.listBySystemIdAndUserId(userInfo.getSystemId(), userInfo.getUserId());
        // 角色id列表
        List<Long> roleIds = new ArrayList<>();
        // 角色编号列表
        List<String> roleCodes = new ArrayList<>();
        // 遍历所有角色
        for (SysRole r : roles) {
            roleIds.add(r.getId());
            roleCodes.add(r.getRoleCode());
        }
        // 赋值角色编号列表
        userInfo.setRoleCodeList(roleCodes);
        List<PermissionSearchResultVO> permissionList = new ArrayList<>();
        // 管理员查询所有权限信息
        if (userInfo.getIsAdmin()) {
            PermissionSearchDTO searchDTO = new PermissionSearchDTO();
            searchDTO.setIsEnable(true);
            searchDTO.setSystemId(userInfo.getSystemId());
            permissionList = permissionService.list(searchDTO);
            userInfo.setPermCodeList(Collections.singletonList("*:*:*"));
            userInfo.setRoleCodeList(Collections.singletonList("Admin"));
        }
        // 如果存在角色信息
        else if (roleIds.size() > 0) {
            // 获取角色下所有权限列表
            permissionList = permissionService.getListByRoleIds(roleIds);
            List<String> permList = permissionList.stream()
                    .map(PermissionSearchResultVO::getPermissionCode).distinct().collect(Collectors.toList());
            userInfo.setPermCodeList(permList);
        }
        // 过滤出路由权限
        List<PermissionSearchResultVO> collect = permissionList.stream()
                .filter(p -> p.getPermissionType().isRouter()).collect(Collectors.toList());
        // 路由权限封装成路由列表
        userInfo.setRouterList(TreeUtils.listToTree(collect, RouterTreeResultVO::new, (e, v) -> {
            v.setId(e.getPermissionId());
            v.setMeta(e.getMeta());
            v.setName(e.getPermissionUrl());
            v.setPath(e.getPermissionUrl());
            v.setType(e.getPermissionType());
            v.setHidden(!e.getIsEnable());
            v.setRedirect(false);
        }));
    }

    @Override
    public List<UserDetailResultVO> getUserListByEnterpriseId(Long enterpriseId) {
        return userMapper.getUserListByEnterpriseId(enterpriseId);
    }

    @Override
    public List<UserDetailResultVO> getUserListByDepartmentId(Long departmentId) {
        return userMapper.getUserListByDepartmentId(departmentId);
    }

    @Override
    public SystemUserInfoVO getUserInfoByToken() {
        SystemUserInfoVO userInfo = userMapper.getSystemUserInfoById(UserInfoUtils.getUserId());
        userInfo.setSystemId(UserInfoUtils.getSystemId());
        // 缓存用户信息，一般在用户登出后清空，避免数据产生，缓存有效期24小时
        String userKey = Constants.LoginUser.LOGIN_USER_PREFIX + UserInfoUtils.getUserId();
        redisUtils.setEx(userKey, JSON.toJSONString(userInfo), 1, TimeUnit.DAYS);
        // 设置密码到期天数
        userInfo.setPwdExpireDays(DateUtil.betweenDay(DateUtil.date(),
                DateTime.from(userInfo.getPasswordExpireTime().atZone(ZoneId.systemDefault()).toInstant()), false));
        this.setRoleAndPermission(userInfo);
        return userInfo;
    }

    @Override
    public UserDetailResultVO getUserInfoById(Long userId) {
        return userMapper.getUserInfoById(userId);
    }

    @Override
    public List<UserDetailResultVO> getUserInfoByIds(List<Long> userIds) {
        return userMapper.getUserInfoByIds(userIds);
    }

    @Override
    public UserDetailResultVO getUserInfoByIdCardNo(String idCardNo) {
        return userMapper.getUserInfoByIdCardNo(idCardNo);
    }

    @Override
    public List<UserDetailResultVO> getUserInfoByIdCardNos(List<String> idCardNos) {
        return userMapper.getUserInfoByIdCardNos(idCardNos);
    }

    @Override
    public UserDetailResultVO getUserInfoByCode(String code) {
        return userMapper.getUserInfoByCode(code);
    }

    @Override
    public List<UserDetailResultVO> getUserInfoByCodes(List<String> codes) {
        return userMapper.getUserInfoByCodes(codes);
    }

    @Override
    public Boolean checkIsAdmin(Long userId) {
        return userMapper.selectById(userId).getIsAdmin();
    }

    @Override
    public void changeStatus(Long userId, Boolean isEnable) {
        SysUser user = userMapper.selectById(userId);
        user.setIsEnable(isEnable);
        userMapper.updateById(user);
    }

    @Override
    public void updatePassword(PasswordUpdateDTO dto) {
        SysUser user = userMapper.selectById(dto.getUserId());
        // 原密码
        String oldPassword = user.getPassword();
        // 判断是否需要校验原密码
        if (dto.getIsNeedCheck()) {
            // 校验原密码
            if (!passwordEncoder.matches(dto.getOldPassword(), oldPassword)) {
                throw new DbbException(CustomErrorCodeEnum.OLD_PASSWORD_NOT_MATCH_ERROR);
            }
        }
        // 赋值新密码
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        // 取用户归属企业/机构的密码策略
        SysPasswordPolicy policy =
                policyMapper.getPolicyByEnterpriseId(
                        // 根据用户id获取用户信息，并从中取出关联的企业/机构id
                        userMapper.getSystemUserInfoById(dto.getUserId()).getEnterpriseId());
        // 从密码策略中取出密码有效期(单位：天)
        Integer validityPeriod = policy.getValidityPeriod();
        // 重置密码过期时间(当前过期时间 + 密码有效期 = 新的过期时间)
        LocalDateTime newExpireTime = LocalDateTime.now().plusDays(validityPeriod);
        user.setPasswordExpireTime(newExpireTime);
        userMapper.updateById(user);
    }

    @Override
    public PageResultVO<UserAuthRoleVO> authRolePage(UserAuthRoleSearchDTO dto) {
        List<Long> roleIds = userRoleService.getRoleIdsByUserIds(Collections.singletonList(dto.getUserId()));
        Page<SysRole> page = roleService.page(new Page<>(dto.getCurrent(), dto.getSize()),
                new LambdaQueryWrapper<SysRole>().eq(CollectionUtils.isEmpty(roleIds), SysRole::getId, 0)
                        .in(CollectionUtils.isNotEmpty(roleIds), SysRole::getId, roleIds)
                        .like(StringUtils.isNotEmpty(dto.getRoleName()), SysRole::getRoleName, dto.getRoleName()));
        List<UserAuthRoleVO> list = (List<UserAuthRoleVO>) BeanConvertUtils.convertCollection(page.getRecords(), UserAuthRoleVO::new, (s, t) -> {
            t.setRoleId(s.getId());
        }).orElse(new ArrayList<>());
        return new PageResultVO<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), list);
    }

    @Override
    public PageResultVO<UserAuthRoleVO> unAuthRolePage(UserAuthRoleSearchDTO dto) {
        List<Long> roleIds = userRoleService.getRoleIdsByUserIds(Collections.singletonList(dto.getUserId()));
        Page<SysRole> page = roleService.page(new Page<>(dto.getCurrent(), dto.getSize()),
                new LambdaQueryWrapper<SysRole>()
                        .notIn(CollectionUtils.isNotEmpty(roleIds), SysRole::getId, roleIds)
                        .like(StringUtils.isNotEmpty(dto.getRoleName()), SysRole::getRoleName, dto.getRoleName()));
        List<UserAuthRoleVO> list = (List<UserAuthRoleVO>) BeanConvertUtils.convertCollection(page.getRecords(), UserAuthRoleVO::new, (s, t) -> {
            t.setRoleId(s.getId());
        }).orElse(new ArrayList<>());
        return new PageResultVO<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), list);
    }

    @Override
    public void authRoleCancel(UserAuthRoleCancelDTO dto) {
        userRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, dto.getUserId())
                .in(SysUserRole::getRoleId, dto.getRoleIds()));
    }

    @Override
    public void authRoleConfirm(UserAuthRoleConfirmDTO dto) {
        List<Long> roleIds = userRoleService.getRoleIdsByUserIds(Collections.singletonList(dto.getUserId()));
        List<SysUserRole> userRoleList = dto.getRoleIds().stream()
                .filter(p -> !roleIds.contains(p))
                .map(p -> new SysUserRole(dto.getUserId(), p)).collect(Collectors.toList());
        // 创建关联信息
        if (CollectionUtils.isNotEmpty(userRoleList)) {
            userRoleService.saveBatch(userRoleList);
        }
    }
}
