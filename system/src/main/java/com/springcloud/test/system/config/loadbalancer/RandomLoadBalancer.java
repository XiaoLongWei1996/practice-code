package com.springcloud.test.system.config.loadbalancer;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author 肖龙威
 * @date 2022/09/15 16:14
 */
public class RandomLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    /**
     * 服务列表
     */
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public RandomLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier available = serviceInstanceListSupplierProvider.getIfAvailable();
        return available.get().next().map(this::getInstanceResponse);
    }

    private Response<ServiceInstance> getInstanceResponse(
            List<ServiceInstance> instances) {
        int size = instances.size();
        int index = RandomUtil.randomInt(size);
        ServiceInstance serviceInstance = instances.get(index);
        return new DefaultResponse(serviceInstance);
    }
}
