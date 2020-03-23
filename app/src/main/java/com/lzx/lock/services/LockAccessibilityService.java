package com.lzx.lock.services;

import android.accessibilityservice.AccessibilityService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.lzx.lock.LockApplication;
import com.lzx.lock.activities.lock.GestureUnlockActivity;
import com.lzx.lock.base.AppConstants;
import com.lzx.lock.db.CommLockInfoManager;
import com.lzx.lock.receiver.LockRestarterBroadcastReceiver;
import com.lzx.lock.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

public class LockAccessibilityService extends AccessibilityService {


    private static String mForegroundPackageName;
    private static LockAccessibilityService mInstance = null;

    private CommLockInfoManager mLockInfoManager;
    public String savePkgName;
    public static boolean isActionLock = false;

    public LockAccessibilityService() {
        mLockInfoManager = CommLockInfoManager.getInstance(this);
    }

    public static LockAccessibilityService getInstance() {
        if (mInstance == null) {
            synchronized (LockAccessibilityService.class) {
                if (mInstance == null) {
                    mInstance = new LockAccessibilityService();
                }
            }
        }
        return mInstance;
    }


    private boolean inWhiteList(String packageName) {
        return packageName.equals(AppConstants.THIS_APP_PACKAGE_NAME);
    }


    @Override
    public void onAccessibilityEvent(@NonNull AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null && event.getClassName() != null) {
                mForegroundPackageName = event.getPackageName().toString();
                Log.d("Locx", "onAccessibilityEvent: event happend");
                lockApp(mForegroundPackageName);
            }
        }
    }


    private void lockApp(String packageName) {
        boolean lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        if (lockState && !TextUtils.isEmpty(packageName) && !inWhiteList(packageName)) {
            boolean isLockOffScreenTime = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN_TIME, false);
            boolean isLockOffScreen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN, false);
            savePkgName = SpUtil.getInstance().getString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, "");

            if (isLockOffScreenTime && !isLockOffScreen) {
                long time = SpUtil.getInstance().getLong(AppConstants.LOCK_CURR_MILLISECONDS, 0);
                long leaverTime = SpUtil.getInstance().getLong(AppConstants.LOCK_APART_MILLISECONDS, 0);
                if (!TextUtils.isEmpty(savePkgName) && !TextUtils.isEmpty(packageName) && !savePkgName.equals(packageName)) {
                    if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                        boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                        if (!isSetUnLock) {
                            if (System.currentTimeMillis() - time > leaverTime) {
                                mLockInfoManager.lockCommApplication(savePkgName);
                            }
                        }
                    }
                }
            }

            if (isLockOffScreenTime && isLockOffScreen) {
                long time = SpUtil.getInstance().getLong(AppConstants.LOCK_CURR_MILLISECONDS, 0);
                long leaverTime = SpUtil.getInstance().getLong(AppConstants.LOCK_APART_MILLISECONDS, 0);
                if (!TextUtils.isEmpty(savePkgName) && !TextUtils.isEmpty(packageName) && !savePkgName.equals(packageName)) {
                    if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                        boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                        if (!isSetUnLock) {
                            if (System.currentTimeMillis() - time > leaverTime) {
                                mLockInfoManager.lockCommApplication(savePkgName);
                            }
                        }
                    }
                }
            }


            if (!isLockOffScreenTime && isLockOffScreen && !TextUtils.isEmpty(savePkgName) && !TextUtils.isEmpty(packageName)) {
                if (!savePkgName.equals(packageName)) {
                    isActionLock = false;
                    if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                        boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                        if (!isSetUnLock) {
                            mLockInfoManager.lockCommApplication(savePkgName);
                        }
                    }
                } else {
                    isActionLock = true;
                }
            }
            if (!isLockOffScreenTime && !isLockOffScreen && !TextUtils.isEmpty(savePkgName) && !TextUtils.isEmpty(packageName) && !savePkgName.equals(packageName)) {
                if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                    boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                    if (!isSetUnLock) {
                        mLockInfoManager.lockCommApplication(savePkgName);
                    }
                }
            }
            if (mLockInfoManager.isLockedPackageName(packageName)) {
                passwordLock(packageName);
            }
        }
    }

    private void passwordLock(String packageName) {
        LockApplication.clearAllActivity();

        Intent intent = new Intent(this, GestureUnlockActivity.class);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
        intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_FINISH);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public String getForegroundPackage() {
        return mForegroundPackageName;
    }

    @Override
    public void onInterrupt() {

    }


    @NonNull
    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        boolean lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        if (lockState) {
            Intent intent = new Intent(this, LockRestarterBroadcastReceiver.class);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }

}
