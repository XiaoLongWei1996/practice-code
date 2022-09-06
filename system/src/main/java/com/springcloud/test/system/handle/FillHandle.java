package com.springcloud.test.system.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 肖龙威
 * @date 2022/09/06 17:23
 */
@Component
public class FillHandle implements MetaObjectHandler {

    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"createDt", Date.class, new Date());
        this.strictInsertFill(metaObject,"updateDt", Date.class, new Date());
    }

    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateDt", Date.class, new Date());
    }
}
