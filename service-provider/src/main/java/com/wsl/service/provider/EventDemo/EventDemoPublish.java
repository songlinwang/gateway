package com.wsl.service.provider.EventDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author wsl
 * @date 2019/5/27
 */
@Component
public class EventDemoPublish {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void push(String message) {
        EventDemo eventDemo = new EventDemo(this, message);
        applicationEventPublisher.publishEvent(eventDemo);
    }
}
