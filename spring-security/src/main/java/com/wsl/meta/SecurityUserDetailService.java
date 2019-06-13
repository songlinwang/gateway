package com.wsl.meta;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
        // 如果用户不存在 抛出异常
        if (!userName.equals("admin")) {
            throw new UsernameNotFoundException("user name is not exists");
        }
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        UserRole userRole = new UserRole();
        List<String> authorList = new LinkedList<>();
        authorList.add("AUTHED");
        // ADMIN
        authorList.add(userRole.getUserRole());
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.
                createAuthorityList(authorList.toArray(new String[authorList.size()]));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
