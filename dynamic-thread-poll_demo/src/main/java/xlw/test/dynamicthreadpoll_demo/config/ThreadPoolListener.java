package xlw.test.dynamicthreadpoll_demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import xlw.test.dynamicthreadpoll_demo.entity.ThreadPool;
import xlw.test.dynamicthreadpoll_demo.service.ThreadPoolService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @description: 线程池监听器
 * @Title: ThreadPoolListener
 * @Author xlw
 * @Package xlw.test.dynamicthreadpoll_demo.config
 * @Date 2024/3/16 22:11
 */
@Component
@Slf4j
public class ThreadPoolListener implements ApplicationListener<ApplicationStartedEvent> {

    @Resource
    private ThreadPoolService threadPoolService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("注册线程池");
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        Map<String, DynamicThreadPool> beansOfType = applicationContext.getBeansOfType(DynamicThreadPool.class);
        beansOfType.entrySet().forEach(e -> {
            String name = e.getKey();
            DynamicThreadPool dynamicThreadPool = e.getValue();
            LinkedBlockingQueue queue = (LinkedBlockingQueue) dynamicThreadPool.getQueue();
            ThreadPool tp = ThreadPool.builder()
                    .applicationName(dynamicThreadPool.getApplicationName())
                    .threadPoolName(name)
                    .coreSize(dynamicThreadPool.getCorePoolSize())
                    .maxSize(dynamicThreadPool.getMaximumPoolSize())
                    .keepAliveTime(dynamicThreadPool.getKeepAliveTime(TimeUnit.MILLISECONDS))
                    .queueSize(dynamicThreadPool.getQueueSize())
                    .queueElementsCount(queue.size())
                    .queueRemainingCapacity(queue.remainingCapacity())
                    .activeCount(dynamicThreadPool.getActiveCount())
                    .taskCount(dynamicThreadPool.getTaskCount())
                    .completedTaskCount(dynamicThreadPool.getCompletedTaskCount())
                    .largestPoolSize(dynamicThreadPool.getLargestPoolSize())
                    .loadPressure(new BigDecimal((double) dynamicThreadPool.getActiveCount() / (double) dynamicThreadPool.getMaximumPoolSize()).setScale(2, BigDecimal.ROUND_HALF_UP))
                    .build();
            threadPoolService.register(tp);
            dynamicThreadPool.setId(tp.getId());
        });
    }
}
