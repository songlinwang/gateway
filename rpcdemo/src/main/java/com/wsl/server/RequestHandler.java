package com.wsl.server;

import com.wsl.common.Request;
import com.wsl.common.Response;
import com.wsl.common.Status;
import com.wsl.common.protocol.MessageProtocol;
import com.wsl.server.register.ServiceObject;
import com.wsl.server.register.ServiceRegister;

import java.lang.reflect.Method;

/**
 * @author wsl
 * @date 2019/7/2
 */
public class RequestHandler {
    private MessageProtocol protocol;

    private ServiceRegister serviceRegister;

    public RequestHandler(MessageProtocol protocol, ServiceRegister serviceRegister) {
        this.protocol = protocol;
        this.serviceRegister = serviceRegister;
    }

    public byte[] handlerRequest(byte[] data) throws Exception {
        // 1.解组消息
        Request request = this.protocol.unmarshallingRequest(data);
        // 2.查找服务对象
        ServiceObject so = this.serviceRegister.getServiceObject(request.getServiceName());
        Response rsp = null;
        if (so == null) {
            rsp = new Response(Status.NOT_FOUND);
        } else {
            try {
                Method m = so.getInterf().getMethod(request.getMethod(), request.getPrameterTypes());
                Object returnValue = m.invoke(so.getObj(), request.getParameters());
                rsp = new Response(Status.SUCCESS);
                rsp.setReturnValue(returnValue);
            } catch (Exception e) {
                rsp = new Response(Status.ERROR);
                rsp.setException(e);
            }
        }
        // 编组响应信息
        return this.protocol.marshallingResponse(rsp);
    }
}
