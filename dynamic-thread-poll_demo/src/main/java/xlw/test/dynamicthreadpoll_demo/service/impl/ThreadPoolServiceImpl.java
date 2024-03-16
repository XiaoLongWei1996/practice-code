package xlw.test.dynamicthreadpoll_demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xlw.test.dynamicthreadpoll_demo.dao.ThreadPoolMapper;
import xlw.test.dynamicthreadpoll_demo.entity.ThreadPool;
import xlw.test.dynamicthreadpoll_demo.service.ThreadPoolService;

/**
 * (ThreadPool)表服务实现类
 *
 * @author xlw
 * @since 2024-03-16 21:06:42
 */
@Service("threadPoolService")
public class ThreadPoolServiceImpl extends ServiceImpl<ThreadPoolMapper, ThreadPool> implements ThreadPoolService {



    @Override
    public void register(ThreadPool threadPool) {
        save(threadPool);
    }
}

