package com.xlw.test.uploader_demo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description: 填充处理器
 * @Title: FillHandler
 * @Author xlw
 * @Package com.xlw.test.uploader_demo.config
 * @Date 2024/2/24 15:22
 */
@Component
public class FillHandler implements MetaObjectHandler {

    /**
     * 插入填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createDt", Date.class, new Date());

        this.strictInsertFill(metaObject, "updateDt", Date.class, new Date());
    }

    /**
     * 更新填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateDt", Date.class, new Date());
    }
}
