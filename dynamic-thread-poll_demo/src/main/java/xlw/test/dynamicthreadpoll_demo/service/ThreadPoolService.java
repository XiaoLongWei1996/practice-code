package xlw.test.dynamicthreadpoll_demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xlw.test.dynamicthreadpoll_demo.entity.ThreadPool;

/**
 * (ThreadPool)表服务接口
 *
 * @author xlw
 * @since 2024-03-16 21:06:40
 */
public interface ThreadPoolService extends IService<ThreadPool> {

    /**
     * 注册
     *
     * @param threadPool
     */
    void register(ThreadPool threadPool);

    /**
     * 更新线程池
     *
     * @param threadPool 线程池
     * @return {@link Boolean }
     */
    Boolean updateThreadPool(ThreadPool threadPool);
}

