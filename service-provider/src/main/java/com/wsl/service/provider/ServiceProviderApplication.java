package com.wsl.service.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wsl
 * @date 2019/1/4
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceProviderApplication {
    /**
     * EnableDiscoveryClient 向服务中心注册
     */
    public static void main(String[] args) {
        SpringApplication.run(ServiceProviderApplication.class, args);
    }
}
