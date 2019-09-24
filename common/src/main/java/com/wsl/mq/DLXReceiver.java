package com.wsl.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wsl
 * @date 2019/9/23
 */
@Component
@Slf4j
public class DLXReceiver {

    @RabbitListener(queues = RabbitMqConfigDemo.DIRECT_QUEUE)
    public void orderDelay(String messageStr, Channel channel, Message message) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        log.error("订单关闭提示时间: {}" + simpleDateFormat.format(date));
        log.error("receive message {}" + messageStr);
    }
}
