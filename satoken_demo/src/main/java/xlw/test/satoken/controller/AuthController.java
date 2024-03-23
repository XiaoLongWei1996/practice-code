package xlw.test.satoken.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xlw.test.common.dto.AuthDTO;
import xlw.test.satoken.config.AuthStatus;
import xlw.test.satoken.config.BusinessException;
import xlw.test.satoken.config.Result;
import xlw.test.satoken.entity.User;
import xlw.test.satoken.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @description: 认证
 * @Title: AuthController
 * @Author xlw
 * @Package xlw.test.satoken.config
 * @Date 2024/3/23 16:37
 */

@Api(tags = "认证")
@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

    @Resource
    private UserService userService;

    @PostMapping("login")
    public Result<String> login(String userName, String password, HttpServletRequest request) {
        log.info("login userName:{} password:{}", userName, password);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", userName);
        wrapper.eq("password", password);
        User user = userService.getOne(wrapper);
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException("用户名或密码错误");
        } else if (user.getDisable().equals(AuthStatus.DISABLE.getCode())) {
            throw new BusinessException("账号已被禁用");
        } else if (user.getLockAccont().equals(AuthStatus.LOCK.getCode())) {
            throw new BusinessException("账号已被锁定");
        }
        user.setIp(request.getRemoteHost());
        user.setStatus(AuthStatus.AUTH.getCode());
        user.setLastDt(new Date());
        userService.updateById(user);
        StpUtil.login(userName);
        AuthDTO authDTO = new AuthDTO();
        BeanUtil.copyProperties(user, authDTO, false);
        StpUtil.getSession().set("info", authDTO);
        return Result.success("登录成功");
    }

    @GetMapping("logout")
    public Result<String> logout() {
        log.info("logout");
        AuthDTO authDTO = (AuthDTO) StpUtil.getSession().get("info");
        User user = new User();
        user.setId(authDTO.getId());
        user.setStatus(AuthStatus.UNAUTH.getCode());
        userService.updateById(user);
        StpUtil.logout(user.getUserName());
        return Result.success("退出成功");
    }

    @PostMapping("register")
    public Result<String> register(User user) {
        log.info("注册用户");
        String password = user.getPassword();
        user.setPassword(SaSecureUtil.md5(password));
        userService.save(user);
        return Result.success("注册成功");
    }
}
