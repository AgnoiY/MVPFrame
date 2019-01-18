package com.mvpframe.ui.view.guide;


import com.mvpframe.R;
import com.mvpframe.databinding.FragmentGuideBinding;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.fragment.BaseLazyFragment;

import static com.mvpframe.ui.LoadResActivity.GUIDE;

/**
 * <引导页>
 * <p>
 * Data：2019/01/18
 *
 * @author yong
 */
public class GuideFragment extends BaseLazyFragment<Object, FragmentGuideBinding> {

    private int guide;

    @Override
    public void onSuccess(String action, Object data) {

    }

    @Override
    public BasePresenter<IMvpView<Object>>[] getPresenterArray() {
        return new BasePresenter[0];
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void lazyLoad() {
        guide = mBundle.getInt(GUIDE,-1);
    }

    @Override
    public void onInvisible() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_guide;
    }
}
