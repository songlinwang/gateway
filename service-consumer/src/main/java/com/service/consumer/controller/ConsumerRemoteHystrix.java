package com.service.consumer.controller;

/**
 * @author wsl
 * @date 2019/7/8
 */
public class ConsumerRemoteHystrix implements ConsumerClient {
    @Override
    public Integer add(Integer a, Integer b) {
        // 服务降级
        return 0;
    }
}
