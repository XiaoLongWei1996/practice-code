package xlw.test.satoken.config;

import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @Title: StpInterfaceImpl
 * @Author xlw
 * @Package xlw.test.satoken.config
 * @Date 2024/3/23 18:39
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {


    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("添加权限" + loginId);
        List<String> list = new ArrayList<>();
        list.add("h1");
        list.add("h2");
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        log.info("添加角色" + loginId);
        List<String> list = new ArrayList<>();
        list.add("r1");
        list.add("r2");
        return list;
    }
}
