package com.wsl.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * @author wsl
 * @date 2019/6/12
 */
@Configuration
/**
 *  开启spring security注解
 */
@EnableWebSecurity
@Repository
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityAuthenticationProvider authenticationProvider;

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

    @Autowired
    private DataSource dataSource;

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
                .anyRequest().authenticated();

        auth.authorizeRequests()
                .and()
                .rememberMe()
                .tokenRepository(persistentRememberMeToken())
                .tokenValiditySeconds(3000);

        auth.formLogin()
                .loginPage("/login")
                .successHandler(securityAuthenticationSuccessHandler)
                .failureHandler(securityAuthenticationFailHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll();

        auth.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
                .logoutSuccessUrl("/adminlogin")
                .deleteCookies("JSESSIONID", "remember-me");
    }


    /**
     * 如果有多个Provider 则采用链条的方式，有一个认证成功则成功  动态认证
     *
     * @return
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return null;
    }

    @Bean
    public PersistentTokenRepository persistentRememberMeToken() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }


    /**
     * 这个是静态认证  可以取消了
     *
     * @param auth
     * @throws Exception
     */
   /* @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }*/


}
