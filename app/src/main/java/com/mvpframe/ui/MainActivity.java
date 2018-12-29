package com.mvpframe.ui;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mvpframe.R;
import com.mvpframe.bean.home.LoginModel;
import com.mvpframe.databinding.ActivityMainBinding;
import com.mvpframe.presenter.account.LoginPresenter;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.activity.BaseLoadActivity;
import com.mvpframe.util.DensityUtil;

import java.util.concurrent.atomic.AtomicInteger;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * <功能详细描述>
 * 泛型传入
 * 1、网络请求实体类：如果有多个实体类可以传入Object或是通过BaseListMode中set、get方法设置
 * 2、自动生成ViewDataBinding
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class MainActivity extends BaseLoadActivity<LoginModel, ActivityMainBinding> {

    private LoginPresenter presenter = new LoginPresenter(this);

    private ConstraintLayout layout;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onSucceed(String action, LoginModel data) {
//        mLoadBinding.text.setText(SharedPrefManager.getUser().getString(SharedPrefUser.USER_NAME, ""));
//        mLoadBinding.text1.setText(data.getNickName());
//        mLoadBinding.bt.setText(action);
    }

    @Override
    public void initListeners() {
//        mLoadBinding.bt.setOnClickListener(this);
        mLoadBinding.ivAdd.setOnClickListener(this);
    }

    int height;//商品价格：添加View高度累加
//    int heightRetain;//保留两个商品价格View高度

    boolean isAdd = false;//是否点击下拉
    boolean isAddAllGoods = true;//展示全部商品价格
    int lsDetailSize = 2;

    @Override
    public void initData() {
//        presenter.login("15713802736", "123456");

        layout = findViewById(R.id.clayout);


        for (int i = 0; i < (lsDetailSize > 2 ? 2 : lsDetailSize); i++) {

            if (lsDetailSize == 1) {
                height = DensityUtil.dip2px(10);
                mLoadBinding.ivAdd.setVisibility(View.GONE);
            } else {
                mLoadBinding.ivAdd.setVisibility(View.VISIBLE);
            }

            layout = addView(layout, lsDetailSize, i);
        }

    }

    @Override
    public BasePresenter<IMvpView<LoginModel>>[] getPresenterArray() {
        return new BasePresenter[]{presenter};
    }

    ConstraintSet setReply = new ConstraintSet();

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (isAddAllGoods) {
            height = 0;
            for (int i = 0; i < 2; i++) {
                sNextGeneratedId = new AtomicInteger(i);
                layout.removeView(findViewById(generateViewId()));
            }
            for (int i = 0; i < lsDetailSize; i++) {
                layout = addView(layout, lsDetailSize, i);
            }
        }

        setAdd(setReply, lsDetailSize);
    }

    @Override
    public void onEmptyClickListener() {
        super.onEmptyClickListener();
//        presenter.login("15713802736", "123456");
    }


    /**
     * 添加商品价格
     *
     * @param layout
     * @param lsDetailSize
     * @param subPosition
     * @return
     */
    private ConstraintLayout addView(ConstraintLayout layout, int lsDetailSize, int subPosition) {

        ConstraintSet set = new ConstraintSet();

        sNextGeneratedId = new AtomicInteger(subPosition);

        View goodsPriceView = getLayoutInflater().inflate(R.layout.activity_text, null);
        goodsPriceView.getRootView().setId(generateViewId());

        ConstraintLayout cl = goodsPriceView.findViewById(goodsPriceView.getId());
        setMoneyColor((TextView) goodsPriceView.findViewById(R.id.tv_goods_y),
                (TextView) goodsPriceView.findViewById(R.id.tv_goods_price),
                true, lsDetailSize, subPosition);

        ImageView ivAddGoods = goodsPriceView.findViewById(R.id.iv_add_goods);
        LinearLayout llAddGoods = goodsPriceView.findViewById(R.id.ll_add_goods);
        ivAddGoods.setVisibility(lsDetailSize == 1 || (llAddGoods.getVisibility()==View.GONE&&isAdd )? View.VISIBLE : View.GONE);
        llAddGoods.setVisibility(View.GONE);

        ivAddGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivAddGoods.setVisibility(View.GONE);
                llAddGoods.setVisibility(View.VISIBLE);
            }
        });

        layout.addView(goodsPriceView);

        cl.getLayoutParams().width = DensityUtil.getXScreenpx((Activity) mContext) - (isAdd ? DensityUtil.dip2px(24) : DensityUtil.dip2px(113));

        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        cl.measure(0, h);

        set.clone(layout);

        set.connect(goodsPriceView.getId(), ConstraintSet.LEFT, isAdd ? R.id.iv_goods_image : R.id.tv_goods_name, ConstraintSet.LEFT);
        set.connect(goodsPriceView.getId(), ConstraintSet.TOP, isAdd ? R.id.iv_goods_image : R.id.tv_goods_name, ConstraintSet.BOTTOM, height);

        set.applyTo(layout);

        height += cl.getMeasuredHeight();

        if (subPosition == lsDetailSize - 1 && isAdd) {
            isAddAllGoods = false;
        }

        if (subPosition == 1) {
            setReply.clone(layout);
            isAdd = true;
//            heightRetain = height;
        }

        return layout;
    }

    /**
     * 显示商品添加下拉
     *
     * @param setReply
     * @param lsDetailSize
     */
    private void setAdd(ConstraintSet setReply, int lsDetailSize) {

        final ConstraintSet setAdd = new ConstraintSet();

        setAdd.clone(layout);

        if (isAdd) {
            setAdd.centerVertically(R.id.tv_goods_name, R.id.iv_goods_image);
            setAdd.centerVertically(R.id.iv_add, R.id.iv_goods_image);
            setAdd.setRotation(R.id.iv_add, 180);
            setAdd.setMargin(R.id.iv_goods_image, ConstraintSet.BOTTOM, DensityUtil.dip2px(7));
            setAdd.applyTo(layout);
        } else {
            for (int i = 0; i < 2; i++) {
                sNextGeneratedId = new AtomicInteger(i);
                int id = generateViewId();
                setReply.connect(id, ConstraintSet.LEFT, R.id.tv_goods_name, ConstraintSet.LEFT);
                setReply.connect(id, ConstraintSet.TOP, R.id.tv_goods_name, ConstraintSet.BOTTOM);
            }
            for (int i = 2; i < lsDetailSize; i++) {
                sNextGeneratedId = new AtomicInteger(i);
                layout.removeView(findViewById(generateViewId()));
            }
            isAddAllGoods = true;
            setReply.applyTo(layout);
        }
        isAdd = !isAdd;
    }


    /**
     * 根据商品状态设置价格的颜色
     */
    private void setMoneyColor(TextView textY, TextView textPrice, boolean isSoldOut, int lsDetailSize, int i) {
        if (i == 0)
            textPrice.setTextSize(20);
        else if (i == lsDetailSize - 1)
            textPrice.setTextSize(14);
        else
            textPrice.setTextSize(16);
        textY.setTextColor(isSoldOut ? ContextCompat.getColor(mContext, R.color.dialog_tips_negative_btn_txt_red) : ContextCompat.getColor(mContext, R.color.dialog_tips_title_txt));
        textPrice.setTextColor(isSoldOut ? ContextCompat.getColor(mContext, R.color.dialog_tips_negative_btn_txt_red) : ContextCompat.getColor(mContext, R.color.dialog_tips_title_txt));
    }

    private AtomicInteger sNextGeneratedId = null;

    public int generateViewId() {
        final int result = sNextGeneratedId.get();
        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
        int newValue = result + 1;
        if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
        if (sNextGeneratedId.compareAndSet(result, newValue)) {
            return result;
        }
        return 0;
    }
}
