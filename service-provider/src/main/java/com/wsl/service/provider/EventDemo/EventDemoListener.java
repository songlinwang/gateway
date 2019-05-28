package com.wsl.service.provider.EventDemo;

import org.springframework.context.ApplicationListener;

/**
 * @author wsl
 * @date 2019/5/27
 */
public class EventDemoListener implements ApplicationListener<EventDemo> {
    @Override
    public void onApplicationEvent(EventDemo eventDemo) {
        System.out.println("receiver " + eventDemo.getMessage());
    }
}