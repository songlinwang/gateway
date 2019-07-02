package com.wsl.client;

import com.wsl.client.net.NettyNetClient;
import com.wsl.common.Request;
import com.wsl.common.Response;
import com.wsl.common.protocol.MessageProtocol;
import com.wsl.discovery.ServiceInfo;
import com.wsl.discovery.ZookeeperServiceInfoDiscovery;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wsl
 * @date 2019/7/1
 */
public class ClientStubProxyFactory {

    private Map<String, MessageProtocol> supportMessageProtocols;

    Map<Class<?>, Object> objectCache = new HashMap<>();

    private ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    @Resource
    private NettyNetClient netClient;

    @Resource
    private ZookeeperServiceInfoDiscovery zsk;

    public <T> T getProxy(Class<T> interf) {
        T obj = (T) this.objectCache.get(interf);
        if (obj == null) {
            obj = (T) Proxy.newProxyInstance(interf.getClassLoader(), new Class[]{interf}, new ClientStubInvocationHandler(interf));
            objectCache.put(interf, obj);
        }
        return obj;
    }

    private class ClientStubInvocationHandler implements InvocationHandler {

        private Class<?> interf;

        ClientStubInvocationHandler(Class<?> interf) {
            super();
            this.interf = interf;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if (method.getName().equals("toString")) {
                return o.getClass().toString();
            }
            if (method.getName().equals("hashCode")) {
                return 0;
            }
            // 1. 获得服务信息
            String serviceName = this.interf.getName();
            List<ServiceInfo> serviceInfoList = zsk.getServiceInfo(serviceName);
            if (serviceInfoList == null || serviceInfoList.size() == 0) {
                throw new Exception("远程服务不存在");
            }
            // 2.随机选择一个服务提供者
            ServiceInfo sinfo = serviceInfoList.get(threadLocalRandom.nextInt(serviceInfoList.size()));

            // 构造Request对象
            Request request = new Request();
            request.setServiceName(serviceName);
            request.setMethod(method.getName());
            request.setPrameterTypes(method.getParameterTypes());
            request.setParameters(objects);

            MessageProtocol messageProtocol = supportMessageProtocols.get(sinfo.getProtocol());
            // request编组
            byte[] data = messageProtocol.marshallingRequest(request);
            // request解组
            byte[] rspData = netClient.sendRequest(data, sinfo);
            Response rsp = messageProtocol.unmarshallingResponse(rspData);
            if (rsp.getException() != null) {
                throw rsp.getException();
            }
            return rsp.getReturnValue();

        }
    }
}
