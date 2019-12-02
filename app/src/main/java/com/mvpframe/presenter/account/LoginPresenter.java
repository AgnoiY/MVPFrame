package com.mvpframe.presenter.account;

import com.mvpframe.bean.account.ListModel;
import com.mvpframe.bean.account.LoginModel;
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
public class LoginPresenter extends BasePresenter<IMvpView> {

    public void login(String userid, String pwd) {

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", userid);
        map.put("password", pwd);

        getRetrofitHttp().post().apiUrl(UrlConstans.LOGIN)
                .addParameter(map).build()
                .request(new BaseModelObserver<LoginModel>(this,true) {
                });
    }


    /**
     * 短信记录列表搜索查询
     */
    public void getSmsRecord(boolean isRefresh, int pageNum, int pageSize) {

        Map<String, Object> map = new HashMap<>();
//        map.put("pageNo", pageNum);
//        map.put("pageSize", pageSize);
        map.put("userid", 12);

        getRetrofitHttp().post().apiUrl(UrlConstans.LIST1)
                .addParameter(map).build()
                .request(new BaseModelObserver<ListModel>(this, isRefresh) {
                });

    }

}
