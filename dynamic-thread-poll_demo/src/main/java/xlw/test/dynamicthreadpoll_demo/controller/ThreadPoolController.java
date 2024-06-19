package xlw.test.dynamicthreadpoll_demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xlw.test.dynamicthreadpoll_demo.config.DynamicThreadPool;
import xlw.test.dynamicthreadpoll_demo.config.Result;
import xlw.test.dynamicthreadpoll_demo.entity.ThreadPool;
import xlw.test.dynamicthreadpoll_demo.service.ThreadPoolService;

import javax.annotation.Resource;

/**
 * (ThreadPool)表控制层
 *
 * @author xlw
 * @since 2024-03-16 21:06:37
 */
@Slf4j
@RestController
@RequestMapping("threadPool")
public class ThreadPoolController {
    /**
     * 服务对象
     */
    @Resource
    private ThreadPoolService threadPoolService;

    @Resource
    private DynamicThreadPool userThreadPoll;

    @GetMapping("test")
    public String test(){
        for (int i = 0; i < 10; i++) {
            userThreadPoll.execute(() -> {
                System.out.println("执行线程");
            });
        }
        return "test";
    }

    /**
     * 查询全部
     *
     * @return {@link Result }<{@link ThreadPool }>
     */
    @GetMapping("queryAll")
    public Result<ThreadPool> queryAll() {
        return Result.succeed(threadPoolService.list());
    }

    /**
     * 按 ID 查询
     *
     * @param id ID
     * @return {@link Result }<{@link ThreadPool }>
     */
    @GetMapping("queryById")
    public Result<ThreadPool> queryById(Integer id) {
        return Result.succeed(threadPoolService.getById(id));
    }

    /**
     * 更新
     *
     * @param threadPool 线程池
     * @return {@link Result }<{@link Boolean }>
     */
    @PostMapping("update")
    public Result<Boolean> update(ThreadPool threadPool) {
        return Result.succeed(threadPoolService.updateThreadPool(threadPool));
    }
	
}

