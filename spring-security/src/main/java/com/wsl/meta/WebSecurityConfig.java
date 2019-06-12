package com.wsl.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author wsl
 * @date 2019/6/12
 */
@Configuration
/**
 *  开启spring security注解
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    /**
     * 授权失败的处理逻辑
     */
    @Autowired
    private SecurityAuthenticationSuccessHandler securityAuthenticationSuccessHandler;

    /**
     * 授权成功的处理逻辑  一般是回写cookie
     */
    @Autowired
    private SecurityAuthenticationFailHandler securityAuthenticationFailHandler;

    /**
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity auth) throws Exception {
        auth.authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(securityAuthenticationSuccessHandler)
                .failureHandler(securityAuthenticationFailHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }


    /**
     * 如果有多个Provider 则采用链条的方式，有一个认证成功则成功  动态认证
     *
     * @return
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }


    /**
     * 这个是静态认证  可以取消了
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }


}
