package com.mvpframe.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.mvpframe.R;
import com.mvpframe.databinding.DialogBaseCommonBinding;
import com.mvpframe.ui.base.interfaces.BaseInterface;
import com.mvpframe.util.ToastUtil;
import com.mvpframe.util.Tools;

/**
 * 提示框
 * <p>
 * Data：2019/06/21
 *
 * @author yong
 */
public class CommonDialog<B extends ViewDataBinding> implements BaseInterface, View.OnClickListener {

    private Context mContext;
    private View mViewDialog;
    private View addVIew;
    private DialogBaseCommonBinding mCommonBing;
    private AlertDialog mAlertDialog;
    private AlertDialog.Builder mAlertBuilder;
    private BaseDialogClickListenter mInterface;
    private boolean isEditVisibility; // 默认布局中的输入框是否显示
    private boolean isOkDismiss;
    private String[] mEditContent;
    private String[] mEditToast;

    public CommonDialog(Context mContext) {
        this(mContext, 0);
    }

    public CommonDialog(Context mContext, int layoutId) {
        this(mContext, layoutId, true);
    }

    /**
     * 提示框
     *
     * @param context
     * @param layoutId  改变布局
     * @param isAddView 　添加View  true：内容部分添加View　false:重新绘制新布局
     */
    public CommonDialog(Context context, int layoutId, boolean isAddView) {

        boolean isLayoutId = layoutId != 0;

        this.mContext = context;

        mAlertBuilder = new AlertDialog.Builder(context);

        ViewDataBinding mDialogBing = DataBindingUtil.inflate(LayoutInflater.from(context), isLayoutId && !isAddView ? layoutId : getLayoutId(), null, false);

        mViewDialog = mDialogBing.getRoot();

        mAlertBuilder.setView(mViewDialog);

        if (!isLayoutId || isAddView) {
            initData();
            initListeners();
        }

        if (isLayoutId && isAddView) {
            addVIew = addMainView(layoutId);
        }
        if (isAddView && addVIew != null) {
            mCommonBing.dialogCommonAddView.setVisibility(View.VISIBLE);
            mCommonBing.dialogCommonAddView.addView(addVIew);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_base_common;
    }

    @Override
    public void initData() {
        mCommonBing = DataBindingUtil.getBinding(mViewDialog);
    }

    @Override
    public void initListeners() {
        mCommonBing.dialogCommonNo.setOnClickListener(this);
        mCommonBing.dialogCommonOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_common_no:
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();
                }
                break;
            case R.id.dialog_common_ok:

                if (isEditContentNull()) {
                    return;
                }

                if (mInterface != null) {
                    mInterface.dialogComonOk(mEditContent);
                }
                if (mAlertDialog != null && isOkDismiss) {
                    mAlertDialog.dismiss();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 布局中输入框内容是否为空
     *
     * @return
     */
    private boolean isEditContentNull() {
        String dialogNotNull = mContext.getString(R.string.dialog_not_null);
        if (isEditVisibility) {
            if (mEditContent == null) {
                mEditContent = new String[1];
            }
            if (mEditToast == null) {
                mEditToast = new String[1];
            }
            mEditContent[mEditContent.length - 1] = mCommonBing.dialogCommonContentEt.getText().toString();
            mEditToast[mEditToast.length - 1] = dialogNotNull;
        }

        if (mEditContent == null) {
            return false;
        }

        for (int i = 0; i < mEditContent.length; i++) {
            if (TextUtils.isEmpty(mEditContent[i]) && mEditContent.length <= mEditToast.length) {
                ToastUtil.makeCenterToast(mContext, TextUtils.isEmpty(mEditToast[i]) ? dialogNotNull : mEditToast[i]);
                return true;
            }
        }
        return false;
    }

    /**
     * 设置按钮监听事件
     *
     * @param mInterface
     * @return
     */
    public CommonDialog setClickListenter(BaseDialogClickListenter mInterface) {
        this.mInterface = mInterface;
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public CommonDialog setTitle(Object title) {

        setTitleVisibility(Tools.isNotNull(title));

        if (title instanceof String) {
            mCommonBing.dialogCommonTitle.setText((String) title);
        } else if (title instanceof Integer) {
            mCommonBing.dialogCommonTitle.setText((Integer) title);
        } else {
            mCommonBing.dialogCommonTitle.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 显示标题
     *
     * @param visibility
     * @return
     */
    public CommonDialog setTitleVisibility(boolean visibility) {
        mCommonBing.dialogCommonTitle.setVisibility(visibility ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置内容
     *
     * @param content
     * @return
     */
    public CommonDialog setContent(Object content) {

        setContentVisibility(Tools.isNotNull(content));

        if (content instanceof String) {
            mCommonBing.dialogCommonContentTv.setText((String) content);
        } else if (content instanceof Integer) {
            mCommonBing.dialogCommonContentTv.setText((Integer) content);
        } else {
            mCommonBing.dialogCommonContentTv.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 显示内容
     *
     * @param visibility
     * @return
     */
    public CommonDialog setContentVisibility(boolean visibility) {
        mCommonBing.dialogCommonContentTv.setVisibility(visibility ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置输入内容
     *
     * @param content
     * @return
     */
    public CommonDialog setEditHint(Object content) {

        setEditVisibility(Tools.isNotNull(content));

        if (content instanceof String) {
            mCommonBing.dialogCommonContentEt.setHint((String) content);
        } else if (content instanceof Integer) {
            mCommonBing.dialogCommonContentEt.setHint((Integer) content);
        } else {
            mCommonBing.dialogCommonContentEt.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置输入内容长度
     *
     * @param maxLength
     * @return
     */
    public CommonDialog setEditMaxLength(int maxLength) {

        setEditVisibility(Tools.isNotNullOrZero(maxLength));

        mCommonBing.dialogCommonContentEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        return this;
    }

    /**
     * 显示输入内容
     *
     * @param visibility
     * @return
     */
    public CommonDialog setEditVisibility(boolean visibility) {
        this.isEditVisibility = visibility;
        mCommonBing.dialogCommonContentEt.setVisibility(visibility ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 添加View中的输入框内容
     *
     * @param editTexts
     * @return
     */
    public CommonDialog addEditTexts(EditText... editTexts) {
        if (editTexts == null || editTexts.length == 0) {
            return this;
        }
        if (this.mEditContent == null) {
            this.mEditContent = new String[editTexts.length + (isEditVisibility ? 1 : 0)];
        }
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].addTextChangedListener(new TextWatchers(i));
        }
        return this;
    }

    /**
     * 动态添加EditText监听
     */
    private class TextWatchers implements TextWatcher {
        private int position;

        public TextWatchers(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // 输入之前
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //　输入过程
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mEditContent[position] = editable.toString();
        }
    }

    /**
     * 添加View中的输入框为空Toast提示
     *
     * @param editToast
     * @return
     */
    public CommonDialog addEditToast(String... editToast) {
        if (editToast != null && editToast.length > 0) {
            this.mEditToast = new String[editToast.length + (isEditVisibility ? 1 : 0)];
            for (int i = 0; i < editToast.length; i++) {
                this.mEditToast[i] = editToast[i];
            }
        }
        return this;
    }

    /**
     * 显示取消按钮
     *
     * @param visibility
     * @return
     */
    public CommonDialog setNoButtonVisibility(boolean visibility) {
        mCommonBing.dialogCommonNo.setVisibility(visibility ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置确定按钮
     *
     * @param okText
     * @return
     */
    public CommonDialog setOkText(Object okText) {

        if (okText instanceof String) {
            mCommonBing.dialogCommonOk.setText((String) okText);
        } else if (okText instanceof Integer) {
            mCommonBing.dialogCommonOk.setText((Integer) okText);
        }
        return this;
    }

    /**
     * 点确定关闭 Dialog 默认：false 不关闭
     *
     * @param okDismiss
     * @return
     */
    public CommonDialog setOkDismiss(boolean okDismiss) {
        this.isOkDismiss = okDismiss;
        return this;
    }

    /**
     * 显示　Dialog
     *
     * @return
     */
    public CommonDialog shows() {
        mAlertDialog = mAlertBuilder.show();
        return this;
    }

    /**
     * 添加要显示的View
     */
    private View addMainView(int layoutId) {
        return DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, null, false).getRoot();
    }

    /**
     * 获取根布局View
     *
     * @return
     */
    public View getViewDialog() {
        return mViewDialog;
    }

    /**
     * 获取绑定跟布局　ViewDataBinding
     *
     * @return
     */
    public B getDialogBing() {
        return DataBindingUtil.getBinding(getView());
    }

    /**
     * 获取根布局View或是添加View
     *
     * @return
     */
    private View getView() {
        return addVIew != null ? addVIew : mViewDialog;
    }
}
