package com.xlw.kafka_demo.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @className: HelloJob
 * @author: xlw
 * @date: 2023/6/8 13:51
 **/
@Component
public class HelloJob {

    //job调度器，发起job
    @Resource
    private JobLauncher jobLauncher;

    //job构建工厂
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    //步骤构建工厂
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    /**
     * job的步骤执行逻辑，逻辑由tasklet完成
     * @StepScope: 表示在启动项目的时候，不加载该Step步骤bean，等step1()被调用时才加载。这就是所谓延时获取。
     * @return
     */
    @Bean
    @StepScope
    public Tasklet tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("hello world");
                return RepeatStatus.FINISHED;
            }
        };
    }

    /**
     * 创建步骤
     * @return
     */
    @Bean
    public Step step1() {
        return stepBuilderFactory
                .get("step1")
                .tasklet(tasklet())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory
                .get("job1")
                .start(step1())
                .build();
    }



}
