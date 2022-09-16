package com.springcloud.test.system.config.loadbalancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 肖龙威
 * @date 2022/09/15 15:59
 */
@Configuration
public class RandomConfig {

    /**
     * 配置随机负载均衡
     * @param serviceInstanceListSupplierProvider
     * @return
     */
    @Bean
    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        return new RandomLoadBalancer(serviceInstanceListSupplierProvider);
    }


}
