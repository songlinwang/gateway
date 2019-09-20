package com.wsl.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author wsl
 * @date 2019/9/18
 */
@Component
@RabbitListener(queues = "hello")
public class HelloReceiver implements ChannelAwareMessageListener {

    /*@RabbitHandler*/
    public void process(String messageStr, Channel channel, Message message) throws Exception {
        System.out.println("receive message:" + messageStr);
        /*channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);*/
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // 消费者每次从mq中获取到的unacked消息的最大个数 （prefetch）
        channel.basicQos(50);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        System.out.println("receive message:" + message);
    }
}
