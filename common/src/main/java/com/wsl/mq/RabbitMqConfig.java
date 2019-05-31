package com.wsl.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wsl
 * @date 2019/5/29
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 发送到该队列的message会在一段时间后过期进入到delay_process_queue
     * 每个message可以控制自己的失效时间
     */
    final static String DELAY_QUEUE_PER_MESSAGE_TTL_NAME = "delay_queue_per_message_ttl";


    /**
     * queue 的过期时间
     */
    final static Integer QUEUE_EXPIRATION = 4000;

    /**
     * message失效后进入的队列，也就是实际的消费队列
     */
    final static String DELAY_PROCESS_QUEUE_NAME = "delay_process_queue";

    /**
     * DLX
     */
    final static String DELAY_EXCHANGE_NAME = "delay_exchange";

    /**
     * 创建 DLX exchange
     *
     * @return
     */
    @Bean
    DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE_NAME);
    }

    /**
     * 创建 DELAY_QUEUE_PER_MESSAGE_TTL_NAME队列
     *
     * @return
     */
    @Bean
    Queue delayQueuePerMessageTTL() {
        return QueueBuilder.durable(DELAY_QUEUE_PER_MESSAGE_TTL_NAME)
                .withArgument("x-dead-letter-exchange", DELAY_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", DELAY_PROCESS_QUEUE_NAME).build();
    }

    /**
     * 创建死信队列 实际的消费队列
     *
     * @return
     */
    @Bean
    Queue delayProcessQueue() {
        return QueueBuilder.durable(DELAY_PROCESS_QUEUE_NAME)
                .build();
    }

    /**
     * 将 DLX绑定到实际的消费队列
     *
     * @param delayProcessQueue
     * @param delayExchange
     * @return
     */
    @Bean
    Binding dlxBinding(Queue delayProcessQueue, DirectExchange delayExchange) {
        return BindingBuilder.bind(delayProcessQueue)
                .to(delayExchange)
                .with(DELAY_PROCESS_QUEUE_NAME);
    }


    /**
     * 发送到该队列的message会在一段时间后过期进入到delay_process_queue
     * 队列里所有的message都有统一的失效时间
     */
    final static String DELAY_QUEUE_PER_QUEUE_TTL_NAME = "delay_queue_per_queue_ttl";

    /**
     * 路由到delay_queue_per_queue_ttl的exchange
     */
    final static String PER_QUEUE_TTL_EXCHANGE_NAME = "per_queue_ttl_exchange";

    /**
     * 这个用来接收失败的消息 实现失败重试
     *
     * @return
     */
    @Bean
    DirectExchange perQueueTTLExchange() {
        return new DirectExchange(PER_QUEUE_TTL_EXCHANGE_NAME);
    }

    /**
     * 失败重试的队列  过期后的交换机也是 DELAY_EXCHANGE_NAME 同时 路由的queue也是  DELAY_PROCESS_QUEUE_NAME，和延迟消费一样
     *
     * @return
     */
    @Bean
    Queue delayQueuePerQueueTTL() {
        return QueueBuilder.durable(DELAY_QUEUE_PER_QUEUE_TTL_NAME)
                .withArgument("x-dead-letter-exchange", DELAY_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", DELAY_PROCESS_QUEUE_NAME)
                .withArgument("x-message-ttl", QUEUE_EXPIRATION)
                .build();
    }

    /**
     * 将 失败重试的队列和 交换机绑定。这个队列超时后也会放到 DELAY_EXCHANGE_NAME 中，供DELAY_PROCESS_QUEUE_NAME 保持一致
     * 唯一不同的是 失败重试的队列在延迟消费的场景下并没有做绑定交换机的操作，这时候RabbitMq会使用默认的Exchange 创建一个默认的Binding 类型为Direct
     *
     * @param delayQueuePerQueueTTL
     * @param perQueueTTLExchange
     * @return
     */
    @Bean
    Binding queueTTLBinding(Queue delayQueuePerQueueTTL, DirectExchange perQueueTTLExchange) {
        return BindingBuilder.bind(delayQueuePerQueueTTL)
                .to(perQueueTTLExchange).with(DELAY_QUEUE_PER_QUEUE_TTL_NAME);
    }

    /**
     * 定义一个最简单的消费者 监听delay_process_queue队列
     *
     * @param connectionFactory
     * @param processReceiver
     * @return
     */
    @Bean
    SimpleMessageListenerContainer processContainer(ConnectionFactory connectionFactory, ProcessReceiver processReceiver) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(DELAY_PROCESS_QUEUE_NAME);
        container.setMessageListener(processReceiver);
        return container;
    }

}
