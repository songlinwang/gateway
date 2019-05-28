package com.wsl.controller;

import com.wsl.meta.event.EventDemo;
import com.wsl.meta.event.EventDemoPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wsl
 * @date 2019/5/27
 */
@RestController
public class CommonController {
    @Autowired
    private EventDemoPublisher eventDemoPublisher;


    @GetMapping(value = "/products/get/{a}")
    public void add(@PathVariable(value = "a") String message) {
        Assert.notNull(message, "message must not be null");
        eventDemoPublisher.publish(message);
    }
}
