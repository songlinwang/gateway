package com.wsl.client.net;

import com.wsl.discovery.ServiceInfo;

/**
 * @author wsl
 * @date 2019/7/1
 */
public interface NetClient {
    byte[] sendRequest(byte[] data, ServiceInfo serviceInfo) throws Throwable;
}
