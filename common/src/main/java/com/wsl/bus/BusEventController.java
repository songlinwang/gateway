package com.wsl.bus;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wsl
 * @date 2019/7/15
 */
@RestController
public class BusEventController implements ApplicationContextAware, ApplicationEventPublisherAware {

    // 时间发布器
    private ApplicationEventPublisher eventPublisher;

    //Spring上下文
    private ApplicationContext applicationContext;

    /**
     * 发布事件
     *
     * @param user
     * @param destination
     * @return
     */
    @PostMapping("/bus/event/publish/user")
    public boolean publishUserEvent(@RequestBody User user,
                                    @RequestParam(value = "destination", required = false) String destination) {
        String serviceInstanceId = applicationContext.getId();
        UserRemoteApplicationEvent userRemoteApplicationEvent = new UserRemoteApplicationEvent(user, serviceInstanceId, destination);
        try {
            eventPublisher.publishEvent(userRemoteApplicationEvent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void onUserRemoteApplicationEvent(UserRemoteApplicationEvent event) {
        System.out.println(event.getSource());
    }
}
