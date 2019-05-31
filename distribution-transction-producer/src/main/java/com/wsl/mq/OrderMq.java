package com.wsl.mq;

import lombok.Data;

/**
 * @author wsl
 * @date 2019/5/31
 */
@Data
public class OrderMq {

    private Long messageId;

    private String content;

    private int messageStatus;
}
