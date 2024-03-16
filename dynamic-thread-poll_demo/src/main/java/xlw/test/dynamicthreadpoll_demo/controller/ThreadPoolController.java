package xlw.test.dynamicthreadpoll_demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xlw.test.dynamicthreadpoll_demo.config.DynamicThreadPool;
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
	
}

