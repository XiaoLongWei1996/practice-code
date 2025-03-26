package com.xlw.mapstruct_demo.entity;


import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author xlw
 * @description: 保存账号BO
 * @title: SaveZhxxBO
 * @package com.piaoshui.ncp.BO
 * @date 2025/3/12 16:35
 */
@Data
public class SaveZhxxBO {

    /**
     * 人员名称
     */
    @ExcelProperty(value = "人员名称", index = 0)
    private String mc;

    /**
     * 密码
     */
    @ExcelProperty(value = "用户密码", index = 1)
    private String password;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户账号", index = 2)
    private String yhm;

    /**
     * 企业id
     */
    @ExcelProperty(value = "所属企业", index = 3)
    private String qymc;

    /**
     * 权限
     */
    @ExcelProperty(value = "权限", index = 4)
    private String qx;

}
