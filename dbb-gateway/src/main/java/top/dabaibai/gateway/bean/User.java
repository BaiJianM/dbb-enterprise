package top.dabaibai.gateway.bean;


import top.dabaibai.user.api.pojo.vo.UserLoginVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 用户信息
 * @author: 白剑民
 * @dateTime: 2022-10-17 16:02:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class User extends UserLoginVO implements Serializable, UserDetails {

    private static final long serialVersionUID = -2869775339642277597L;

    @Override
    public List<UserGrantedAuthority> getAuthorities() {
        return super.getRoleCodeList().stream().map(UserGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    /**
     * @description: 用户账号是否未过期
     * @author: 白剑民
     * @date: 2022-10-18 12:54:11
     * @return: boolean
     * @version: 1.0
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @description: 用户账号是否未被锁定
     * @author: 白剑民
     * @date: 2022-10-18 12:54:13
     * @return: boolean
     * @version: 1.0
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @description: 用户凭证是否未过期
     * @author: 白剑民
     * @date: 2022-10-18 12:54:15
     * @return: boolean
     * @version: 1.0
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @description: 用户账户是否已启用
     * @author: 白剑民
     * @date: 2022-10-18 12:54:17
     * @return: boolean
     * @version: 1.0
     */
    @Override
    public boolean isEnabled() {
        return super.getIsEnable();
    }

}
