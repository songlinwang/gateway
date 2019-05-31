package com.wsl.service;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author wsl
 * @date 2019/5/31
 */
public class PrizeConsumer {

    @RabbitListener(queues = "topic.queue1")
    public void receiver(Message message, Channel channel) {
        // 这个属性里面可以跟一些信息 比如说 消费了几次
        MessageProperties messageProperties = message.getMessageProperties();
        ConsumerMq consumerMq = (ConsumerMq) JSONObject.parse(message.getBody());
        // 需要配置文件中打开自动确认 如果不自动确认 mq可能会持续的发送信息
        try {
            // TODO 接收到订单信息后处理 剩下的逻辑 比如说派单，但是如果派单是 多张表  是不是意味着也要加事务呢？
            // todo 害怕回到多次消息 要有个机制 保证幂等性  redis的话 要知道有内存淘汰策略的
            // 手动ack需要获取channel  如果上述逻辑通了 直接手动ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {

        }

    }
}
