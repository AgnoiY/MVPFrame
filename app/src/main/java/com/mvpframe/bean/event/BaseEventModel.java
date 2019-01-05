package com.mvpframe.bean.event;

import java.util.ArrayList;
import java.util.List;

/**
 * <事件类型>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */

public class BaseEventModel<T> {

    private T model;
    //添加要finish界面的ClassName
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public BaseEventModel<T> setList(List<String> list) {
        this.list = list;
        return this;
    }

    public BaseEventModel<T> add(Class c) {
        if (list == null) list = new ArrayList<>();
        list.add(c.getSimpleName());
        return this;
    }

    public T getModel() {
        return model;
    }

    public BaseEventModel<T> setModel(T model) {
        this.model = model;
        return this;
    }
}
