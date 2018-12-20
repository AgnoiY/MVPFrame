package com.mvpframe.capabilities.http.interfaces;

/**
 * 数据解析helper
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public interface ParseHelper<T> {
    /*解析数据*/
    T parse(String data);
}
