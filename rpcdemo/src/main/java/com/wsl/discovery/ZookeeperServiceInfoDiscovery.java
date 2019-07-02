package com.wsl.discovery;

import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.I0Itec.zkclient.ZkClient;

import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wsl
 * @date 2019/7/1
 */
public class ZookeeperServiceInfoDiscovery implements ServiceInfoDiscoverer {


    private ZkClient zkClient;

    private String centerRootPath = "/Rpc-framework";

    public ZookeeperServiceInfoDiscovery() {
        String addr = "zk.address";
        zkClient = new ZkClient(addr);
        zkClient.setZkSerializer(new MyZkSerializer());
    }

    @Override
    public List<ServiceInfo> getServiceInfo(String name) {
        String servicePath = centerRootPath.concat("/").concat(name).concat("/service");
        List<String> children = zkClient.getChildren(servicePath);
        List<ServiceInfo> serviceInfoList = new LinkedList<>();
        for (String serviceInfo : children) {
            try {
                String dech = URLDecoder.decode(serviceInfo, "UTF-8");
                serviceInfoList.add(JSONObject.parseObject(dech, ServiceInfo.class));
            } catch (Exception e) {

            }
        }
        return serviceInfoList;
    }
}
