package xlw.test.dynamicthreadpoll_demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xlw.test.dynamicthreadpoll_demo.entity.ThreadPool;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @Title: BaseConfig
 * @Author xlw
 * @Package xlw.test.dynamicthreadpoll_demo.config
 * @Date 2024/3/16 18:24
 */
@Configuration
public class BaseConfig {

    private ThreadPool threadPool;

    @Value("${spring.application.name}")
    private String applicationName;

    @PostConstruct
    public void init() {
        threadPool = threadPool
                .builder()
                .threadPoolName("userThreadPoll")
                .coreSize(4)
                .maxSize(8)
                .keepAliveTime(5000L)
                .queueSize(6)
                .build();
    }

    @Bean
    public DynamicThreadPool userThreadPoll() {
        DynamicThreadPool dynamicThreadPool = new DynamicThreadPool(
                applicationName,
                "userThreadPoll",
                threadPool.getCoreSize(),
                threadPool.getMaxSize(),
                threadPool.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                threadPool.getQueueSize(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        return dynamicThreadPool;
    }
}
