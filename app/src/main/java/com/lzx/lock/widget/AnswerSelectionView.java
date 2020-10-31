package com.lzx.lock.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lzx.lock.R;
import com.lzx.lock.db.entities.Answer;

import java.util.Collections;
import java.util.List;

public class AnswerSelectionView extends LinearLayout {
    private AnswerSelectionView.OnPatternListener mOnPatternListener;
    private ColorStateList mDefaultButtonBackgroundTintList;

    public interface OnPatternListener {
        void onAnswerSelected(Answer answer);
    }

    public AnswerSelectionView(Context context, AttributeSet set) {
        super(context, set);
    }

    public void setAnswers(List<Answer> answers) {
        Typeface typeface = ResourcesCompat.getFont(this.getContext(), R.font.kg_first_time_in_forever);
        Collections.shuffle(answers);
        for (Answer answer : answers) {
            final Answer a = answer;
            Button myButton = new Button(this.getContext());
            myButton.setText(answer.text);
            myButton.setTag(answer.uid);
            myButton.setTypeface(typeface);
            myButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
            myButton.setTransformationMethod(null);
            myButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyAnswerSelected(a);
                }
            });
            mDefaultButtonBackgroundTintList = myButton.getBackgroundTintList();
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

    public void setDisplayCorrectAnswer(Answer answer) {
        for (int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);
            if ((Integer)view.getTag() == answer.uid) {
                view.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                break;
            }
        }
    }

    public void setDisplayIncorrectAnswer(Answer answer) {
        for (int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);
            if ((Integer)view.getTag() == answer.uid) {
                view.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                break;
            }
        }
    }

    public void clearAnswer() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);
            if (view instanceof Button)
                view.setBackgroundTintList(mDefaultButtonBackgroundTintList);
        }
    }

    public void disable() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);
            if (view instanceof Button) {
                view.setEnabled(false);
                view.setClickable(false);
            }
        }
    }

    public void enable() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);
            if (view instanceof Button) {
                view.setEnabled(true);
                view.setClickable(true);
            }
        }
    }
}
