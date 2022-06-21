package com.camelot.purchase.common.exception;

import com.camelot.purchase.common.utils.ResultUtil;
import com.camelot.purchase.common.vo.AjaxInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Admin
 * @date 2022/6/21
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BusinessException.class)
    public AjaxInfo onBusinessException(HttpServletRequest request,BusinessException be){
        log.error("自定义异常捕获",be);
        int errCode = be.getCode() == null ? -1 : be.getCode();
        return ResultUtil.reFailure(errCode, be.getMessage());
    }

}
