package com.wsl.meta.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author wsl
 * @date 2019/5/27
 */
public class EventDemo extends ApplicationEvent {
    private String message;


    public EventDemo(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
