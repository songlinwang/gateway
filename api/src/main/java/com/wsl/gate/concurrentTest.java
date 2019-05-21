package com.wsl.gate;

import java.util.concurrent.Executors;

/**
 * @author wsl
 * @date 2019/3/15
 */
public class concurrentTest {

    public static void main(String[] args) {

        Executors.newCachedThreadPool();
        Executors.newFixedThreadPool(2);
        Executors.newSingleThreadExecutor();

    }
}
