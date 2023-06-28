package top.dabaibai.user.biz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.dabaibai.user.api.pojo.dto.*;
import top.dabaibai.user.api.pojo.vo.*;
import top.dabaibai.user.biz.entity.SysUser;
import top.dabaibai.web.commons.model.PageResultVO;

import java.util.List;

/**
 * @description: 用户信息接口类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
public interface UserService extends IService<SysUser> {

    /**
     * @param dto 用户注册传参
     * @description: 用户注册
     * @author: 白剑民
     * @date: 2022-10-28 16:21:30
     * @return: top.dabaibai.user.entity.vo.UserRegisterResultVO
     * @version: 1.0
     */
    UserRegisterResultVO register(UserRegisterDTO dto);

    /**
     * @param dtoList 批量用户注册传参
     * @description: 批量用户注册
     * @author: 白剑民
     * @date: 2022-10-31 10:51:39
     * @return: java.util.List<top.dabaibai.user.entity.vo.UserRegisterResultVO>
     * @version: 1.0
     */
    List<UserRegisterResultVO> batchRegister(List<UserRegisterDTO> dtoList);

    /**
     * @param userIds 用户id列表
     * @description: 删除用户信息
     * @author: 白剑民
     * @date: 2022-10-27 10:55:34
     * @version: 1.0
     */
    void delete(List<Long> userIds);

    /**
     * @param dto 用户信息更新传参
     * @description: 用户信息更新
     * @author: 白剑民
     * @date: 2022-10-27 09:36:41
     * @version: 1.0
     */
    void update(UserUpdateDTO dto);

    /**
     * @param username 账号
     * @param systemId 应用子系统id
     * @description: 根据账号获取登录信息
     * @author: 白剑民
     * @date: 2022-11-03 14:08:29
     * @return: top.dabaibai.user.api.pojo.vo.LoginVO
     * @version: 1.0
     */
    UserLoginVO getUserLoginInfoByUsername(String username, Long systemId);

    /**
     * @param phone    手机号
     * @param systemId 应用子系统id
     * @description: 根据手机号获取登录信息
     * @author: 白剑民
     * @date: 2022-11-03 14:08:09
     * @return: top.dabaibai.user.api.pojo.vo.LoginVO
     * @version: 1.0
     */
    UserLoginVO getUserLoginInfoByPhone(String phone, Long systemId);

    /**
     * @param dto 获取用户列表入参
     * @description: 页面
     * @author: 白剑民
     * @date: 2023-05-24 10:01:07
     * @return: PageResultVO<UserInfoVO>
     * @version: 1.0
     */
    PageResultVO<UserSearchResultVO> page(UserSearchDTO dto);

    /**
     * @param enterpriseId 企业/机构id
     * @description: 获取企业/机构其下所有用户列表
     * @author: 白剑民
     * @date: 2022-10-21 17:21:05
     * @return: java.util.List<top.dabaibai.user.entity.vo.UserInfoVO>
     * @version: 1.0
     */
    List<UserDetailResultVO> getUserListByEnterpriseId(Long enterpriseId);

    /**
     * @param departmentId 部门id
     * @description: 获取指定部门下所有用户列表
     * @author: 白剑民
     * @date: 2022-10-24 16:55:46
     * @return: java.util.List<top.dabaibai.user.entity.vo.UserInfoVO>
     * @version: 1.0
     */
    List<UserDetailResultVO> getUserListByDepartmentId(Long departmentId);

    /**
     * @description: 根据token获取用户信息
     * @author: 白剑民
     * @date: 2023-04-27 10:38:10
     * @return: top.dabaibai.user.api.entity.vo.SystemUserInfoVO
     * @version: 1.0
     */
    SystemUserInfoVO getUserInfoByToken();

    /**
     * @param userId 用户id
     * @description: 根据id获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:07:13
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    UserDetailResultVO getUserInfoById(Long userId);

    /**
     * @param userIds 用户id列表
     * @description: 根据id列表批量获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:07:13
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    List<UserDetailResultVO> getUserInfoByIds(List<Long> userIds);

    /**
     * @param idCardNo 身份证号
     * @description: 根据身份证号获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:07:34
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    UserDetailResultVO getUserInfoByIdCardNo(String idCardNo);

    /**
     * @param idCardNos 身份证号列表
     * @description: 根据身份证号列表批量获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:07:34
     * @return: java.util.List<top.dabaibai.user.entity.vo.UserInfoVO>
     * @version: 1.0
     */
    List<UserDetailResultVO> getUserInfoByIdCardNos(List<String> idCardNos);

    /**
     * @param code 用户编号
     * @description: 根据用户编号获取用户信息
     * @author: 白剑民
     * @date: 2022-10-28 15:31:46
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    UserDetailResultVO getUserInfoByCode(String code);

    /**
     * @param codes 用户编号列表
     * @description: 根据用户编号列表批量获取用户信息
     * @author: 白剑民
     * @date: 2022-10-28 15:31:46
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    List<UserDetailResultVO> getUserInfoByCodes(List<String> codes);

    /**
     * @param userId 用户id
     * @description: 判断用户是否为管理员
     * @author: 白剑民
     * @date: 2022-10-27 09:59:23
     * @return: java.lang.Boolean
     * @version: 1.0
     */
    Boolean checkIsAdmin(Long userId);

    /**
     * @param userId   用户id
     * @param isEnable 启用或禁用
     * @description: 启用或禁用用户账号
     * @author: 白剑民
     * @date: 2022-10-27 10:02:51
     * @version: 1.0
     */
    void changeStatus(Long userId, Boolean isEnable);

    /**
     * @param dto 用户密码修改传参
     * @description: 用户密码修改
     * @author: 白剑民
     * @date: 2022-10-27 10:33:07
     * @version: 1.0
     */
    void updatePassword(PasswordUpdateDTO dto);

    /**
     * @param dto 入参
     * @description: 用户查询授权角色分页
     * @author: 白剑民
     * @date: 2023-05-24 15:54:47
     * @return: PageResultVO<?>
     * @version: 1.0
     */
    PageResultVO<UserAuthRoleVO> authRolePage(UserAuthRoleSearchDTO dto);

    /**
     * @param dto 入参
     * @description: 角色授权用户信息列表
     * @author: 白剑民
     * @date: 2023-05-23 12:18:48
     * @return: PageResultVO<RoleAuthUserVO>
     * @version: 1.0
     */
    PageResultVO<UserAuthRoleVO> unAuthRolePage(UserAuthRoleSearchDTO dto);

    /**
     * @param dto 入参
     * @description: 角色授权用户信息列表
     * @author: 白剑民
     * @date: 2023-05-23 12:18:48
     * @return: PageResultVO<RoleAuthUserVO>
     * @version: 1.0
     */
    void authRoleCancel(UserAuthRoleCancelDTO dto);

    /**
     * @param dto 入参
     * @description: 角色授权用户
     * @author: 白剑民
     * @date: 2023-05-23 12:43:44
     * @return: void
     * @version: 1.0
     */
    void authRoleConfirm(UserAuthRoleConfirmDTO dto);
}
