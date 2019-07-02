package com.wsl.server.register;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wsl
 * @date 2019/7/2
 */
public class DefaultServiceRegister implements ServiceRegister {
    private Map<String, ServiceObject> serviceMap = new HashMap<>();
    @Override
    public void register(ServiceObject serviceObject, String protocol, int port) throws Exception {
        if (serviceObject == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        this.serviceMap.put(serviceObject.getName(), serviceObject);
    }

    @Override
    public ServiceObject getServiceObject(String name) throws Exception {
        return this.serviceMap.get(name);
    }
}
