package com.wsl.service;

import lombok.Data;

/**
 * @author wsl
 * @date 2019/5/31
 */
@Data
public class ConsumerMq {
    private Long messageId;

    private String content;

    private int messageStatus;
}
