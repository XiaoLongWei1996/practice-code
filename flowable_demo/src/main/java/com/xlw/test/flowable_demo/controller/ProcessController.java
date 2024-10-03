package com.xlw.test.flowable_demo.controller;

import com.xlw.test.flowable_demo.entiry.Result;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.Deployment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 流程控制器
 * @Title: ProcessController
 * @Author xlw
 * @Package com.xlw.test.flowable_demo.controller
 * @Date 2024/9/30 16:44
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("process")
public class ProcessController {

    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    @GetMapping("addProcess")
    public Result<String> addProcess() {
        Deployment deploy = repositoryService
                .createDeployment()
                .addClasspathResource("bpmn/qj.bpmn.xml")
                .deploy();
        return Result.success(deploy.getId());
    }
}
