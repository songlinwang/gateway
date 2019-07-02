package com.wsl.common.protocol;

import com.alibaba.fastjson.JSON;
import com.wsl.common.Request;
import com.wsl.common.Response;

/**
 * @author wsl
 * @date 2019/7/2
 */
public class JsonMessageProtocol implements MessageProtocol {
    @Override
    public byte[] marshallingRequest(Request req) throws Exception {
        Request temp = new Request();
        temp.setServiceName(req.getServiceName());
        temp.setMethod(req.getMethod());
        temp.setHeaders(req.getHeaders());
        temp.setPrameterTypes(req.getPrameterTypes());

        if (req.getParameters() != null) {
            Object[] params = req.getParameters();
            Object[] serizeParmas = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                serizeParmas[i] = JSON.toJSONString(params[i]);
            }

            temp.setParameters(serizeParmas);
        }

        return JSON.toJSONBytes(temp);
    }

    @Override
    public Request unmarshallingRequest(byte[] data) throws Exception {
        return null;
    }

    @Override
    public byte[] marshallingResponse(Response rsp) throws Exception {
        return new byte[0];
    }

    @Override
    public Response unmarshallingResponse(byte[] data) throws Exception {
        return null;
    }
}
