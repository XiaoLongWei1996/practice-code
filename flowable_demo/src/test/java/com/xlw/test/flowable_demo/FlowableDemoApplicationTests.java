package com.xlw.test.flowable_demo;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    @Test
    void queryProcessInstance() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("70001")
                .singleResult();
        System.out.println(processInstance.getId() + ":" + processInstance.getName() + ":" + processInstance.getBusinessKey()
                + ":" + processInstance.getBusinessStatus());
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

    @Test
    void queryTask() {
        //查询变量
        List<HistoricVariableInstance> variableList = historyService.createHistoricVariableInstanceQuery().processInstanceId("70001").list();

        //查询流程
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey("f606ad4be4aa4c6990c40be90e7efe03").singleResult();
        List<Process> processes = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId()).getProcesses();
        for (Process process : processes) {
            Collection<FlowElement> flowElements = process.getFlowElements();
            for (FlowElement flowElement : flowElements) {
                System.out.println(flowElement.getName() + ":" + flowElement.getId());
            }
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

    /**
     * 查看流程进度图
     *
     * @throws IOException io异常
     */
    @Test
    void processImg() throws IOException {
        String processInstanceId = "70001";
        String procDefId;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            procDefId = historicProcessInstance.getProcessDefinitionId();
        } else {
            procDefId = processInstance.getProcessDefinitionId();
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
        DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new DefaultProcessDiagramGenerator(); // 创建默认的流程图生成器
        String imageType = "png"; // 生成图片的类型
        List<String> highLightedActivities = new ArrayList<>(); // 高亮节点集合
        List<String> highLightedFlows = new ArrayList<>(); // 高亮连线集合
        List<HistoricActivityInstance> hisActInsList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list(); // 查询所有历史节点信息
        hisActInsList.forEach(historicActivityInstance -> { // 遍历
            if ("sequenceFlow".equals(historicActivityInstance.getActivityType())) {
                // 添加高亮连线
                highLightedFlows.add(historicActivityInstance.getActivityId());
            } else {
                // 添加高亮节点
                highLightedActivities.add(historicActivityInstance.getActivityId());
            }
        });
        String activityFontName = "宋体"; // 节点字体
        String labelFontName = "微软雅黑"; // 连线标签字体
        String annotationFontName = "宋体"; // 连线标签字体
        ClassLoader customClassLoader = null; // 类加载器
        double scaleFactor = 1.0d; // 比例因子，默认即可
        boolean drawSequenceFlowNameWithNoLabelDI = true; // 不设置连线标签不会画
        // 生成图片
        InputStream inputStream = defaultProcessDiagramGenerator.generateDiagram(bpmnModel, imageType, highLightedActivities
                , highLightedFlows, activityFontName, labelFontName, annotationFontName, customClassLoader,
                scaleFactor, drawSequenceFlowNameWithNoLabelDI); // 获取输入流
        ImageIO.write(ImageIO.read(inputStream), "png", new File("D:\\p1.png"));
        IoUtil.close(inputStream);
    }
}
