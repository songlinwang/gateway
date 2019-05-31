package com.wsl.service;

import com.alibaba.fastjson.JSONObject;
import com.wsl.mq.OrderMq;
import com.wsl.mq.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author wsl
 * @date 2019/5/31
 */
public class ProductService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 生产订单消息
     * <p>
     * 插入数据库失败的话 直接rollback
     *
     * @param message
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean productTradeOrder(String message) {

        OrderMq orderMq = new OrderMq();
        orderMq.setMessageId(System.currentTimeMillis());
        orderMq.setContent(message);
        // 0 表示发送失败 以及未发送 1 表示发送成功
        orderMq.setMessageStatus(0);
        // 创建订单mq消息
        redisTemplate.opsForSet().add("OrderMq", orderMq);
        // 创建商品订单
        redisTemplate.opsForList().leftPush("Order", message);
        //采用mq发送消息
        // fixme 假如发送到 mq的commit信息失败了。这时候 可以加一个定时任务，轮询发送没有成功的消息，持续发送 直至成功
        sendOrder(orderMq);
        return true;

    }

    /**
     * 发送订单消息到mq
     */
    public void sendOrder(OrderMq orderMq) {
        // 消息的id 会随着mq的回调被接受到 进而可以查看消息的状态
        CorrelationData correlationData = new CorrelationData(orderMq.getMessageId().toString());
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE, RabbitMqConfig.ROUTING_KEY1, JSONObject.toJSON(orderMq));
    }

    /**
     * ack表示mq是否处理成功
     */
    private final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        if (ack) {
            //todo 将订单mq消息 的发送状态置为 1 表示发送成功
        } else {
            System.out.println("相应的异常处理");
        }
    };
}
