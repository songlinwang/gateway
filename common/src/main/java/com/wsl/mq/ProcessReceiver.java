package com.wsl.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author wsl
 * @date 2019/5/29
 */
@Component
@Slf4j
public class ProcessReceiver implements ChannelAwareMessageListener {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public static final String FAIL_MESSAGE = "This message will fail";

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            processMessage(message);
        } catch (Exception e) {
            // 如果发生了异常，则将消息重定向到缓冲队列 会在一定延迟之后自动重做
            MessageProperties messageProperties = message.getMessageProperties();
            Map<String, Object> headerMap = messageProperties.getHeaders();
            // 给死信队列 队列虽然绑定了自己的交换机 但是一旦过期还是会送到消费者的交换机被消费。
            // 他绑定交换机相当于白绑定了，也没消费者立即消费他 因为是实现延迟消费
            // fixme  发送的json ，消费者受到消息 直接message.getBody。如果直接发送message呢？
            rabbitTemplate.convertAndSend(RabbitMqConfig.DELAY_QUEUE_PER_QUEUE_TTL_NAME, message, msg -> {
                msg.getMessageProperties().setExpiration(RabbitMqConfig.QUEUE_EXPIRATION.toString());
                return msg;
            });

            /*MessageProperties messageProperties = message.getMessageProperties();
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            Integer republishTimes = (Integer) headers.get(X_REPUBLISH_TIMES);
            if (republishTimes != null) { //如果超过了重试次数，直接返回
                if (republishTimes >= recoverTimes) {
                    log.warn(String.format("this message [ %s] republish times >= %d times, and will discard",
                            message.toString(), RabbitConstant.DEFAULT_REPUBLISH_TIMES));
                    return;
                } else {
                    republishTimes = republishTimes + 1; //重试次数+1
                }
            } else {
                republishTimes = 1;
            }
            headers.put(RepublishDeadLetterRecoverer.X_REPUBLISH_TIMES, republishTimes);
            messageProperties.setRedelivered(true);
            headers.put(X_EXCEPTION_STACKTRACE, getStackTraceAsString(cause));
            headers.put(X_EXCEPTION_MESSAGE,
                    cause.getCause() != null ? cause.getCause().getMessage() : cause.getMessage());
            headers.put(X_ORIGINAL_EXCHANGE, message.getMessageProperties().getReceivedExchange());
            headers.put(X_ORIGINAL_ROUTING_KEY, message.getMessageProperties().getReceivedRoutingKey());
            String routingKey = genRouteKey(message);
            this.errorTemplate.send("DeadLetterExchange", routingKey, message);
            log.info("The #" + republishTimes + " republish message ["
                    + message.getMessageProperties().getMessageId() + "] to exchange [" + this.errorExchangeName
                    + "] and routingKey[" + routingKey + "]");*/
        }
    }

    private void processMessage(Message message) throws Exception {
        String mess = new String(message.getBody());
        log.error("message :" + mess);
        if (FAIL_MESSAGE.equals(mess)) {
            throw new Exception("some exception happended");
        }
    }
}
