package com.wsl.server;

/**
 * @author wsl
 * @date 2019/7/2
 */

public abstract class RpcServer {

    protected int port;

    protected String protocol;

    protected RequestHandler handler;

    public RpcServer(int port, String protocol, RequestHandler handler) {
        this.port = port;
        this.protocol = protocol;
        this.handler = handler;
    }

    public abstract void start();

    public abstract void stop();
}
