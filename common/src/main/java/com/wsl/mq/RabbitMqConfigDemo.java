package com.wsl.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wsl
 * @date 2019/9/18
 */
@Configuration
public class RabbitMqConfigDemo {

    public static final String DIRECT_QUEUE = "hello";
    public static final String DIRECT_EXCHANGE = "direct_exchange";
    public static final String DIRECT_ROUTING_KEY = "direct_routing_key";

    // 延迟队列 begin
    // 延迟队列名称
    public static final String ORDER_DELAY_QUEUE = "order.delay.queue";
    // 延迟队列交换机
    public static final String ORDER_DELAY_EXCHANGE = "order.delay.exchange";
    // 延迟队列路由
    public static final String ORDER_DELAY_ROUTING_KEY = "order.delay.routing.key";
    // 延迟队列 end

    // 延迟队列相关的队列
    public static final String ORDER_QUEUE = "register.queue";
    public static final String ORDER_EXCHANGE = "register.exchange";
    public static final String ORDER_ROUTING_KEY = "register.routing.key";

    @Bean
    public Queue queue() {
        return new Queue(DIRECT_QUEUE);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(DIRECT_ROUTING_KEY);
    }

    // 延迟任务

    /**
     * 延迟队列  消息发送到该队列 队列可以设置过期时间 也可以消息设置过期时间 这个队列不能有消费者
     * 请注意这个x-dead-letter-routing-key 是正常消费业务 而且延迟队列的routingkey
     *
     * @return
     */
    @Bean
    public Queue delayOrderQueue() {
        return QueueBuilder.durable(ORDER_DELAY_QUEUE).withArgument("x-dead-letter-exchange", ORDER_DELAY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ORDER_ROUTING_KEY).build();
    }

    @Bean
    public DirectExchange delayOrderExchange() {
        return new DirectExchange(ORDER_DELAY_EXCHANGE);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(delayOrderQueue()).to(delayOrderExchange()).with(ORDER_DELAY_ROUTING_KEY);
    }

    // 正常处理业务的队列

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE).build();
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDER_ROUTING_KEY);
    }


    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, HelloReceiver helloReceiver) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(DIRECT_QUEUE);
        // 如果序列化的话 ack要手动设置 不然yml配置可能会失效，这样手动ack的话 就会报错
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(helloReceiver);
        return container;
    }

    /**
     * 消费者系列化配置 生产者配置在rabbitmqtemplate中
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

    /*@Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        return connectionFactory;
    }*/

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
