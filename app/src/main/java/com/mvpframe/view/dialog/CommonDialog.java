package com.mvpframe.view.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.mvpframe.R;
import com.mvpframe.databinding.DialogTipsBinding;
import com.mvpframe.util.DensityUtil;
import com.mvpframe.util.Tools;

import java.lang.ref.SoftReference;

/**
 * Dialog
 * <p>
 * Data：2019/01/28
 *
 * @author yong
 */
public class CommonDialog extends BaseFragmentDialog<DialogTipsBinding> implements View.OnClickListener {

    private String TAG = "CommonDialog";
    private BaseDialogClickListenter mInterface;
    private BaseDialogClickListenter.Among mInterfaceAmong;

    private String title, contentMsg, noMsg, amongMsg, okMsg;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_tips;
    }

    @Override
    public void initData() {
        ViewGroup.LayoutParams params = mDialogBing.dialogTipsContentMsg.getLayoutParams();
        params.width = DensityUtil.getXScreenpx(getActivity()) * 4 / 5;
        mDialogBing.dialogTipsContentMsg.setLayoutParams(params);

        mDialogBing.dialogTipsAmong.setVisibility(mInterfaceAmong != null && Tools.isNotNull(amongMsg) ? View.VISIBLE : View.GONE);
        mDialogBing.view.setVisibility(mInterfaceAmong != null && Tools.isNotNull(amongMsg) ? View.VISIBLE : View.GONE);

        mDialogBing.dialogTipsTitle.setVisibility(Tools.isNotNull(title) ? View.VISIBLE : View.GONE);

        mDialogBing.dialogTipsTitle.setText(title);
        mDialogBing.dialogTipsContentMsg.setText(contentMsg);
        mDialogBing.dialogTipsAmong.setText(amongMsg);
        if (Tools.isNotNull(okMsg))
            mDialogBing.dialogTipsOk.setText(okMsg);
        if (Tools.isNotNull(noMsg))
            mDialogBing.dialogTipsNo.setText(noMsg);
    }

    @Override
    public void initListeners() {
        mDialogBing.dialogTipsNo.setOnClickListener(this);
        mDialogBing.dialogTipsOk.setOnClickListener(this);
        mDialogBing.dialogTipsAmong.setOnClickListener(this);
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public CommonDialog setTitleMsg(@Nullable String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置内容
     *
     * @param contentMsg
     * @return
     */
    public CommonDialog setContentMsg(@NonNull String contentMsg) {
        this.contentMsg = contentMsg;
        return this;
    }

    /**
     * 设置取消按钮
     *
     * @param noMsg
     * @return
     */
    public CommonDialog setButtonNo(@NonNull String noMsg) {
        if (Tools.isNotNull(noMsg))
            this.noMsg = noMsg;
        return this;
    }

    /**
     * 设置中间按钮
     *
     * @param amongMsg
     * @return
     */
    public CommonDialog setButtonAmong(@NonNull String amongMsg) {
        this.amongMsg = amongMsg;
        return this;
    }

    /**
     * 设置确定按钮
     *
     * @param okMsg
     * @return
     */
    public CommonDialog setButtonOk(@NonNull String okMsg) {
        if (Tools.isNotNull(okMsg))
            this.okMsg = okMsg;
        return this;
    }

    /**
     * 两个按钮
     *
     * @param mInterface
     * @return
     */
    public CommonDialog setClickListenter(BaseDialogClickListenter mInterface) {
        this.mInterface = mInterface;
        return this;
    }

    /**
     * 三个按钮
     *
     * @param mInterfaceAmong
     * @return
     */
    public CommonDialog setClickListenterAmong(BaseDialogClickListenter.Among mInterfaceAmong) {
        this.mInterfaceAmong = mInterfaceAmong;
        return this;
    }

    /**
     * 显示Dialog
     *
     * @param o
     * @return
     */
    public CommonDialog shows(Object o) {
        SoftReference soft = new SoftReference(o);
        Object ob = soft.get();
        FragmentActivity activity = (FragmentActivity) ob;

        if (ob instanceof BaseDialogClickListenter.Among)
            this.mInterfaceAmong = (BaseDialogClickListenter.Among) ob;
        else if (ob instanceof BaseDialogClickListenter)
            this.mInterface = (BaseDialogClickListenter) ob;
        show(activity.getSupportFragmentManager(), TAG);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_tips_no:
                dismiss();
                break;
            case R.id.dialog_tips_among:
                dismiss();
                if (mInterfaceAmong != null)
                    mInterfaceAmong.dialogTipsAmong();
                break;
            case R.id.dialog_tips_ok:
                dismiss();
                if (mInterfaceAmong != null)
                    mInterfaceAmong.dialogTipsOk();
                if (mInterface != null)
                    mInterface.dialogTipsOk();
                break;
        }
    }
}
