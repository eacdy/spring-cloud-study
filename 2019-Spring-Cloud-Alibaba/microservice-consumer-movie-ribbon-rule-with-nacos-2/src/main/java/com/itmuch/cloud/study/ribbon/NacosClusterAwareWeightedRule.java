package com.itmuch.cloud.study.ribbon;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.collect.Lists;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author limu.zl
 */
@Slf4j
public class NacosClusterAwareWeightedRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties discoveryProperties;
    @Value("${spring.cloud.nacos.discovery.cluster-name}")
    private String clusterName;

    @Override
    public Server choose(Object key) {
        try {
            DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer) getLoadBalancer();
            String name = loadBalancer.getName();

            NamingService namingService = discoveryProperties.namingServiceInstance();
            List<Instance> instances = namingService.selectInstances(name, Lists.newArrayList(this.clusterName), true);
            if (CollectionUtils.isEmpty(instances)) {
                instances = namingService.selectInstances(name, true);
                log.warn("发生跨集群的调用，name = {}, clusterName = {}, instance = {}", name, clusterName, instances);
            }

            Instance instance = ExtendBalancer.getHostByRandomWeight2(instances);
            /*
             * instance转server的逻辑参考自：
             * org.springframework.cloud.alibaba.nacos.ribbon.NacosServerList.instancesToServerList
             */
            return new NacosServer(instance);
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }
}