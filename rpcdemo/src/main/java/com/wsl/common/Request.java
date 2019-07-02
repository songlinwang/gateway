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
public class Request implements Serializable {

    private static final long serialVersionUID = -5200571424236772650L;

    private String serviceName;

    private String method;

    private Map<String, String> headers = new HashMap<String, String>();

    private Class<?>[] prameterTypes;

    private Object[] parameters;
}
