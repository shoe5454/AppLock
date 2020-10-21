package com.lzx.lock.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lzx.lock.R;

import java.util.List;

public class AnswerSelectionView extends LinearLayout {
    private AnswerSelectionView.OnPatternListener mOnPatternListener;

    public interface OnPatternListener {
        void onAnswerSelected();
    }

    public AnswerSelectionView(Context context, AttributeSet set) {
        super(context, set);
    }

    public void clearAnswer() {
        // TODO
    }

    public void setAnswers() {
        Button myButton = new Button(this.getContext());
        myButton.setText("A cat");
        myButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAnswerSelected();
            }
        });

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.addView(myButton, lp);
    }

    public void setOnPatternListener(AnswerSelectionView.OnPatternListener onPatternListener) {
        mOnPatternListener = onPatternListener;
    }

    private void notifyAnswerSelected() {
        if (mOnPatternListener != null) {
            mOnPatternListener.onAnswerSelected();
        }
        //sendAccessEvent(R.string.lockscreen_access_pattern_start);
    }


}
