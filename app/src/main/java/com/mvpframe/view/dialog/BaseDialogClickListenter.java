package com.mvpframe.view.dialog;

/**
 * Dialog
 * <p>
 * Data：2019/01/28
 *
 * @author yong
 */
public interface BaseDialogClickListenter {

    interface Among extends BaseDialogClickListenter {
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
