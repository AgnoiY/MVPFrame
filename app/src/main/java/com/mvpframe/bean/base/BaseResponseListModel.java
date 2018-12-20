package com.mvpframe.bean.base;

import java.io.Serializable;
import java.util.List;

/**
 * <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */

public class BaseResponseListModel<T> implements Serializable {

    /**
     * 数据对象/成功返回对象
     */
    private List<T> data;
    /**
     * 状态码
     */
    private int code;
    /**
     * 描述信息
     */
    private String msg;
    /**
     * 是否成功
     *
     * @return
     */
    private boolean success = false;

    public int getCode() {
        return code;
    }

    public BaseResponseListModel<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResponseListModel<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
