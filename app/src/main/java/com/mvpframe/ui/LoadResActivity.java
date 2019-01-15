package com.mvpframe.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mvpframe.R;
import com.mvpframe.app.MyApplication;
import com.mvpframe.util.LogUtil;

public class LoadResActivity extends Activity implements View.OnClickListener {

    private TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load_res);
        new LoadDexTask().execute();
        tv = findViewById(R.id.tv);
        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
        System.exit(0);
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
        ((MyApplication) getApplication()).installFinishWelCome(getApplication(),true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
    }
}
