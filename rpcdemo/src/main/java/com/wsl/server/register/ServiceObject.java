package com.wsl.server.register;

import lombok.Data;

/**
 * @author wsl
 * @date 2019/7/2
 */
@Data
public class ServiceObject {
    /**
     * service 名称
     */
    private String name;

    /**
     * 对应的service 类名
     */
    private Class<?> interf;

    /**
     * 用于反射 生成Obj类型的返回值
     */
    private Object obj;

    public ServiceObject(String name, Class<?> interf, Object obj) {
        this.name = name;
        this.interf = interf;
        this.obj = obj;
    }
}
