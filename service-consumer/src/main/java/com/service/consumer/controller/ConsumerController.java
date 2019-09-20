package com.service.consumer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wsl
 * @date 2019/1/4
 */
@RestController
@RefreshScope
public class ConsumerController {

    @Value("${from}")
    private String from;

    @Resource
    private ProductService productService;


    @Resource
    private ConsumerClient consumerClient;

    @RequestMapping(value = "/products/add/{a}/{b}", method = RequestMethod.GET)
    public Integer add(@PathVariable Integer a, @PathVariable Integer b) {
        return consumerClient.add(a, b);
    }

    @RequestMapping(value = "/products/get/{a}", method = RequestMethod.GET)
    public String get(@PathVariable Integer a) {
        return productService.find(a);
    }

}
