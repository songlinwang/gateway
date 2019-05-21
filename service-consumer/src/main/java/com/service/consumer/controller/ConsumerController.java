package com.service.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author wsl
 * @date 2019/1/4
 */
@RestController
public class ConsumerController {

    /*@Resource
    private RestTemplate restTemplate;

    @GetMapping(value = "/add")
    public String add() {
        return restTemplate.getForEntity("http://service-provider/add/10/20", String.class).getBody();
    }*/

    @Resource
    private ConsumerClient consumerClient;

    @RequestMapping(value = "/products/add/{a}/{b}", method = RequestMethod.GET)
    public Integer add(@PathVariable Integer a, @PathVariable Integer b) {
        return consumerClient.add(a, b);
    }
}
