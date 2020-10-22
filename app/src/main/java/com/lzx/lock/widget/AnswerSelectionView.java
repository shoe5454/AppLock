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
import com.lzx.lock.db.entities.Answer;

import java.util.List;

public class AnswerSelectionView extends LinearLayout {
    private AnswerSelectionView.OnPatternListener mOnPatternListener;

    public interface OnPatternListener {
        void onAnswerSelected(Answer answer);
    }

    public AnswerSelectionView(Context context, AttributeSet set) {
        super(context, set);
    }

    public void clearAnswer() {
        // TODO
    }

    public void setAnswers(List<Answer> answers) {
        Typeface typeface = ResourcesCompat.getFont(this.getContext(), R.font.kg_first_time_in_forever);
        for (Answer answer : answers) {
            final Answer a = answer;
            Button myButton = new Button(this.getContext());
            myButton.setText("A " + answer.text);
            //myButton.setDa
            myButton.setTypeface(typeface);
            myButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            myButton.setTransformationMethod(null);
            myButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyAnswerSelected(a);
                }
            });
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            this.addView(myButton, lp);
        }

    }

    public void setOnPatternListener(AnswerSelectionView.OnPatternListener onPatternListener) {
        mOnPatternListener = onPatternListener;
    }

    private void notifyAnswerSelected(Answer answer) {
        if (mOnPatternListener != null) {
            mOnPatternListener.onAnswerSelected(answer);
        }
        //sendAccessEvent(R.string.lockscreen_access_pattern_start);
    }


}
