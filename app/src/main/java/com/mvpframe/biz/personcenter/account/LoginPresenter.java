package com.mvpframe.biz.personcenter.account;

import android.content.Context;

import com.mvpframe.bean.home.LoginModel;
import com.mvpframe.biz.base.BasePresenter;
import com.mvpframe.biz.base.IMvpView;
import com.mvpframe.bridge.http.BaseModelObserver;
import com.mvpframe.constant.UrlConstans;

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

    private Context context;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    public void login(String userid, String pwd) {

        if (!isViewAttached()) return;

        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("pwd", pwd);

        getRetrofitHttp().post().apiUrl(UrlConstans.LOGIN)
                .addParameter(map).build()
                .request(new BaseModelObserver<LoginModel>(context) {//BaseListModelObserver:返回数据List集合

                    @Override
                    public void onSuccess(String action, LoginModel value) {
                        getMvpView().onSuccess(action, value);
                    }

                    @Override
                    public void onError(String action, int code, String desc) {//有需要需重写
                        super.onError(action, code, desc);
                        getMvpView().onError(action, code, desc);
                    }
                });

    }
}
