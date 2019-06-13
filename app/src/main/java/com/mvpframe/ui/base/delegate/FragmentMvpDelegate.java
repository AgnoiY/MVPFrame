package com.mvpframe.ui.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Fragment生命周期
 *
 * @author yong
 * @data Data：2018/12/18
 */
public interface FragmentMvpDelegate {

    /**
     * Must be called from {@link Fragment#onCreate(Bundle)}
     *
     * @param saved The bundle
     */
    void onCreate(Bundle saved);

    /**
     * Must be called from {@link Fragment#onDestroy()}
     */
    void onDestroy();

    /**
     * Must be called from {@link Fragment#onViewCreated(View, Bundle)}
     *
     * @param view               The inflated view
     * @param savedInstanceState the bundle
     */
    void onViewCreated(View view, @Nullable Bundle savedInstanceState);

    /**
     * Must be called from {@link Fragment#onDestroyView()}
     */
    void onDestroyView();

    /**
     * Must be called from {@link Fragment#onPause()}
     */
    void onPause();

    /**
     * Must be called from {@link Fragment#onResume()}
     */
    void onResume();

    /**
     * Must be called from {@link Fragment#onStart()}
     */
    void onStart();

    /**
     * Must be called from {@link Fragment#onStop()}
     */
    void onStop();

    /**
     * Must be called from {@link Fragment#onActivityCreated(Bundle)}
     *
     * @param savedInstanceState The saved bundle
     */
    void onActivityCreated(Bundle savedInstanceState);

    /**
     * Must be called from {@link Fragment#onAttach(Activity)}
     *
     * @param activity The activity the fragment is attached to
     */
    void onAttach(Activity activity);

    /**
     * Must be called from {@link Fragment#onDetach()}
     */
    void onDetach();

    /**
     * Must be called from {@link Fragment#onSaveInstanceState(Bundle)}
     */
    void onSaveInstanceState(Bundle outState);
}
