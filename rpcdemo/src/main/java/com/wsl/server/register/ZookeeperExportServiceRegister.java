package com.wsl.server.register;

import com.alibaba.fastjson.JSON;
import com.wsl.discovery.ServiceInfo;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;

/**
 * @author wsl
 * @date 2019/7/2
 */
public class ZookeeperExportServiceRegister extends DefaultServiceRegister implements ServiceRegister {

    private ZkClient client;

    private String centerRootPath = "/Rpc-framework";

    /**
     * 服务注册
     *
     * @param serviceObject
     * @param protocol
     * @param port
     * @throws Exception
     */
    @Override
    public void register(ServiceObject serviceObject, String protocol, int port) throws Exception {
        super.register(serviceObject, protocol, port);
        ServiceInfo serviceInfo = new ServiceInfo();
        String host = InetAddress.getLocalHost().getHostAddress();
        String address = host.concat(":").concat(String.valueOf(port));
        serviceInfo.setAddress(address);
        serviceInfo.setName(serviceObject.getInterf().getName());
        serviceInfo.setProtocol(protocol);
    }

    /**
     * 服务暴露到zk
     */
    private void exportService(ServiceInfo serviceInfo) {
        String serviceName = serviceInfo.getName();
        String uri = JSON.toJSONString(serviceInfo);
        try {
            uri = URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String servicePath = centerRootPath.concat("/").concat(serviceName).concat("/service");
        if (!client.exists(servicePath)) {
            client.createPersistent(servicePath);
        }
        String uriPath = servicePath.concat("/").concat(uri);
        if (client.exists(uriPath)) {
            client.delete(uriPath);
        }
        client.createEphemeral(uriPath);
    }

}
