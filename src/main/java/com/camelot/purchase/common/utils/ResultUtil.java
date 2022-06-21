package com.camelot.purchase.common.utils;

import com.camelot.purchase.common.vo.AjaxInfo;

/**
 * @author Admin
 * @date 2022/6/20
 */
public class ResultUtil {

    private ResultUtil(){}

    public static <T> AjaxInfo<T> reSuccess(T data){
        AjaxInfo ajaxInfo = new AjaxInfo();
        ajaxInfo.setCode(200);
        ajaxInfo.setMsg("success");
        ajaxInfo.setData(data);
        return ajaxInfo;
    }

    public static <T> AjaxInfo reFailure(String message){
        AjaxInfo ajaxInfo = new AjaxInfo<>();
        ajaxInfo.setCode(-1);
        ajaxInfo.setMsg(message);
        return ajaxInfo;
    }
}
