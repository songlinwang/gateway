package com.wsl.service;

import com.wsl.mq.HelloSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author wsl
 * @date 2019/5/22
 */
@RestController
@Slf4j
public class TestController {
    @Resource
    private UserService userService;
    @Resource
    private HelloSender helloSender;

    @Resource
    private Map<String,CalcService> typeCalcMap;

    @GetMapping(value = "/products/get")
    public void add(HttpServletRequest request, HttpServletResponse response) {
        /*Cookie[] cookies = request.getCookies();
        Cookie cookie = new Cookie("a", "b");
        cookie.setDomain("open.game.163.com");
        cookie.setPath("/");
        response.addCookie(cookie);*/
       /* System.out.println(typeCalcMap.get("add").calc("111"));
        System.out.println(typeCalcMap.get("sub").calc("111"));*/
        helloSender.send();
    }
}
