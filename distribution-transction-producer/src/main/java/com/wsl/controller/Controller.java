package com.wsl.controller;

import com.wsl.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wsl
 * @date 2019/5/31
 */

@RestController
public class Controller {

    @Resource
    private ProductService productService;

    /**
     * 用户下单
     *
     * @param message
     */
    @GetMapping(value = "/products/get/{a}")
    public void add(@PathVariable(value = "a") String message) {

    }
}
