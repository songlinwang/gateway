package com.wsl.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wsl
 * @date 2019/7/2
 */
@Data
public class Response implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4317845782629589997L;

    private Status status;

    private Map<String, String> headers = new HashMap<>();

    private Object returnValue;

    private Exception exception;

    public Response(Status status) {
        this.status = status;
    }

    public Response() {

    }
}
