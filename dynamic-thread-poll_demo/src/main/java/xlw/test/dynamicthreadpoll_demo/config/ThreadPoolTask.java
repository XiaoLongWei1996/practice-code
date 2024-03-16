package xlw.test.dynamicthreadpoll_demo.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xlw.test.dynamicthreadpoll_demo.entity.ThreadPool;
import xlw.test.dynamicthreadpoll_demo.service.ThreadPoolService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @Title: ThreadPoolTask
 * @Author xlw
 * @Package xlw.test.dynamicthreadpoll_demo.config
 * @Date 2024/3/16 23:51
 */
@Component
public class ThreadPoolTask implements ApplicationContextAware {

    @Resource
    private ThreadPoolService threadPoolService;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 每秒监控一次线程池
     */
    @Scheduled(fixedRate = 1000)
    public void monitorThreadPool() {
        Map<String, DynamicThreadPool> beansOfType = applicationContext.getBeansOfType(DynamicThreadPool.class);
        beansOfType.entrySet().forEach(e -> {
            String name = e.getKey();
            DynamicThreadPool dynamicThreadPool = e.getValue();
            LinkedBlockingQueue queue = (LinkedBlockingQueue) dynamicThreadPool.getQueue();
            ThreadPool tp = ThreadPool.builder()
                    .id(dynamicThreadPool.getId())
                    //.applicationName(dynamicThreadPool.getApplicationName())
                    //.threadPoolName(name)
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
            threadPoolService.updateById(tp);
        });
    }
}
