package com.ecut.service.impl;

import com.ecut.mapper.LoginMapper;
import com.ecut.model.SecurityUser;
import com.ecut.service.LoginService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author zhouwei
 */
@Service
public class JwtTokenUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser = loginService.loadByUserName(username);
        if (Objects.isNull(securityUser)){
            throw new UsernameNotFoundException("用户不存在");
        }
        return securityUser;
    }
}
