package com.wsl.meta;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wsl
 * @date 2019/6/12
 */
@Component
public class SecurityAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SecurityUserDetailService securityUserDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return createSuccessAuthentication(authentication);
       /* Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        String salt = "";
        String userName = authentication.getName();
        String password = (String) authentication.getCredentials();
        // 如果用户不存在 抛出异常
        User user = (User) redisTemplate.opsForList().leftPop(userName);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 判断密码 以及账号是否停用
        String encodePwd = md5PasswordEncoder.encodePassword(password, salt);
        if (!encodePwd.equals(user.getPassword())) {
            throw new BadCredentialsException("密码不正确");
        }
        // 查看用户状态是否被停用
        if (user.getStatus().equals("STOP")) {
            // 被停用的用户
            throw new DisabledException("账户不可用");
        }
        UserRole userRole = (UserRole) redisTemplate.opsForList().leftPop(userName);
        List<String> authorList = new LinkedList<>();
        authorList.add("AUTHED");
        // ADMIN
        authorList.add(userRole.getUserRole());
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.
                createAuthorityList(authorList.toArray(new String[authorList.size()]));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userName, password, authorities);
        usernamePasswordAuthenticationToken.setDetails(user);
        return usernamePasswordAuthenticationToken;*/
    }

    /**
     * fixme 要保证密码的正确性，这个可以参考下 DaoAuthenticationProvider
     *
     * @param authentication
     * @return
     */
    private Authentication createSuccessAuthentication(Authentication authentication) {
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        String salt = "";
        String userName = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserDetails userDetails = securityUserDetailService.loadUserByUsername(userName);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //usernamePasswordAuthenticationToken.setDetails(user);
        return usernamePasswordAuthenticationToken;
    }

    /**
     * 执行支持判断 true代表支持
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
