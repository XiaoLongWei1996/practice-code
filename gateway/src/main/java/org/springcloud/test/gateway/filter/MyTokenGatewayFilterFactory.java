package org.springcloud.test.gateway.filter;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 肖龙威
 * @date 2022/10/13 16:30
 */
@Component
public class MyTokenGatewayFilterFactory implements GatewayFilterFactory<MyTokenGatewayFilterFactory.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();
            List<String> token = headers.get("token");
            if (ObjectUtil.isEmpty(token)) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                return response.setComplete();
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
