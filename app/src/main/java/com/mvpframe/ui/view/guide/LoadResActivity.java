package com.mvpframe.ui.view.guide;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.mvpframe.R;
import com.mvpframe.application.App;
import com.mvpframe.ui.MainActivity;
import com.mvpframe.util.DensityUtil;
import com.mvpframe.util.LogUtil;
import com.mvpframe.view.textview.ButtonTextView;

/**
 * <加载引导页，并分包加载class>
 * <p>
 * Data：2019/01/17
 *
 * @author yong
 */
public class LoadResActivity extends Activity implements View.OnClickListener {

    private ButtonTextView btvSkipMain;
    private LinearLayout llPage;
    /*保存ScrollView中的ViewGroup*/
    private LinearLayout childGroup = null;
    private GuideHorizontalScrollView scrollView;
    private Handler handler;
    public static String POSITION = "position";
    private boolean installFinishWelCome = false;
    public static int[] poscache = {R.mipmap.ic_launcher, R.mipmap.ic_launcher_round};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load_res);
        initData();
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        new DensityUtil();
        FrameLayout flayout = findViewById(R.id.flayout);
        FrameLayout flSkip = findViewById(R.id.fl_skip);
        llPage = findViewById(R.id.ll_page);
        scrollView = new GuideHorizontalScrollView(this);
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        scrollView.setHorizontalScrollBarEnabled(false);
        btvSkipMain = createView(0, 0);
        btvSkipMain.setText(getString(R.string.guide_skip));
        btvSkipMain.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        btvSkipMain.setStrokeColor(ContextCompat.getColor(this, R.color.colorPrimary));
        btvSkipMain.setPadding(DensityUtil.dip2px(30), DensityUtil.dip2px(10),
                DensityUtil.dip2px(30), DensityUtil.dip2px(10));
        btvSkipMain.setVisibility(View.GONE);
        btvSkipMain.setOnClickListener(this);
        flayout.addView(scrollView, 0);
        flSkip.addView(btvSkipMain);
        for (int i = 0; i < poscache.length; i++) {
            ButtonTextView textView = createView(DensityUtil.dip2px(10), DensityUtil.dip2px(10));
            textView.setSolid(true);
            textView.setCornerRadius(200);
            textView.setStrokeColor(ContextCompat.getColor(this, i == 0 ? R.color.colorPrimary : R.color.white));
            llPage.addView(textView);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        new LoadDexTask().execute();
    }

    @Override
    public void onClick(View v) {
        finishLoadRes();
    }

    class LoadDexTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                MultiDex.install(getApplication());
                LogUtil.d("install finish");
            } catch (Exception e) {
                LogUtil.w(e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            LogUtil.d("get install finish");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtil.d("back");
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
    }

    /**
     * 结束引导页进入主界面
     */
    private void finishLoadRes() {
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

    /**
     * 创建 ButtonTextView
     *
     * @param width
     * @param height
     * @return
     */
    private ButtonTextView createView(int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width > 0 ? width : ViewGroup.LayoutParams.WRAP_CONTENT,
                height > 0 ? height : ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = DensityUtil.dip2px(8);
        ButtonTextView textView = new ButtonTextView(this);
        textView.setLayoutParams(params);
        return textView;
    }

    /**
     * 设置页码
     *
     * @param context
     * @param pageNo
     */
    private void setPageColor(Context context, int pageNo) {
        for (int i = 0; i < poscache.length; i++) {
            ((ButtonTextView) llPage.getChildAt(i)).setStrokeColor(ContextCompat.getColor(context,
                    pageNo == i ? R.color.colorPrimary : R.color.white));
        }
    }

    public class GuideHorizontalScrollView extends HorizontalScrollView {
        private Context context;

        /*记录当前的页数标识*/
        private int pageNo = 0;

        /*触摸屏幕时X轴初始位置*/
        private int startpos;

        /*触摸屏幕后X轴移动位置*/
        private int moveSpeed;

        public GuideHorizontalScrollView(Context context) {
            super(context);
            this.context = context;
            init();
        }

        /*重写触摸事件，判断左右滑动*/
        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    startpos = (int) ev.getX();
                    LogUtil.d("startpos=" + startpos);
                }
                break;
                case MotionEvent.ACTION_MOVE: {
                    moveSpeed = (int) ev.getX();
                }
                break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {

                    /*先根据速度来判断向左或向右*/
                    int speed = releaseSpeedChange((int) ev.getX(), 80);
                    LogUtil.d("speed=" + speed);
                    if (speed > 0) {
                        nextPage();
                        return true;
                    }
                    if (speed < 0) {
                        prevPage();
                        return true;
                    }

                    /*这里是根据触摸起始和结束位置来判断向左或向右*/
                    LogUtil.d("ev.getX()-startpos=" + (ev.getX() - startpos));
                    if (Math.abs((ev.getX() - startpos)) > getWidth() / 3) {
                        if (ev.getX() - startpos > 0) {
                            /*向左*/
                            prevPage();
                        } else {
                            /*向右*/
                            nextPage();
                        }
                    } else {
                        LogUtil.d("ToPage:pageNo=" + pageNo);
                        /*不变*/
                        scrollToPage(pageNo > 0 ? 1 : 0);
                    }
                    return true;
                }
            }
            return super.onTouchEvent(ev);
        }

        /**
         * 初始化，加入而个子View
         */
        private void init() {
            childGroup = new LinearLayout(context);
            childGroup.setOrientation(LinearLayout.HORIZONTAL);
            addView(childGroup);
            addRight(createExampleView(pageNo));
            if (poscache.length > 1)
                addRight(createExampleView(pageNo + 1));
        }

        /**
         * 左翻页
         */
        public void prevPage() {
            if (pageNo != 0) {
                pageNo--;
                LogUtil.d("prevPage:pageNo=" + pageNo);
                LogUtil.d("prevPage:getChildCount=" + childGroup.getChildCount());
                scrollToPage(pageNo > 0 ? 1 : 0);
                if (pageNo > 0)
                    addLeft(createExampleView(pageNo - 1));
                if (pageNo != poscache.length - 2)
                    removeRight();
            }
        }

        /**
         * 右翻页
         */
        public void nextPage() {
            if (pageNo < poscache.length - 1) {
                pageNo++;
                LogUtil.d("nextPage:pageNo=" + pageNo);
                LogUtil.d("nextPage:getChildCount=" + childGroup.getChildCount());
                scrollToPage(1);
                if (pageNo < poscache.length - 1)
                    addRight(createExampleView(pageNo + 1));
                if (pageNo > 1)
                    removeLeft();
            }
        }


        /**
         * 获取子View的X坐标
         *
         * @param index
         * @return
         */
        private int getChildLeft(int index) {
            if (index >= 0 && childGroup != null) {
                if (index < childGroup.getChildCount())
                    return childGroup.getChildAt(index).getLeft();
            }
            return 0;
        }

        /**
         * 向右边添加View
         *
         * @param view 需要添加的View
         * @return true添加成功|false添加失败
         */
        public boolean addRight(View view) {
            if (view == null || childGroup == null) return false;
            childGroup.addView(view);
            return true;
        }

        /**
         * 删除右边的View
         *
         * @return true成功|false失败
         */
        public boolean removeRight() {
            if (childGroup == null || childGroup.getChildCount() <= 0) return false;
            childGroup.removeViewAt(childGroup.getChildCount() - 1);
            return true;
        }

        /**
         * 向左边添加View
         *
         * @param view 需要添加的View
         * @return true添加成功|false添加失败
         */
        public boolean addLeft(View view) {
            if (view == null || childGroup == null) return false;
            childGroup.addView(view, 0);

            /*因为在左边增加了View，因此所有View的x坐标都会增加，因此需要让ScrollView也跟着移动，才能从屏幕看来保持平滑。*/
            int tmpwidth = view.getLayoutParams().width;
            if (tmpwidth == 0) tmpwidth = getWinWidth();
            LogUtil.d("the new view's width = " + view.getLayoutParams().width);
            this.scrollTo(this.getScrollX() + tmpwidth, 0);

            return true;
        }

        /**
         * 删除左边的View
         *
         * @return true成功|false失败
         */
        public boolean removeLeft() {
            if (childGroup == null || childGroup.getChildCount() <= 0) return false;

            /*因为在左边删除了View，因此所有View的x坐标都会减少，因此需要让ScrollView也跟着移动。*/
            int tmpwidth = childGroup.getChildAt(0).getWidth();
            childGroup.removeViewAt(0);
            this.scrollTo((int) (this.getScrollX() - tmpwidth), 0);

            return true;
        }

        /**
         * 跳转到指定的页面
         *
         * @param index 跳转的页码
         * @return
         */
        public boolean scrollToPage(int index) {
            if (childGroup == null) return false;
            btvSkipMain.setVisibility(pageNo >= poscache.length - 1 ? View.VISIBLE : View.GONE);
            setPageColor(context, pageNo);
            if (index < 0 || index >= childGroup.getChildCount()) return false;
            smoothScrollTo(getChildLeft(index), 0);
            return true;
        }

        /**
         * 生成View
         *
         * @param index
         * @return
         */
        private View createExampleView(int index) {
            LogUtil.d("index=" + index);
            LayoutParams params = new LayoutParams(getWinWidth(), getWinHeight());
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(poscache[index]);
            return imageView;
        }

        private int releaseSpeedChange(int x, int limit) {
            /*检测到向左的速度很大*/
            if (moveSpeed - x > limit) return 1;
            /*检测到向右的速度很大*/
            if (x - moveSpeed > limit) return -1;

            return 0;
        }
    }

    /**
     * 获取屏幕信息
     *
     * @return
     */
    private int getWinWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕信息
     *
     * @return
     */
    private int getWinHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
