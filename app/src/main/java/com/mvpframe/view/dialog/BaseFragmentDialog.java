package com.mvpframe.view.dialog;


import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mvpframe.ui.base.interfaces.BaseInterface;

/**
 * Dialog
 * <p>
 * Data：2019/01/28
 *
 * @author yong
 */
public abstract class BaseFragmentDialog<B extends ViewDataBinding> extends DialogFragment implements BaseInterface {

    protected B mDialogBing;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog alertDialog = new Dialog(getContext());

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return alertDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDialogBing = DataBindingUtil.inflate(inflater, getLayoutId(), container, true);

        return mDialogBing.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListeners();
    }

}
