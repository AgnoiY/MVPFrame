package com.mvpframe.ui;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
public class LoadResActivity extends AppCompatActivity implements View.OnClickListener {

    private EnabledViewpager viewPager;

    public static String GUIDE = "GUIDE";
    private boolean installFinishWelCome = false;
    private int[] ints = {R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load_res);
        initView();
        initData();
        initListeners();
    }

    @Override
    public void onClick(View v) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            startActivity(new Intent(this, MainActivity.class));
        installFinishWelCome = false;
        installFinishWelCome(1);
        finish();
        new Handler().postDelayed(() -> {
            System.exit(0);
        }, 100);

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
        for (int i : ints) {
            GuideFragment guide = new GuideFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(GUIDE, i);
            guide.setArguments(bundle);
            list.add(guide);
        }
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), list));
        viewPager.setCurrentItem(0);
    }

    /**
     * 增加按钮点击事件
     */
    private void initListeners() {

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

    /**
     * 欢迎页
     *
     * @param installFinishWelCome
     */
    private void installFinishWelCome(int installFinishWelCome) {
        ((MyApplication) getApplication()).installFinishWelCome(getApplication(), installFinishWelCome);
    }
}
