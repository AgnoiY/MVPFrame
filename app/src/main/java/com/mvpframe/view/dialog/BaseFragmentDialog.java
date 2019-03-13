package com.mvpframe.view.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        mDialogBing = DataBindingUtil.inflate(getActivity().getLayoutInflater(), getLayoutId(), null, false);

        alertDialog.setView(mDialogBing.getRoot());

        initData();
        initListeners();

        return alertDialog.create();
    }

}
