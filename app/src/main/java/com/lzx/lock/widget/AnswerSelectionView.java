package com.lzx.lock.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
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
        Typeface typeface = ResourcesCompat.getFont(this.getContext(), R.font.kg_first_time_in_forever);
        myButton.setTypeface(typeface);
        myButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        myButton.setTransformationMethod(null);
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
