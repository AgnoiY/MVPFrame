package com.mvpframe.capabilities.http.exception;

/**
 * api接口错误/异常统一处理类
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class ApiException extends Exception {
    private final int code;//错误码
    private final String msg;//错误信息

    public ApiException(Throwable throwable, int code, String msg) {
        super(throwable);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
