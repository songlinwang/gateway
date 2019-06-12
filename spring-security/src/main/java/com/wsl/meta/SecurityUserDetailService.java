package com.wsl.meta;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author wsl
 * @date 2019/6/12
 */
@Component
public class SecurityUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        // step 1  查询用户信息 如果查不到 抛出异常
        // step 2  返回userDetail对象
        return null;
    }
}
