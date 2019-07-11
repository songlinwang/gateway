package com.service.consumer.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wsl
 * @date 2019/1/4
 */
@RestController
public class ConsumerController {


    @Resource
    private ConsumerClient consumerClient;

    @RequestMapping(value = "/products/add/{a}/{b}", method = RequestMethod.GET)
    public Integer add(@PathVariable Integer a, @PathVariable Integer b) {
        return consumerClient.add(a, b);
    }
}
