package com.wsl.im;

import sun.util.calendar.BaseCalendar;

import java.io.IOException;
import java.net.Socket;

/**
 * @author wsl
 * @date 2019/7/24
 */
public class IoClient {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                while (true) {
                    try {
                        socket.getOutputStream().write(("hello word" + System.currentTimeMillis()).getBytes());
                        Thread.sleep(2000);
                    } catch (Exception e) {

                    }
                }
            } catch (IOException e) {

            }
        }).start();
    }
}
