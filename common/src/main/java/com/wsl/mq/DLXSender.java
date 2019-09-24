package com.wsl.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wsl
 * @date 2019/9/23
 */
@Component
@Slf4j
public class DLXSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String message) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        log.error("订单生成时间: {}" + simpleDateFormat.format(date));
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        rabbitTemplate.setConnectionFactory(cachingConnectionFactory);
        rabbitTemplate.convertAndSend(RabbitMqConfigDemo.ORDER_DELAY_ROUTING_KEY, RabbitMqConfigDemo.ORDER_DELAY_EXCHANGE, message);
    }
}
