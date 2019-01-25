package com.mvpframe.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvpframe.R;
import com.mvpframe.databinding.DialogTipsBinding;
import com.mvpframe.util.Tools;

import java.lang.ref.SoftReference;

/**
 * 三个按钮或是两个按钮 Dialog
 * Data：2018/12/18
 *
 * @author yong
 */
@SuppressLint("ValidFragment")
public class CommonDialog extends DialogFragment implements View.OnClickListener {

    private Context mActivity;
    private DialogTipsBinding mBinding;

    private DialogInterface mInterface;
    private DialogInterface.Among mInterfaceAmong;

    public CommonDialog(Object o) {

        SoftReference soft = new SoftReference(o);
        Object object = soft.get();
        this.mActivity = (Activity) o;
        if (object instanceof DialogInterface)
            this.mInterface = (DialogInterface) object;
        else if (object instanceof DialogInterface.Among)
            this.mInterfaceAmong = (DialogInterface.Among) object;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return builder(inflater, container);

    }

    private View builder(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_tips, container, true);

//        ViewGroup.LayoutParams params = mBinding.dialogTipsLayout.getLayoutParams();
//        params.width = DensityUtil.getXScreenpx(mActivity) * 4 / 5;
//        mBinding.dialogTipsLayout.setLayoutParams(params);

        mBinding.dialogTipsNo.setOnClickListener(this);
        mBinding.dialogTipsAmong.setOnClickListener(this);
        mBinding.dialogTipsOk.setOnClickListener(this);

        mBinding.dialogTipsAmong.setVisibility(mInterfaceAmong != null ? View.VISIBLE : View.GONE);
        mBinding.view.setVisibility(mInterfaceAmong != null ? View.VISIBLE : View.GONE);

        return mBinding.getRoot();

    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public CommonDialog setTitleMsg(@Nullable String title) {
        if (mBinding.dialogTipsTitle != null && Tools.isNotNull(title)) {
            mBinding.dialogTipsTitle.setText(title);
        }
        return this;
    }

    /**
     * 设置内容
     *
     * @param msg
     * @return
     */
    public CommonDialog setContentMsg(String msg) {
        if (mBinding.dialogTipsContentMsg != null && Tools.isNotNull(msg)) {
            mBinding.dialogTipsContentMsg.setText(msg);
        }
        return this;
    }

    /**
     * 显示 Dialog
     *
     * @return
     */
//    public CommonDialog shows() {
//        if (!isShowing())
//            show();
//        return this;
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.dialog_tips_no:
                dismiss();
                break;
            case R.id.dialog_tips_among:
                if (mInterfaceAmong != null)
                    mInterfaceAmong.dialogTipsAmong();
                dismiss();
                break;
            case R.id.dialog_tips_ok:
                if (mInterfaceAmong != null)
                    mInterfaceAmong.dialogTipsOk();
                if (mInterface != null)
                    mInterface.dialogTipsOk();
                dismiss();
                break;
        }
    }

}
