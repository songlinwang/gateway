package com.wsl.bus;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 自定义事件
 *
 * @author wsl
 * @date 2019/7/15
 */
public class UserRemoteApplicationEvent extends RemoteApplicationEvent {
    private UserRemoteApplicationEvent() {
    }

    public UserRemoteApplicationEvent(User user, String originService,
                                      String destinationService) {
        super(user, originService, destinationService);
    }
}
