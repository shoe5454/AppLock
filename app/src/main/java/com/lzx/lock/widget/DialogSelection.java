package com.lzx.lock.widget;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lzx.lock.R;

public class DialogSelection extends BaseDialog {


    private TextView mBtnAccessibility;
    private TextView mBtnUsageStats;

    private OnClickListener mOnClickListenerA;
    private OnClickListener mOnClickListenerUS;


    @Override
    protected void init() {
        mBtnAccessibility = findViewById(R.id.tv_accessibility);
        mBtnUsageStats = findViewById(R.id.tv_usage_stats);


        mBtnUsageStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListenerUS != null) {
                    dismiss();
                    mOnClickListenerUS.onClick();
                }
            }
        });

        mBtnAccessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListenerA != null) {
                    dismiss();
                    mOnClickListenerA.onClick();
                }
            }
        });


    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_selection;
    }

    public interface OnClickListener {
        void onClick();
    }

    public void setOnAccessibilityClickListener(OnClickListener onClickListener) {
        mOnClickListenerA = onClickListener;
    }

    public void setOnUsageStatsClickListener(OnClickListener onClickListener) {
        mOnClickListenerUS = onClickListener;
    }

    ////////////////////////////
    @Override
    protected AnimatorSet setEnterAnim() {
        return null;
    }

    @Override
    protected AnimatorSet setExitAnim() {
        return null;
    }

    public DialogSelection(@NonNull Context context) {
        super(context);
    }

    @Override
    protected float setWidthScale() {
        return 0.9f;
    }

}
