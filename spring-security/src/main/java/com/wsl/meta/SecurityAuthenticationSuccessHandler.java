package com.wsl.meta;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wsl
 * @date 2019/6/12
 */
@Component
public class SecurityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
            throws IOException, ServletException {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "token, Accept, Origin, X-Requested-With, Content-Type, Last-Modified");
        //todo 这里可以采用 JWT 的格式 生成token，后期校验token
        Cookie cookie = new Cookie("NeteaseLogin", "xhsb28dgsfgab-sghzxngf2747-wjgg376126e7");
        cookie.setDomain("163.com");
        cookie.setMaxAge(-1);
        httpServletResponse.addCookie(cookie);
        httpServletResponse.getWriter().write("200");
    }
}
