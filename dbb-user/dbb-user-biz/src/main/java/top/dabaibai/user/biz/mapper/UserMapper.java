package top.dabaibai.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.dabaibai.user.api.pojo.dto.UserSearchDTO;
import top.dabaibai.user.api.pojo.vo.SystemUserInfoVO;
import top.dabaibai.user.api.pojo.vo.UserDetailResultVO;
import top.dabaibai.user.api.pojo.vo.UserLoginVO;
import top.dabaibai.user.api.pojo.vo.UserSearchResultVO;
import top.dabaibai.user.biz.entity.SysUser;

import java.util.List;

/**
 * @description: 用户信息Mapper接口
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:36
 */
@Repository
public interface UserMapper extends BaseMapper<SysUser> {

    /**
     * @param page 页面
     * @param dto  入参
     * @description: 页面
     * @author: 白剑民
     * @date: 2023-05-24 13:35:51
     * @return: Page<SysUser>
     * @version: 1.0
     */
    Page<UserSearchResultVO> page(Page<SysUser> page, @Param("dto") UserSearchDTO dto);

    /**
     * @param enterpriseId 企业/机构id
     * @description: 根据企业/机构id获取其下所有用户列表
     * @author: 白剑民
     * @date: 2022-10-21 17:22:35
     * @return: java.util.List<top.dabaibai.user.entity.vo.UserInfoVO>
     * @version: 1.0
     */
    List<UserDetailResultVO> getUserListByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    /**
     * @param departmentId 部门id
     * @description: 根据部门id获取其下所有用户列表
     * @author: 白剑民
     * @date: 2022-10-24 16:56:51
     * @return: java.util.List<top.dabaibai.user.entity.vo.UserInfoVO>
     * @version: 1.0
     */
    List<UserDetailResultVO> getUserListByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * @param userId 用户id
     * @description: 根据id获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:08:29
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    SystemUserInfoVO getSystemUserInfoById(@Param("userId") Long userId);

    /**
     * @param userId 用户id
     * @description: 根据id获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:08:29
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    UserDetailResultVO getUserInfoById(@Param("userId") Long userId);

    /**
     * @param userIds 用户id列表
     * @description: 根据id列表批量获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:08:29
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    List<UserDetailResultVO> getUserInfoByIds(@Param("userIds") List<Long> userIds);

    /**
     * @param idCardNo 身份证号
     * @description: 根据身份证号获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:08:45
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    UserDetailResultVO getUserInfoByIdCardNo(@Param("idCardNo") String idCardNo);

    /**
     * @param idCardNos 身份证号
     * @description: 根据身份证号列表批量获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:08:45
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    List<UserDetailResultVO> getUserInfoByIdCardNos(@Param("idCardNos") List<String> idCardNos);

    /**
     * @param code 用户编号
     * @description: 根据用户编号获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:08:45
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    UserDetailResultVO getUserInfoByCode(@Param("code") String code);

    /**
     * @param codes 用户编号列表
     * @description: 根据用户编号列表批量获取用户信息
     * @author: 白剑民
     * @date: 2022-10-26 17:08:45
     * @return: top.dabaibai.user.entity.vo.UserInfoVO
     * @version: 1.0
     */
    List<UserDetailResultVO> getUserInfoByCodes(@Param("codes") List<String> codes);

    /**
     * @param username 账号
     * @param phone    手机号
     * @description: 根据账号或手机号获取用户及密码策略信息
     * @author: 白剑民
     * @date: 2023-04-06 13:43:24
     * @return: top.dabaibai.user.api.pojo.vo.LoginVO
     * @version: 1.0
     */
    UserLoginVO getLoginInfoByUsernameOrPhone(@Param("username") String username, @Param("phone") String phone);
}
