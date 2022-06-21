package com.camelot.purchase.common.vo;

import lombok.Data;

/**
 * @author Admin
 * @date 2022/6/20
 */
@Data
public class AjaxInfo<T> {
    private int code;
    private String msg;
    private T data;

}
