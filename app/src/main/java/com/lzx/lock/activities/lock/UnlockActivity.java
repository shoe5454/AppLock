package com.lzx.lock.activities.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzx.lock.LockApplication;
import com.lzx.lock.R;
import com.lzx.lock.activities.main.MainActivity;
import com.lzx.lock.base.AppConstants;
import com.lzx.lock.base.BaseActivity;
import com.lzx.lock.db.CommLockInfoManager;
import com.lzx.lock.db.dao.AnswerDao;
import com.lzx.lock.db.entities.Answer;
import com.lzx.lock.db.entities.AnswerSubtype;
import com.lzx.lock.db.entities.AnswerType;
import com.lzx.lock.services.LockService;
import com.lzx.lock.utils.LockPatternUtils;
import com.lzx.lock.utils.LockUtil;
import com.lzx.lock.utils.SpUtil;
import com.lzx.lock.utils.StatusBarUtil;
import com.lzx.lock.widget.AnswerSelectionView;
import com.lzx.lock.widget.LockPatternViewPattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by xian on 2017/2/17.
 */

public class UnlockActivity extends BaseActivity {

    public static final String FINISH_UNLOCK_THIS_APP = "finish_unlock_this_app";
    private final Executor executor = Executors.newSingleThreadExecutor();
    private AnswerSelectionView mAnswerSelectionView;
    private ImageView mUnLockQuestionImage;
    private TextView mUnlockQuestionText;
    private ViewGroup mUnLockLayout;
    private PackageManager packageManager;
    private String pkgName;
    private String actionFrom;
    private LockPatternUtils mLockPatternUtils;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private CommLockInfoManager mLockInfoManager;
    private LockPatternViewPattern mPatternViewPattern;
    private GestureUnlockReceiver mGestureUnlockReceiver;
    private ApplicationInfo appInfo;
    //private Drawable iconDrawable;
    //private String appLabel;
    private Answer mCorrectAnswer;
    private HashMap<Integer, Answer> mWrongAnswersSelected = new HashMap<>();
    @NonNull
    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mAnswerSelectionView.clearAnswer();
            mAnswerSelectionView.enable();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_unlock;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        mUnLockLayout = findViewById(R.id.unlock_layout);
        mAnswerSelectionView = findViewById(R.id.unlock_answer_selection);
        mUnlockQuestionText = findViewById(R.id.unlock_question_text);
        mUnLockQuestionImage = findViewById(R.id.unlock_question_image);
    }

    @Override
    protected void initData() {
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
        actionFrom = getIntent().getStringExtra(AppConstants.LOCK_FROM);
        packageManager = getPackageManager();
        mLockInfoManager = new CommLockInfoManager(this);

        executor.execute(() -> {
            mWrongAnswersSelected.clear();
            AnswerDao dao = ((LockApplication)this.getApplication()).getDb().answerDao();
            // Get answer with lowest score and lowest lowestScore with <= 3 correct consecutive guesses
            List<Answer> allAnswers = dao.getByMaxCurrentMaxConsecutiveCorrectGuessesOrderByScoreThenLowestScore(3);
            if (allAnswers.isEmpty()) {
                allAnswers = dao.getOrderByScoreThenLowestScore();
            }
            Answer correctAnswer = allAnswers.get(0);
            List<Answer> otherAnswers = new ArrayList<>();
            switch (AnswerType.values()[correctAnswer.type]) {
                case NOUN_IDENTIFICATION:
                    otherAnswers = initDataNounIdentification(dao, allAnswers, correctAnswer);
                    break;
                case ADJECTIVE_IDENTIFICATION:
                    break;
            }
            final List<Answer> otherAnswersFinal = otherAnswers;
            new Handler(Looper.getMainLooper()).post(() -> {
               updateAnswerSelectionView(correctAnswer, otherAnswersFinal);
            });
        });

        //initLayoutBackground();
        initAnswerSelectionView();


        mGestureUnlockReceiver = new GestureUnlockReceiver();
        IntentFilter filter = new IntentFilter();
        //  filter.addAction(UnLockMenuPopWindow.UPDATE_LOCK_VIEW);
        filter.addAction(FINISH_UNLOCK_THIS_APP);
        registerReceiver(mGestureUnlockReceiver, filter);
    }

    private List<Answer> initDataNounIdentification(AnswerDao dao, List<Answer> allAnswers, Answer correctAnswer) {
        // Remove answers which do not match type and subtype, and remove the correct answer
        List<Answer> allOtherAnswers = allAnswers.stream().filter(answer -> {
            return answer.type == correctAnswer.type && (correctAnswer.subtype == null || answer.subtype == correctAnswer.subtype) && answer.uid != correctAnswer.uid;
        }).collect(Collectors.toList());
        // Pick 3 random answers from the other answers
        List<Answer> otherAnswers = new ArrayList<>();
        if (allOtherAnswers.size() == 3) {
            otherAnswers.addAll(allOtherAnswers);
        } else {
            if (allOtherAnswers.size() < 3) {
                if (correctAnswer.subtype == null)
                    allOtherAnswers = dao.getByTypeAndUidNotEquals(correctAnswer.type, correctAnswer.uid);
                else
                    allOtherAnswers = dao.getByTypeAndSubtypeAndUidNotEquals(correctAnswer.type, correctAnswer.subtype, correctAnswer.uid);
            }
            int lowestIndex = 0;
            int highestIndex = allOtherAnswers.size() - 1;
            while (otherAnswers.size() < 3) {
                final int index = new Random().nextInt(highestIndex - lowestIndex + 1) + lowestIndex;
                Answer otherAnswer = allOtherAnswers.get(index);
                if (otherAnswers.stream().noneMatch((answer) -> answer.uid == otherAnswer.uid)) {
                    otherAnswers.add(otherAnswer);
                }
            }
        }
        return otherAnswers;
    }

    /*private void initLayoutBackground() {
        try {
            appInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (appInfo != null) {
                //iconDrawable = packageManager.getApplicationIcon(appInfo);
                //appLabel = packageManager.getApplicationLabel(appInfo).toString();
                final Drawable icon = packageManager.getApplicationIcon(appInfo);
                mUnLockLayout.setBackgroundDrawable(icon);
                mUnLockLayout.getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                mUnLockLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                                mUnLockLayout.buildDrawingCache();
                                int width = mUnLockLayout.getWidth(), height = mUnLockLayout.getHeight();
                                if (width == 0 || height == 0) {
                                    Display display = getWindowManager().getDefaultDisplay();
                                    Point size = new Point();
                                    display.getSize(size);
                                    width = size.x;
                                    height = size.y;
                                }
                                Bitmap bmp = LockUtil.drawableToBitmap(icon, width, height);
                                try {
                                    LockUtil.blur(UnlockActivity.this, LockUtil.big(bmp), mUnLockLayout, width, height);
                                } catch (IllegalArgumentException ignore) {
                                    CrashReporter.logException(ignore);
                                }
                                return true;
                            }
                        });
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    private void initAnswerSelectionView() {
        //mAnswerSelectionView.setLineColorRight(0x80ffffff);
        //mLockPatternUtils = new LockPatternUtils(this);
        /*mPatternViewPattern = new LockPatternViewPattern(mAnswerSelectionView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(@NonNull List<LockPatternView.Cell> pattern) {
                if (mLockPatternUtils.checkPattern(pattern)) { //
                    mAnswerSelectionView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    if (actionFrom.equals(AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY)) {
                        startActivity(new Intent(UnlockActivity.this, MainActivity.class));
                        finish();
                    } else {
                        SpUtil.getInstance().putLong(AppConstants.LOCK_CURR_MILLISECONDS, System.currentTimeMillis());
                        SpUtil.getInstance().putString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, pkgName);

                        //Send the last unlocked time to the app lock service
                        Intent intent = new Intent(LockService.UNLOCK_ACTION);
                        intent.putExtra(LockService.LOCK_SERVICE_LASTTIME, System.currentTimeMillis());
                        intent.putExtra(LockService.LOCK_SERVICE_LASTAPP, pkgName);
                        sendBroadcast(intent);

                        mLockInfoManager.unlockCommApplication(pkgName);
                        finish();
                    }
                } else {
                    mAnswerSelectionView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                        mFailedPatternAttemptsSinceLastTimeout++;
                        int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                        if (retry >= 0) {
                            String format = getResources().getString(R.string.password_error_count);
                            mUnlockQuestionText.setText(format);
                            //TODO: click a pic of intruder
                        }
                    } else {

                        //ToastUtil.showShort(getString(R.string.password_short));
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= 3) {
                        mAnswerSelectionView.postDelayed(mClearPatternRunnable, 500);
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) { // The number of failures is greater than the maximum number of error attempts before blocking the user
                        mAnswerSelectionView.postDelayed(mClearPatternRunnable, 500);
                    } else {
                        mAnswerSelectionView.postDelayed(mClearPatternRunnable, 500);
                    }
                }
            }
        });
        mAnswerSelectionView.setOnPatternListener(mPatternViewPattern);*/
        mAnswerSelectionView.setOnPatternListener(new AnswerSelectionView.OnPatternListener() {
            @Override
            public void onAnswerSelected(Answer answer) {
                if (mCorrectAnswer.uid == answer.uid) { //
                    mAnswerSelectionView.setDisplayCorrectAnswer(answer);
                    if (actionFrom.equals(AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY)) {
                        startActivity(new Intent(UnlockActivity.this, MainActivity.class));
                        finish();
                    } else {
                        SpUtil.getInstance().putLong(AppConstants.LOCK_CURR_MILLISECONDS, System.currentTimeMillis());
                        SpUtil.getInstance().putString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, pkgName);

                        //Send the last unlocked time to the app lock service
                        Intent intent = new Intent(LockService.UNLOCK_ACTION);
                        intent.putExtra(LockService.LOCK_SERVICE_LASTTIME, System.currentTimeMillis());
                        intent.putExtra(LockService.LOCK_SERVICE_LASTAPP, pkgName);
                        sendBroadcast(intent);

                        executor.execute(() -> {
                            AnswerDao dao = ((LockApplication)UnlockActivity.this.getApplication()).getDb().answerDao();
                            if (mWrongAnswersSelected.isEmpty()) {
                                mCorrectAnswer.score++;
                                mCorrectAnswer.currentMaxConsecutiveCorrectGuesses++;
                                mCorrectAnswer.lastCorrectGuessTimestamp = System.currentTimeMillis();
                            } else {
                                mCorrectAnswer.currentMaxConsecutiveCorrectGuesses = 0;
                                mCorrectAnswer.score--;
                                if (mCorrectAnswer.score < mCorrectAnswer.lowestScore) {
                                    mCorrectAnswer.lowestScore = mCorrectAnswer.score;
                                    mCorrectAnswer.lowestScoreTimestamp = System.currentTimeMillis();
                                }
                                Collection<Answer> toUpdates = mWrongAnswersSelected.values();
                                for (Answer toUpdate : toUpdates) {
                                    toUpdate.score--;
                                    if (toUpdate.score < toUpdate.lowestScore) {
                                        toUpdate.lowestScore = toUpdate.score;
                                        toUpdate.lowestScoreTimestamp = System.currentTimeMillis();
                                    }
                                }
                                dao.updateAll(toUpdates);
                            }
                            dao.update(mCorrectAnswer);
                            new Handler(Looper.getMainLooper()).post(() -> {
                                //mLockInfoManager.unlockCommApplication(pkgName);
                                finish();
                            });
                        });
                    }
                } else {
                    mWrongAnswersSelected.put(answer.uid, answer);
                    mAnswerSelectionView.setDisplayIncorrectAnswer(answer);
                    mAnswerSelectionView.disable();
                    //if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                        mFailedPatternAttemptsSinceLastTimeout++;
                        /*int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                        if (retry >= 0) {
                            String format = getResources().getString(R.string.password_error_count);
                            mUnlockQuestionText.setText(format);
                            //TODO: click a pic of intruder
                        }*/
                    //} else {

                        //ToastUtil.showShort(getString(R.string.password_short));
                    //}
                    //if (mFailedPatternAttemptsSinceLastTimeout >= 3) {
                        mAnswerSelectionView.postDelayed(mClearPatternRunnable, 2000 * mFailedPatternAttemptsSinceLastTimeout);
                    //}
                    /*if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) { // The number of failures is greater than the maximum number of error attempts before blocking the user
                        mAnswerSelectionView.postDelayed(mClearPatternRunnable, 500);
                    } else {
                        mAnswerSelectionView.postDelayed(mClearPatternRunnable, 500);
                    }*/
                }
            }
        });
        //mAnswerSelectionView.setTactileFeedbackEnabled(true);
    }

    private void updateAnswerSelectionView(Answer correctAnswer, List<Answer> otherAnswers) {
        mCorrectAnswer = correctAnswer;
        switch (AnswerType.values()[mCorrectAnswer.type]) {
            case NOUN_IDENTIFICATION:
                if (mCorrectAnswer.subtype == null) {
                    mUnlockQuestionText.setText("What is this?");
                } else {
                    switch (AnswerSubtype.values()[mCorrectAnswer.subtype]) {
                        case BODY_PART:
                            mUnlockQuestionText.setText("What body part is this?");
                            break;
                        case PERSON_DESCRIPTION:
                            mUnlockQuestionText.setText("What is this?");
                            break;
                        default:
                            mUnlockQuestionText.setText("What " + AnswerSubtype.values()[mCorrectAnswer.subtype].name().toLowerCase() + " is this?");
                    }
                }
                break;
            case ADJECTIVE_IDENTIFICATION:
                break;
        }
        int resId = getResources().getIdentifier(mCorrectAnswer.imageResName, "drawable", getPackageName());
        mUnLockQuestionImage.setImageResource(resId);
        List<Answer> answers = new ArrayList<>();
        answers.add(mCorrectAnswer);
        answers.addAll(otherAnswers);
        mAnswerSelectionView.setAnswers(answers);
    }

    @Override
    public void onBackPressed() {
        if (actionFrom.equals(AppConstants.LOCK_FROM_FINISH)) {
            LockUtil.goHome(this);
        } else if (actionFrom.equals(AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY)) {
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void initAction() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGestureUnlockReceiver);
    }

    private class GestureUnlockReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            String action = intent.getAction();
//            if (action.equals(UnLockMenuPopWindow.UPDATE_LOCK_VIEW)) {
//                mLockPatternView.initRes();
//            } else
            if (action.equals(FINISH_UNLOCK_THIS_APP)) {
                finish();
            }
        }
    }
}
