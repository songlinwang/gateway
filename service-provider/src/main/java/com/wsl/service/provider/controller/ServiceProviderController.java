package com.wsl.service.provider.controller;

import com.wsl.service.provider.EventDemo.EventDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wsl
 * @date 2019/1/4
 */
@RestController
@Slf4j
public class ServiceProviderController {


    private final DiscoveryClient discoveryClient;

    @Autowired
    public ServiceProviderController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping(value = "/products/add/{a}/{b}")
    public Integer add(@PathVariable(value = "a") Integer a, @PathVariable(value = "b") Integer b) {
        ServiceInstance instance = discoveryClient.getLocalServiceInstance();
        Integer r = a + b;
        log.error("/add host:" + instance.getHost() + "service_id" + instance.getServiceId());
        return r;
    }

    @GetMapping(value = "/products/get/{a}")
    public void add(@PathVariable(value = "a") String message) {
        EventDemo eventDemo = new EventDemo(this, message);
        applicationEventPublisher.publishEvent(eventDemo);
    }


}
