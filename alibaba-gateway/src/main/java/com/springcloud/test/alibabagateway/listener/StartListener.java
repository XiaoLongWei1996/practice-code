package com.springcloud.test.alibabagateway.listener;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author 肖龙威
 * @date 2022/11/08 13:49
 */
@Component
public class StartListener implements ApplicationListener<ApplicationStartedEvent> {

    // nacos server ip
    private static final String remoteAddress = "localhost:8848";
    // nacos server username
    private static final String userName = "admin";
    // nacos server password
    private static final String password = "123456";
    // nacos group
    private static final String groupId = "g1";
    // nacos dataId
    private static final String dataId = "gateway-test.json";
    // nacos namespace
    private static final String NACOS_NAMESPACE_ID = "7adfdfab-4bb0-46d3-a671-af0288719945";

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        dynamicRuleConfig();
    }

    //动态规则配置,引入nacos配置中心,改变nacos配置文件,sentinel规则也会改变
    private void dynamicRuleConfig() {
        //nacos主机信息
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, remoteAddress);
        properties.put(PropertyKeyConst.USERNAME, userName);
        properties.put(PropertyKeyConst.PASSWORD, password);
        properties.put(PropertyKeyConst.NAMESPACE, NACOS_NAMESPACE_ID);
        //配置nacos数据源
        ReadableDataSource<String, Set<GatewayFlowRule>> gateFlowRuleDataSource = new NacosDataSource<>(properties, groupId, dataId,
                source -> JSON.parseObject(source, new TypeReference<Set<GatewayFlowRule>>() {
                }));
        //加载数据源的规则数据
        GatewayRuleManager.register2Property(gateFlowRuleDataSource.getProperty());
    }

    //定义网关限流规则
    private void configRule() {
        //配置网关限流规则
        Set<GatewayFlowRule> set = new HashSet<>();
        //配置route id的限流方式
        GatewayFlowRule rule = new GatewayFlowRule();
        //设置api类型,可以匹配gateway的route,也可以匹配api组
        rule.setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID);
        //api名称,gateway中的route id或者api组名称
        rule.setResource("query1");
        //设置阈值类型:qps或者线程
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //设置阈值
        rule.setCount(3);
        //流控方式,快速失败
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        //统计窗口,默认单位秒
        rule.setIntervalSec(1);
        //突发流量时额外允许的请求数目
        rule.setBurst(0);
        //添加规则
        set.add(rule);

        //配置api组的限流方式
        GatewayFlowRule rule1 = new GatewayFlowRule();
        //设置api类型,可以匹配gateway的route,也可以匹配api组
        rule1.setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME);
        //api名称,gateway中的route id或者api组名称
        rule1.setResource("api1");
        //设置阈值类型:qps或者线程
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //设置阈值
        rule1.setCount(3);
        //流控方式,快速失败
        rule1.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        //统计窗口,默认单位秒
        rule1.setIntervalSec(1);
        //突发流量时额外允许的请求数目
        rule1.setBurst(0);
        set.add(rule1);
        //载入规则
        GatewayRuleManager.loadRules(set);
    }

    //定义api组
    private void configApiDefind() {
        //配置api定义
        Set<ApiDefinition> definitions = new HashSet<>();
        //api定义
        ApiDefinition apiDefinition = new ApiDefinition();
        //api组的名称
        apiDefinition.setApiName("api1");
        //api组
        Set<ApiPredicateItem> apiGroup = new HashSet<>();
        //组中的项
        ApiPathPredicateItem item1 = new ApiPathPredicateItem();
        //匹配路径
        item1.setPattern("/config/**");
        //匹配策略,路径前缀匹配
        item1.setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX);
        //添加项
        apiGroup.add(item1);
        //添加组
        apiDefinition.setPredicateItems(apiGroup);
        //添加api定义
        definitions.add(apiDefinition);
        //载入api定义
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}
