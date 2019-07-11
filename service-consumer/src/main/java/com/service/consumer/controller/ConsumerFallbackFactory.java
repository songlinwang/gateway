package com.service.consumer.controller;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author wsl
 * @date 2019/7/11
 */
@Component
public class ConsumerFallbackFactory implements FallbackFactory<ConsumerClient> {
    @Override
    public ConsumerClient create(Throwable throwable) {
        return new ConsumerClient() {
            @Override
            public Integer add(Integer a, Integer b) {
                if (throwable instanceof NullPointerException) {
                    return 1;
                } else{
                    return 2;
                }

            }
        };
    }
}
