package xlw.test.satoken.config;

import lombok.Getter;

/**
 * @description:
 * @Title: AuthStatus
 * @Author xlw
 * @Package xlw.test.satoken.config
 * @Date 2024/3/23 16:48
 */
@Getter
public enum AuthStatus {

    UNAUTH(0, "未认证"),

    AUTH(1, "认证成功"),

    ENABLE(0, "账号已禁用"),

    DISABLE(1, "账号已启用"),

    UNLOCK(0, "账号已锁定"),

    LOCK(1, "账号已解锁");

    private Integer code;

    private String des;

    AuthStatus(Integer code, String des) {
        this.code = code;
        this.des = des;
    }
}
