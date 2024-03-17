package xlw.test.dynamicthreadpoll_demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import xlw.test.dynamicthreadpoll_demo.config.DynamicThreadPool;
import xlw.test.dynamicthreadpoll_demo.dao.ThreadPoolMapper;
import xlw.test.dynamicthreadpoll_demo.entity.ThreadPool;
import xlw.test.dynamicthreadpoll_demo.service.ThreadPoolService;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * (ThreadPool)表服务实现类
 *
 * @author xlw
 * @since 2024-03-16 21:06:42
 */
@Service("threadPoolService")
public class ThreadPoolServiceImpl extends ServiceImpl<ThreadPoolMapper, ThreadPool> implements ThreadPoolService {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void register(ThreadPool threadPool) {
        save(threadPool);
    }

    @Override
    public Boolean updateThreadPool(ThreadPool threadPool) {
        boolean b = updateById(threadPool);
        if (b) {
            DynamicThreadPool tp = applicationContext.getBean(threadPool.getThreadPoolName(), DynamicThreadPool.class);
            tp.setCorePoolSize(threadPool.getCoreSize());
            tp.setMaximumPoolSize(threadPool.getMaxSize());
            tp.setKeepAliveTime(threadPool.getKeepAliveTime(), TimeUnit.MILLISECONDS);
            tp.setQueueSize(threadPool.getQueueSize());
            return true;
        }
        return false;
    }
}

