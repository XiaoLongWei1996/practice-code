package com.springcloud.test.alibabaconsumer.config.security;

import com.springcloud.test.alibabaconsumer.entity.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author 肖龙威
 * @date 2022/11/25 9:18
 */
@Setter
@Getter
public class LoginUser extends User {

    private Users users;

    public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Users users){
        super(username, password, authorities);
        this.users = users;
    }

    public LoginUser(String username, String password, boolean enabled, boolean accountNonExpired,
                     boolean credentialsNonExpired, boolean accountNonLocked,
                     Collection<? extends GrantedAuthority> authorities, Users users) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.users = users;
    }
}
