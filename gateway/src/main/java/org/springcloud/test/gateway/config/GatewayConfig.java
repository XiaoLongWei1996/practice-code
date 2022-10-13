package org.springcloud.test.gateway.config;

import org.springcloud.test.gateway.filter.MyGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author 肖龙威
 * @date 2022/10/13 16:12
 */
//@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("role_route",
                        p -> p
                                .path("/role/**")
                                .filters(f -> f.filter(new MyGatewayFilter()))
                                .uri("lb://system")
                )
                .build();
    }
}
