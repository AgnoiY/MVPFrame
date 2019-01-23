package com.mvpframe.presenter.account;

import com.mvpframe.bean.home.LoginModel;
import com.mvpframe.bridge.http.BaseModelObserver;
import com.mvpframe.constant.UrlConstans;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;

import java.util.HashMap;
import java.util.Map;

/**
 * <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class LoginPresenter extends BasePresenter<IMvpView<LoginModel>> {

    public void login(String userid, String pwd) {

        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("pwd", pwd);

        getRetrofitHttp().post().apiUrl(UrlConstans.LOGIN)
                .addParameter(map).build()
                .request(new BaseModelObserver<LoginModel>(this));

    }

}
