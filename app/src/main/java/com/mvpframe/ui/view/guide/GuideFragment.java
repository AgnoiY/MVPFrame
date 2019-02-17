package com.mvpframe.ui.view.guide;


import com.mvpframe.R;
import com.mvpframe.databinding.FragmentGuideBinding;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.fragment.BaseLazyFragment;

import static com.mvpframe.ui.LoadResActivity.POSITION;
import static com.mvpframe.ui.LoadResActivity.poscache;

/**
 * <引导页>
 * <p>
 * Data：2019/01/18
 *
 * @author yong
 */
public class GuideFragment extends BaseLazyFragment<Object, FragmentGuideBinding> {

    private int position;//引导页索引

    @Override
    public BasePresenter<IMvpView<Object>>[] getPresenterArray() {
        return new BasePresenter[0];
    }

    @Override
    public void initListeners() {
    }

    @Override
    public void lazyLoad() {

        getBundle();
    }

    @Override
    public void onInvisible() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_guide;
    }

    /**
     * 获取传递的Bundle
     */
    private void getBundle() {
        position = mBundle.getInt(POSITION, -1);
        mLazyBinding.ivGuide.setBackgroundResource(poscache[position]);
    }
}
