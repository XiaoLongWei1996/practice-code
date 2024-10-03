package com.xlw.test.flowable_demo;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class FlowableDemoApplicationTests {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    /**
     * 部署流程
     */
    @Test
    void deploy() {
        Deployment deploy = repositoryService
                .createDeployment()     //创建部署
                .addClasspathResource("bpmn/bx.bpmn20.xml")  //添加流程文件
                .name("报销流程")  //流程名称
                .category("OA") //设置类别
                .key("BX")   //key
                .deploy();//部署
        System.out.println(JSONUtil.toJsonStr(deploy));
    }

    /**
     * 删除流程
     */
    @Test
    void deleteProcess() {
        //级联删除,会删除流程相关的实例、任务等
        repositoryService.deleteDeployment("20001", true);
    }

    /**
     * 查询流程
     */
    @Test
    void queryProcess() {
        Deployment deployment = repositoryService
                .createDeploymentQuery()
                .deploymentId("5001")
                .singleResult();
        System.out.println(deployment.getName());
    }


    /**
     * 查询流程定义
     */
    @Test
    void queryProcessDefinition() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .deploymentId("10001")
                .list();
        for (ProcessDefinition processDefinition : list) {
            System.out.println(processDefinition.getId() + ":" + processDefinition.getName());
        }
    }

    /**
     * 进程暂停或激活
     */
    @Test
    void processSuspendOrActivate() {
        //suspend 挂起
        repositoryService.suspendProcessDefinitionById("");
        //activate 激活
        repositoryService.activateProcessDefinitionById("");
    }

    /**
     * 启动流程
     */
    @Test
    void startProcess() {
        try {
            //设置流程发起人
            Authentication.setAuthenticatedUserId("柯南");
            Map<String, Object> variables = new HashMap<>();
            variables.put("user1", "小明");
            variables.put("user2", "小青");
            variables.put("money", 5000);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("BX", IdUtil.fastSimpleUUID(), variables);
            System.out.println(processInstance.getId() + ":" + processInstance.getName());
        } finally {
            //这个方法最终使用一个ThreadLocal类型的变量进行存储，也就是与当前的线程绑定，所以流程实例启动完毕之后，需要设置为null，防止多线程的时候出问题。
            Authentication.setAuthenticatedUserId(null);
        }
    }

    /**
     * handle 任务
     */
    @Test
    void handleTask() {
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("BX")  //流程定义key
                .includeProcessVariables()   //查询包含程序变量,否则task.getProcessVariables()为空
                .taskAssignee("小明")        //指定办理人
                .list();

        for (Task task : list) {
            System.out.println(task.getId() + ":" + task.getName());
            //完成任务
            Map<String, Object> processVariables = task.getProcessVariables();
            processVariables.put("status", false);
            //System.out.println(JSONUtil.toJsonStr(processVariables));
            //processVariables.put("money", 500);
            taskService.complete(task.getId(), processVariables);
            //Comment comment = taskService.addComment(task.getId(), task.getProcessInstanceId(), "拒绝");
            //System.out.println(JSONUtil.toJsonStr(comment));
            //完成任务时候设置流程变量
            //taskService.complete(task.getId(), processVariables);
            //Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
            //System.out.println(JSONUtil.toJsonStr(variables));
        }
    }

    /**
     * 查询历史任务
     */
    @Test
    void queryHistoryTask() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().finished().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println(historicTaskInstance.getTaskLocalVariables());
            System.out.println(JSONUtil.toJsonStr(historicTaskInstance));
        }
    }

    @Test
    void queryHistoryProcess() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .finished()
                .processDefinitionKey("BX").list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            System.out.println(JSONUtil.toJsonStr(historicProcessInstance));
        }
    }
}
