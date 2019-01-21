package com.mvpframe.ui;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.mvpframe.R;
import com.mvpframe.adapters.ViewPagerAdapter;
import com.mvpframe.app.MyApplication;
import com.mvpframe.ui.view.guide.GuideFragment;
import com.mvpframe.util.LogUtil;
import com.mvpframe.view.viewpager.EnabledViewpager;

import java.util.ArrayList;
import java.util.List;

/**
 * <加载引导页，并分包加载class>
 * <p>
 * Data：2019/01/17
 *
 * @author yong
 */
public class LoadResActivity extends AppCompatActivity {

    private EnabledViewpager viewPager;

    private Handler handler;
    public static String POSITION = "position";
    private boolean installFinishWelCome = false;
    public static int[] ints = {R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load_res);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewPager = findViewById(R.id.view_pager);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        new LoadDexTask().execute();
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < ints.length; i++) {
            GuideFragment guide = new GuideFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(POSITION, i);
            guide.setArguments(bundle);
            list.add(guide);
        }
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), list));
        viewPager.setCurrentItem(0);
    }

    class LoadDexTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                MultiDex.install(getApplication());
                LogUtil.e("install finish");
                ((MyApplication) getApplication()).installFinish(getApplication());
            } catch (Exception e) {
                LogUtil.e(e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            LogUtil.e("get install finish");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtil.e("back");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            installFinishWelCome = true;
            installFinishWelCome(2);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        installFinishWelCome(installFinishWelCome ? 2 : 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    /**
     * 欢迎页
     *
     * @param installFinishWelCome
     */
    private void installFinishWelCome(int installFinishWelCome) {
        ((MyApplication) getApplication()).installFinishWelCome(getApplication(), installFinishWelCome);
    }

    /**
     * 结束引导页进入主界面
     */
    public void finishLoadRes() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            startActivity(new Intent(this, MainActivity.class));
        installFinishWelCome = false;
        installFinishWelCome(1);
        finish();
        handler = new Handler();
        handler.postDelayed(() -> {
            System.exit(0);
        }, 100);
    }
}
