package com.service.consumer.controller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wsl
 * @date 2019/1/4
 */
@FeignClient("service-provider")
@Component
public interface ConsumerClient {

    @RequestMapping(method = RequestMethod.GET, value = "/add/{a}/{b}")
    Integer add(@PathVariable(value = "a") Integer a, @PathVariable(value = "b") Integer b);
}
