package com.wsl.common;

/**
 * @author wsl
 * @date 2019/7/2
 */
public enum Status {
    // 状态成功
    SUCCESS(200,"成功"),
    // 服务器开小差了
    ERROR(500,"服务器错误"),
    // 请求地址未找到
    NOT_FOUND(404,"NOT FOUND");

    private int code;

    private String message;

    private Status(int code,String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
