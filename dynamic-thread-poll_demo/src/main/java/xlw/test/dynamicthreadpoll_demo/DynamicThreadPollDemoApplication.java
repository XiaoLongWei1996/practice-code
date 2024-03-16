package xlw.test.dynamicthreadpoll_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan(value = {"xlw.test.dynamicthreadpoll_demo.dao"})
@SpringBootApplication
public class DynamicThreadPollDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicThreadPollDemoApplication.class, args);
    }

}
