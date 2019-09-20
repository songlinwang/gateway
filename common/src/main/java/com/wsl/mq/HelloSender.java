package com.wsl.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author wsl
 * @date 2019/9/18
 */
@Component
@Slf4j
public class HelloSender implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void init() {
        rabbitTemplate.setConfirmCallback(this);
    }

    public void send() {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        String context = "发送消息";
        // 执行序列化方式 接受consumer采用RabbitListenerContainerFactory 的setMessageConverter的方式
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        this.rabbitTemplate.convertAndSend(RabbitMqConfigDemo.DIRECT_EXCHANGE, RabbitMqConfigDemo.DIRECT_ROUTING_KEY, context, correlationData);
      /*  rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {

            }
        });*/
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData == null ? "" : correlationData.getId();
        log.error("消息确认成功,id:{}", id);
        if (b) {
            log.error("消息确认成功,id:{}", id);
        } else {
            log.error("消息确认失败,id:{},cause:{}", id, s);
            // fixme 事务回滚
            log.error("事务回滚");
        }
    }
}
