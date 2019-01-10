package com.mvpframe.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mvpframe.R;

import java.util.List;

public class Asd extends BaseQuickAdapter<String, BaseViewHolder> {
    public Asd(@Nullable List<String> data) {
        super(R.layout.ad, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setVisible(R.id.tv, true).setText(R.id.tv, item);
    }
}
