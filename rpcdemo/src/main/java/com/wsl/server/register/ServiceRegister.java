package com.wsl.server.register;

/**
 * @author wsl
 * @date 2019/7/2
 */
public interface ServiceRegister {

    void register(ServiceObject serviceObject, String protocol, int port) throws Exception;

    ServiceObject getServiceObject(String name) throws Exception;
}
