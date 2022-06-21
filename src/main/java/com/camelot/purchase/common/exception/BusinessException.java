package com.camelot.purchase.common.exception;

/**
 * @author Admin
 * @date 2022/6/21
 */
public class BusinessException extends RuntimeException {

    private Integer code;
    private String messageFormat;
    private Object[] args;

    public BusinessException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
        this.messageFormat = messageFormat;
        this.args = args;
    }

    public BusinessException(String messageFormat) {
        super(messageFormat);
        this.messageFormat = messageFormat;
    }

    public BusinessException(Integer code, String messageFormat) {
        this(messageFormat);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }


    public Object[] getArgs() {
        return args;
    }


    public static void main(String[] args) {
        BusinessException businessException = new BusinessException("%s是傻逼", "小明");
        System.out.println(businessException.getMessage());
        System.out.println(businessException.getCode());

        BusinessException exception = new BusinessException(200, "测试异常");
        System.out.println(exception.getMessage());
        System.out.println(exception.getCode());
    }
}
