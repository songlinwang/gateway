package com.wsl.meta.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author wsl
 * @date 2019/5/27
 */
@Slf4j
@Component
public class EventDemoListener implements ApplicationListener<EventDemo> {
    @Override
    public void onApplicationEvent(EventDemo eventDemo) {
        log.error("receiver " + eventDemo.getMessage());
    }
}
