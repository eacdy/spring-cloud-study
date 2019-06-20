package com.itmuch.cloud.study.ribbon;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.collect.Lists;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author limu.zl
 */
@Slf4j
public class NacosWeightRandomV1Rule extends AbstractLoadBalancerRule {
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object key) {
        List<Server> servers = this.getLoadBalancer().getReachableServers();

        List<InstanceWithWeight> instanceWithWeights = servers.stream()
                .map(server -> {
                    // 注册中心只用Nacos，没同时用其他注册中心（例如Eureka），理论上不会实现
                    if (!(server instanceof NacosServer)) {
                        log.error("参数非法，server = {}", server);
                        throw new IllegalArgumentException("参数非法，server不是NacosServer实例！");
                    }

                    NacosServer nacosServer = (NacosServer) server;
                    Instance instance = nacosServer.getInstance();
                    double weight = instance.getWeight();
                    return new InstanceWithWeight(
                            server,
                            Double.valueOf(weight).intValue()
                    );
                })
                .collect(Collectors.toList());

        Server server = this.weightRandom(instanceWithWeights);

        log.info("选中的server = {}", server);
        return server;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class InstanceWithWeight {
        private Server server;
        private Integer weight;
    }

    /**
     * 根据权重随机
     * 算法参考 https://blog.csdn.net/u011627980/article/details/79401026
     *
     * @param list 实例列表
     * @return 随机出来的结果
     */
    private Server weightRandom(List<InstanceWithWeight> list) {
        List<Server> instances = Lists.newArrayList();
        for (InstanceWithWeight instanceWithWeight : list) {
            int weight = instanceWithWeight.getWeight();
            for (int i = 0; i <= weight; i++) {
                instances.add(instanceWithWeight.getServer());
            }
        }
        int i = new Random().nextInt(instances.size());
        return instances.get(i);
    }
}
