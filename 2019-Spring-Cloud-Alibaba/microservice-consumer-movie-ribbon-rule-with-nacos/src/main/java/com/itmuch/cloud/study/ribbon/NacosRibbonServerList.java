package com.itmuch.cloud.study.ribbon;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.collect.Lists;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;

import java.util.ArrayList;
import java.util.List;

/**
 * 参考org.springframework.cloud.alibaba.nacos.ribbon.NacosServerList
 */
@Slf4j
public class NacosRibbonServerList extends AbstractServerList<NacosServer> {

    private NacosDiscoveryProperties discoveryProperties;

    private String serviceId;

    public NacosRibbonServerList(NacosDiscoveryProperties discoveryProperties) {
        this.discoveryProperties = discoveryProperties;
    }

    @Override
    public List<NacosServer> getInitialListOfServers() {
        return getServers();
    }

    @Override
    public List<NacosServer> getUpdatedListOfServers() {
        return getServers();
    }

    private List<NacosServer> getServers() {
        try {
            Instance instance = discoveryProperties.namingServiceInstance()
                    .selectOneHealthyInstance(serviceId, true);
            log.debug("选择的instance = {}", instance);
            return instancesToServerList(
                    Lists.newArrayList(instance)
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Can not get service instances from nacos, serviceId=" + serviceId,
                    e);
        }
    }

    private List<NacosServer> instancesToServerList(List<Instance> instances) {
        List<NacosServer> result = new ArrayList<>();
        if (null == instances) {
            return result;
        }
        for (Instance instance : instances) {
            result.add(new NacosServer(instance));
        }
        return result;
    }

    public String getServiceId() {
        return serviceId;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        this.serviceId = iClientConfig.getClientName();
    }
}
