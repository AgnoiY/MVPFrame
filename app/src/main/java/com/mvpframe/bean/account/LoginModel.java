package com.mvpframe.bean.account;

import com.mvpframe.bean.base.BaseResponseModel;

/**
 * <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class LoginModel extends BaseResponseModel<LoginModel> {

    /**
     * id : 1
     * userid : 15713802736
     * pwd : 123456
     * name : 创利丰
     * nickName : 测试
     * birthday : null
     * address : null
     * time : null
     * tongxunlu : null
     */

    private String id;
    private String userid;
    private String pwd;
    private String name;
    private String nickName;
    private Object birthday;
    private Object address;
    private Object time;
    private Object tongxunlu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public Object getTongxunlu() {
        return tongxunlu;
    }

    public void setTongxunlu(Object tongxunlu) {
        this.tongxunlu = tongxunlu;
    }

    /**
     * userId : 51
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJ1c2VyX2lkIjoiNTEiLCJpc3MiOiJTZXJ2aWNlIiwiZXhwIjoxNTU1OTk1NzY4LCJpYXQiOjE1NDgyMTk3Njh9.R3vVgsugepFWyTd06Pms3pnBU_1qck9WbP8JqCM6N-c
     */

    private int userId;
    private String token;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
