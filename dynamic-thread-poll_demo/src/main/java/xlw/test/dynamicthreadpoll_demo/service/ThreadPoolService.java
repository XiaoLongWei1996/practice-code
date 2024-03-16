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

    void register(ThreadPool threadPool);
}

