package com.xlw.test.spring_security_demo.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xlw.test.spring_security_demo.config.Cache;
import com.xlw.test.spring_security_demo.config.MultiAuthenticationToken;
import com.xlw.test.spring_security_demo.entity.Result;
import com.xlw.test.spring_security_demo.entity.LoginUser;
import com.xlw.test.spring_security_demo.entity.UserInfo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @Title: SysController
 * @Author xlw
 * @Package com.xlw.test.spring_security_demo.controller
 * @Date 2024/9/20 17:15
 */
@RestController
@RequestMapping("sys")
public class SysController {

    @Resource
    private AuthenticationManager authenticationManager;

    @PostMapping("login")
    public Result<String> login(@RequestBody LoginUser user) {
        System.out.println("自定义登录接口" + JSONUtil.toJsonStr(user));
        //1.将前端传入的数据封装成功SpringSecurity的认证对象
        MultiAuthenticationToken authenticationToken = new MultiAuthenticationToken(user.getUserName(), user.getPassword(), null, 1);
        //2.手动调用认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate == null) {
            return Result.fail("登录失败");
        }
        //生成token
        String token = IdUtil.fastSimpleUUID();
        //保存到缓存中
        Cache.TOKEN_CACHE.put(token, (UserInfo) authenticate.getDetails());
        //返回token
        return Result.success(token);
    }

    @PostMapping("logout")
    public Result<Boolean> logout(HttpServletRequest request) {
        //判断是否有token
        String authToken = request.getHeader("AuthToken");
        if (StrUtil.isBlank(authToken)) {
            throw new RuntimeException("AuthToken为空");
        }
        //获取当前用户
        MultiAuthenticationToken authentication = (MultiAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Result.result(-1, false, "未登录");
        }
        //删除token缓存
        Cache.TOKEN_CACHE.remove(authToken);
        return Result.success(true);
    }

    @PostAuthorize("hasRole('ADMIN')")
    @GetMapping("test")
    public Result<String> test() {
        return Result.success("ok");
    }
}
