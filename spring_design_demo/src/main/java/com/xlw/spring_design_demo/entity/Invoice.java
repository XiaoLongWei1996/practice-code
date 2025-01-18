package com.xlw.spring_design_demo.entity;


import com.xlw.spring_design_demo.state.invoice.InvoiceStateEnum;
import lombok.Data;

/**
 * @description:
 * @Title: Invoice
 * @Author xlw
 * @Package com.xlw.spring_design_demo.entity
 * @Date 2025/1/18 11:11
 */
@Data
public class Invoice {

    private String uuid;

    private InvoiceStateEnum state;
}
