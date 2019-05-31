package com.wsl.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wsl
 * @date 2019/5/29
 */
@Configuration
public class RabbitMqConfig {

    public static final String DIRECT_EXCHANGE = "topic.exchange";

    public static final String DIRECT_QUEUE1 = "topic.queue1";

    public static final String ROUTING_KEY1 = "topic.key1";

    @Bean(name = "productQueue")
    public Queue productQueue() {
        return new Queue(DIRECT_QUEUE1, true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(productQueue()).to(directExchange()).with(ROUTING_KEY1);
    }
}
