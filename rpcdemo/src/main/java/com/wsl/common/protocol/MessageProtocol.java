package com.wsl.common.protocol;

import com.wsl.common.Request;
import com.wsl.common.Response;

/**
 * @author wsl
 * @date 2019/7/2
 */
public interface MessageProtocol {
    byte[] marshallingRequest(Request req) throws Exception;

    Request unmarshallingRequest(byte[] data) throws Exception;

    byte[] marshallingResponse(Response rsp) throws Exception;

    Response unmarshallingResponse(byte[] data) throws Exception;
}
