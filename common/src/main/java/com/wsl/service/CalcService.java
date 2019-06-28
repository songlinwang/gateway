package com.wsl.service;

import org.springframework.stereotype.Component;

/**
 * @author wsl
 * @date 2019/6/27
 */
@Component
public interface CalcService {

    /**
     * 算法的类型
     *
     * @return
     */
    String calcType();

    /**
     * 具体的算法
     *
     * @return
     */
    String calc(String string);

}
