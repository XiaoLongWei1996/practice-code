package com.xlw.test.flowable_demo.listener;

import cn.hutool.json.JSONUtil;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.HistoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description: 报销执行监听器
 * @Title: BxExecutionListener
 * @Author xlw
 * @Package com.xlw.test.flowable_demo.listener
 * @Date 2024/10/3 13:10
 */
@Component
public class BxExecutionListener implements ExecutionListener {

    @Resource
    private HistoryService historyService;

    @Override
    public void notify(DelegateExecution execution) {
        FlowElement currentFlowElement = execution.getCurrentFlowElement();
        System.out.println(currentFlowElement.getName());
        Map<String, Object> variables = execution.getVariables();
        System.out.println(JSONUtil.toJsonStr(variables));
        System.out.println(execution.getEventName());
        String processInstanceBusinessKey = execution.getProcessInstanceBusinessKey();
        System.out.println(processInstanceBusinessKey);
    }
}
