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
