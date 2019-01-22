package com.mvpframe.ui.view.guide;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvpframe.R;
import com.mvpframe.util.LogUtil;

import static com.mvpframe.ui.LoadResActivity.POSITION;
import static com.mvpframe.ui.LoadResActivity.poscache;

/**
 * <引导页>
 * <p>
 * Data：2019/01/18
 *
 * @author yong
 */
//public class GuideFragment extends BaseLazyFragment<Object, FragmentGuideBinding> {
public class GuideFragment extends Fragment {

    private static final String TAG = "GuideFragment";

    private Bundle mBundle;
    private int position;//引导页索引

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle(view);
    }

    /**
     * 获取传递的Bundle
     */
    private void getBundle(View view) {
        if (getArguments() != null)
            mBundle = getArguments();
        else
            LogUtil.e(TAG + ":" + mBundle);

        initData(view);
    }

    private void initData(View view) {
        position = mBundle.getInt(POSITION, -1);
        view.findViewById(R.id.iv_guide).setBackgroundResource(poscache[position]);
    }

    //    @Override
//    public void onSuccess(String action, Object data) {
//
//    }
//
//    @Override
//    public BasePresenter<IMvpView<Object>>[] getPresenterArray() {
//        return new BasePresenter[0];
//    }
//
//    @Override
//    public void initListeners() {
//    }
//
//    @Override
//    public void lazyLoad() {
//        position = mBundle.getInt(POSITION, -1);
//        mLazyBinding.ivGuide.setBackgroundResource(ints[position]);
//    }
//
//    @Override
//    public void onInvisible() {
//
//    }
//
//    @Override
//    public int getLayout() {
//        return R.layout.fragment_guide;
//    }

}
