package com.springcloud.test.alibabaconsumer.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.springcloud.test.alibabaconsumer.config.security.LoginUser;
import com.springcloud.test.alibabaconsumer.util.SendUtil;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author 肖龙威
 * @date 2022/10/31 15:12
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private SendUtil sendUtil;

    private Executor pool = Executors.newFixedThreadPool(10);

    @PostConstruct
    public void init() {
        //定义熔断规则
        List<DegradeRule> rules = new ArrayList<>();
        //熔断规则
        DegradeRule rule = new DegradeRule();
        //设置资源名称
        rule.setResource("hello");
        //设置最小的请求数
        rule.setMinRequestAmount(5);
        //设置慢查询比率阈值
        rule.setSlowRatioThreshold(0.5);
        //设置时间窗口,熔断时长
        rule.setTimeWindow(5);
        //设置运行时间(毫秒)
        rule.setCount(1000);
        //设置统计时间间隔
        rule.setStatIntervalMs(1000);
        //设置熔断策略
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        //添加规则
        rules.add(rule);
        //载入规则
        DegradeRuleManager.loadRules(rules);

        //控流规则
        List<FlowRule> rules1 = new ArrayList<>();
        //直接控流,qps达到指定数直接拒绝访问
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("query");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setCount(5);
        rules1.add(flowRule);
        //关联控流,当关联资源(write)达到指定阈值,则该资源(read)被限流
        flowRule = new FlowRule();
        flowRule.setResource("read");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setStrategy(RuleConstant.STRATEGY_RELATE);
        flowRule.setRefResource("write");
        flowRule.setCount(5);
        rules1.add(flowRule);
        //链路控流,从入口资源访问该资源超过阈值,则会被限流
        flowRule = new FlowRule();
        flowRule.setResource("recieve");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setStrategy(RuleConstant.STRATEGY_CHAIN);
        flowRule.setRefResource("send");
        flowRule.setCount(5);
        rules1.add(flowRule);

        //直接控流,qps达到指定数直接拒绝访问
        flowRule = new FlowRule();
        flowRule.setResource("warm");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setCount(5);
        //预热模式,qps的值开始从5/3经过10秒后到达5
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
        //预热时间
        flowRule.setWarmUpPeriodSec(10);
        rules1.add(flowRule);

        //直接控流,qps达到指定数直接拒绝访问
        flowRule = new FlowRule();
        flowRule.setResource("wait");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setCount(5);
        //排队等待模式,只能用到QPS,给每个线程设置一个等待时间,如果在等待时间内能被执行则正确返回,否则报错
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        //设置等待时间
        flowRule.setMaxQueueingTimeMs(1000);
        rules1.add(flowRule);
        FlowRuleManager.loadRules(rules1);

        //热点参数规则
        List<ParamFlowRule> paramFlowRules = new ArrayList<>();
        ParamFlowRule paramFlowRule = new ParamFlowRule();
        //设置资源名称
        paramFlowRule.setResource("myparam");
        //设置限流阈值
        paramFlowRule.setCount(1);
        //设置热点参数的索引0表示第一个参数
        paramFlowRule.setParamIdx(0);
        //参数流控制详细项,对参数的某些具体值进行限流
        ParamFlowItem item = new ParamFlowItem();
        //输入参数值的类型名称
        item.setClassType(Integer.class.getTypeName());
        //参数的值
        item.setObject(String.valueOf(1));
        //设置限流阈值
        item.setCount(10);
        paramFlowRule.setParamFlowItemList(Collections.singletonList(item));
        paramFlowRules.add(paramFlowRule);
        ParamFlowRuleManager.loadRules(paramFlowRules);

        //系统保护,监控当前主机的资源使用情况对该系统(不是资源)进行限流
        List<SystemRule> systemRules = new ArrayList<>();
        SystemRule systemRule = new SystemRule();
        //设置系统允许最大的线程数
        systemRule.setMaxThread(1);
        systemRules.add(systemRule);
        SystemRuleManager.loadRules(systemRules);

        //授权规则,对访问的资源设置黑名单或者白名单,阻拦或者允许被限制的app,需要实现RequestOriginParser来解析origin
        List<AuthorityRule> authorityRules = new ArrayList<>();
        AuthorityRule authorityRule = new AuthorityRule();
        //资源名称
        authorityRule.setResource("read1");
        //策略白名单或者黑名单
        authorityRule.setStrategy(RuleConstant.AUTHORITY_WHITE);
        //被限制的app,也就是请求头的origin,多个用逗号隔开
        authorityRule.setLimitApp("app1");
        authorityRules.add(authorityRule);
        AuthorityRuleManager.loadRules(authorityRules);
    }

    @SentinelResource("query")
    @GetMapping("query")
    public ResponseEntity<String> test() {
        String result = restTemplate.getForObject("http://config/config/test", String.class);
        return ResponseEntity.ok(result);
    }

    @GetMapping("query1")
    public ResponseEntity<String> test1() {
        Entry entry = null;
        try {
            entry = SphU.entry("hello");
            Thread.sleep(3000);
            System.out.println("hello");
        }  catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BlockException e) {
            System.out.println("服务降级");
            e.printStackTrace();
        } finally {
            if (ObjectUtil.isNotEmpty(entry)) {
                entry.exit();
            }
            ContextUtil.exit();
        }
        return ResponseEntity.ok("result");
    }

    @SentinelResource(value = "read1", fallback = "error", entryType = EntryType.IN)
    @GetMapping("read")
    public String read(){
//        System.out.println(origin);
        return "读操作";
    }

    @SentinelResource(value = "write", fallback = "error")
    @GetMapping("write")
    public String write() {

        return "写操作";
    }

    @SentinelResource(value = "send", fallback = "error")
    @GetMapping("send")
    public String send() {
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                System.out.println(sendUtil.recieve());
            });
        }
        return "发送";
    }

    @SentinelResource(value = "send1", fallback = "error")
    @GetMapping("send1")
    public String send1() {
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                System.out.println(sendUtil.recieve());
            });
        }
        return "发送";
    }

    @SentinelResource(value = "warm", fallback = "error")
    @GetMapping("warm")
    public String warm() {

        return "预热";
    }

    @SentinelResource(value = "wait", fallback = "error")
    @GetMapping("wait")
    public String wait1() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser p = (LoginUser) authentication.getPrincipal();
        System.out.println(p.getUsers());
        return "等待";
    }

    @SentinelResource(value = "myparam", fallback = "error")
    @GetMapping("param")
    public String param(@RequestParam(value = "a", required = false) Integer a) {
        System.out.println(a);
        return "参数";
    }

    @GetMapping("/loginInfo")
    public String loginInfo() {
        return "请登录";
    }

    public String error(BlockException e) throws BlockException {
        e.printStackTrace();
        throw new AuthorityException("服务器繁忙");
    }
}
