package com.mvpframe.view.dialog;

public interface DialogInterface {

    interface Among extends DialogInterface {
        /**
         * 中间的按钮
         */
        void dialogTipsAmong();
    }

    /**
     * 确定按钮
     */
    void dialogTipsOk();
}
