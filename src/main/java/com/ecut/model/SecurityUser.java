package com.ecut.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhouwei
 */
@Data
public class SecurityUser implements UserDetails {
    private String username;
    private String password;
    private List<String> authorities;

    /**
     * 不需要序列化
     */
    private List<SimpleGrantedAuthority> authList;
    public SecurityUser() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authList == null){
            authList = this.authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return authList;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
