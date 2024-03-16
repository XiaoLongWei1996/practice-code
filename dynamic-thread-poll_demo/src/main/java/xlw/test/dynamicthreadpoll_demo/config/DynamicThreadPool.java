package xlw.test.dynamicthreadpoll_demo.config;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.*;

/**
 * @description:
 * @Title: DynamicThreadPool
 * @Author xlw
 * @Package xlw.test.dynamicthreadpoll_demo.config
 * @Date 2024/3/16 22:15
 */
@Getter
@Setter
public class DynamicThreadPool extends ThreadPoolExecutor {

    private Integer id;

    //线程池名称
    private String threadPoolName;

    private String applicationName;

    private Integer queueSize;

    public DynamicThreadPool(String applicationName, String threadPoolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int queueSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>(queueSize), threadFactory, handler);
        this.applicationName = applicationName;
        this.threadPoolName = threadPoolName;
        this.queueSize = queueSize;
    }

}
