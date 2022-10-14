package org.springcloud.test.gateway.predicate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author 肖龙威
 * @date 2022/10/14 13:48
 */
@Component
public class BlackTokenRoutePredicateFactory extends AbstractRoutePredicateFactory<BlackTokenRoutePredicateFactory.Config> {

    public BlackTokenRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public ShortcutType shortcutType() {
        return ShortcutType.GATHER_LIST;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("message");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        System.out.println(config.getMessage());
        return p -> {
            return Objects.equals(config.getMessage(), "abcd");
        };
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {
        private String message;
    }
}
