package com.wsl.discovery;

import java.util.List;

/**
 * 服务发现
 *
 * @author wsl
 * @date 2019/7/1
 */
public interface ServiceInfoDiscoverer {
    List<ServiceInfo> getServiceInfo(String name);
}
