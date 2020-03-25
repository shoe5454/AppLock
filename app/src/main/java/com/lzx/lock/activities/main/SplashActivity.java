package com.lzx.lock.activities.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lzx.lock.R;
import com.lzx.lock.activities.lock.GestureSelfUnlockActivity;
import com.lzx.lock.activities.pwd.CreatePwdActivity;
import com.lzx.lock.base.AppConstants;
import com.lzx.lock.base.BaseActivity;
import com.lzx.lock.services.BackgroundManager;
import com.lzx.lock.services.LoadAppListService;
import com.lzx.lock.utils.AppUtils;
import com.lzx.lock.utils.LockUtil;
import com.lzx.lock.utils.SpUtil;
import com.lzx.lock.widget.DialogPermission;
import com.lzx.lock.widget.DialogSelection;

/**
 * Created by xian on 2017/2/17.
 */

public class SplashActivity extends BaseActivity {

    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 101;
    private static final int RESULT_ACTION_ACCESSIBILITY_SETTINGS = 103;
    public static final String TAG = "SplashActivity";

    private ImageView mImgSplash;

    @Nullable
    private ObjectAnimator mAnimator;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        AppUtils.hideStatusBar(getWindow(), true);
        mImgSplash = findViewById(R.id.img_splash);
    }

    @Override
    protected void initData() {

        //loads apps in background...
        BackgroundManager.startService(this, LoadAppListService.class);


        mAnimator = ObjectAnimator.ofFloat(mImgSplash, "alpha", 0.5f, 1);
        mAnimator.setDuration(1000);
        mAnimator.start();
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                initAppLock();
            }
        });
    }

    private void initAppLock() {
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_IF_FIRST_LOCK_NOT_CHOOSEN, true)) {
            showFirstSelectionDialog();
        } else {

            BackgroundManager.startBackgroundLockService(this);
            Intent intent = new Intent(SplashActivity.this, GestureSelfUnlockActivity.class);
            intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.THIS_APP_PACKAGE_NAME);
            intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void showFirstSelectionDialog() {
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true);
        DialogSelection dialogSelection = new DialogSelection(SplashActivity.this);
        dialogSelection.setOnAccessibilityClickListener(new DialogSelection.OnClickListener() {
            @Override
            public void onClick() {
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_TYPE_ACCESSIBILITY, true);
                showAccessibilityDialog();
            }
        });
        dialogSelection.setOnUsageStatsClickListener(new DialogSelection.OnClickListener() {
            @Override
            public void onClick() {
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_TYPE_ACCESSIBILITY, false);
                showUsageStatsDialog();
            }
        });
        dialogSelection.show();
    }

    private void showUsageStatsDialog() {
        if (!LockUtil.isStatAccessPermissionSet(SplashActivity.this) && LockUtil.isNoOption(SplashActivity.this)) {
            DialogPermission dialog = new DialogPermission(SplashActivity.this);
            dialog.show();
            dialog.setOnClickListener(new DialogPermission.onClickListener() {
                @Override
                public void onClick() {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                    }
                }
            });
        } else {
            gotoCreatePwdActivity();
        }
    }

    private void showAccessibilityDialog() {
        if (!LockUtil.isAccessibilitySettingsOn(getApplicationContext())) {
            //startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent, RESULT_ACTION_ACCESSIBILITY_SETTINGS);
        } else {
            gotoCreatePwdActivity();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            if (LockUtil.isStatAccessPermissionSet(SplashActivity.this)) {
                gotoCreatePwdActivity();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (requestCode == RESULT_ACTION_ACCESSIBILITY_SETTINGS) {
            if (LockUtil.isAccessibilitySettingsOn(getApplicationContext())) {
                Log.d(TAG, "onActivityResult: accessiblity settings on result code== " + resultCode);
                gotoCreatePwdActivity();
            }
            else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private void gotoCreatePwdActivity() {
        BackgroundManager.startBackgroundLockService(this);

        Intent intent = new Intent(SplashActivity.this, CreatePwdActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void initAction() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAnimator = null;
    }
}
