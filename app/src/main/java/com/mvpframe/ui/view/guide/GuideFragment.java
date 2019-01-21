package com.mvpframe.ui.view.guide;


import android.view.View;

import com.mvpframe.R;
import com.mvpframe.databinding.FragmentGuideBinding;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.LoadResActivity;
import com.mvpframe.ui.base.fragment.BaseLazyFragment;

import static com.mvpframe.ui.LoadResActivity.POSITION;
import static com.mvpframe.ui.LoadResActivity.ints;

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
    public void onSuccess(String action, Object data) {

    }

    @Override
    public BasePresenter<IMvpView<Object>>[] getPresenterArray() {
        return new BasePresenter[0];
    }

    @Override
    public void initListeners() {
        mLazyBinding.btvSkipApp.setOnClickListener(this);
    }

    @Override
    public void lazyLoad() {
        position = mBundle.getInt(POSITION, -1);
        mLazyBinding.ivGuide.setBackgroundResource(ints[position]);
        mLazyBinding.btvSkipApp.setVisibility(position == ints.length - 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onInvisible() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_guide;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btv_skip_app:
                ((LoadResActivity) mActivity).finishLoadRes();
                break;
        }
    }


}
