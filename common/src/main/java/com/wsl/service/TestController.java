package com.wsl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wsl
 * @date 2019/5/22
 */
@RestController
@Slf4j
public class TestController {
    @Resource
    private UserService userService;

    @GetMapping(value = "/products/get/{a}/{b}")
    public void add(@PathVariable(value = "a") Integer a, @PathVariable(value = "b") Integer b) {
        log.error("获取用户名:" + userService.getUserName());
    }
}
