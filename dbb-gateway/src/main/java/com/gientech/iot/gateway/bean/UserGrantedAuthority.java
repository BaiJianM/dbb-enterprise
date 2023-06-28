package com.gientech.iot.gateway.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * @description: 用户授权信息，用于存放用户登陆后拥有的权限
 * @author: 白剑民
 * @dateTime: 2022-10-17 16:03:05
 */
public class UserGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String role;

    public UserGrantedAuthority(String uri) {
        this.role = uri;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UserGrantedAuthority) {
            return role.equals(((UserGrantedAuthority) obj).role);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }

    public void setAuthority(String role) {
        this.role = role;
    }
}
