package com.itmuch.cloud.study.ribbon;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支持优先调用同集群实例的ribbon负载均衡规则.
 *
 * @author itmuch.com
 */
@Slf4j
public class NacosRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public Server choose(Object key) {
        try {
            String clusterName = this.nacosDiscoveryProperties.getClusterName();
            DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer) getLoadBalancer();
            String name = loadBalancer.getName();

            NamingService namingService = this.nacosDiscoveryProperties.namingServiceInstance();

            List<Instance> instances = namingService.selectInstances(name, true);
            if (CollectionUtils.isEmpty(instances)) {
                log.warn("{}服务当前无任何实例", name);
                return null;
            }

            List<Instance> instancesToChoose = instances;
            if (StringUtils.isNotBlank(clusterName)) {
                List<Instance> sameClusterInstances = instances.stream()
                        .filter(instance -> Objects.equals(clusterName, instance.getClusterName()))
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(sameClusterInstances)) {
                    instancesToChoose = sameClusterInstances;
                } else {
                    log.warn("发生跨集群的调用，name = {}, clusterName = {}, instance = {}", name, clusterName, instances);
                }
            }

            Instance instance = ExtendBalancer.getHostByRandomWeight2(instancesToChoose);

            return new NacosServer(instance);
        } catch (Exception e) {
            log.warn("NacosRule发生异常", e);
            return null;
        }
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }
}