package com.camelot.purchase.common.utils;

import com.camelot.purchase.common.vo.AjaxInfo;

/**
 * @author Admin
 * @date 2022/6/20
 */
public class ResultUtil {

    private ResultUtil() {
    }

    public static <T> AjaxInfo<T> reSuccess(T data) {
        return reSuccess(200, data);
    }

    public static <T> AjaxInfo<T> reSuccess(int code, T data) {
        AjaxInfo ajaxInfo = new AjaxInfo();
        ajaxInfo.setCode(code);
        ajaxInfo.setMsg("success");
        ajaxInfo.setData(data);
        return ajaxInfo;
    }

    public static <T> AjaxInfo reFailure(String message) {
        return reFailure(-1, message);
    }

    public static <T> AjaxInfo reFailure(int code, String message) {
        AjaxInfo ajaxInfo = new AjaxInfo<>();
        ajaxInfo.setCode(code);
        ajaxInfo.setMsg(message);
        return ajaxInfo;
    }
}
