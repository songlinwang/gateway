package com.wsl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wsl
 * @date 2019/5/27
 */
@Controller
public class CommonController {

    @GetMapping(value = "/")
    public String add() {
        return "index";
    }

    @GetMapping(value = "/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }
}
