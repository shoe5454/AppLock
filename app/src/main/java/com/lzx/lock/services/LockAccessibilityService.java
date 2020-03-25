package com.lzx.lock.services;

import android.accessibilityservice.AccessibilityService;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.lzx.lock.LockApplication;
import com.lzx.lock.activities.lock.GestureUnlockActivity;
import com.lzx.lock.base.AppConstants;
import com.lzx.lock.db.CommLockInfoManager;
import com.lzx.lock.receiver.LockRestarterBroadcastReceiver;
import com.lzx.lock.utils.SpUtil;

public class LockAccessibilityService extends AccessibilityService {


    private static final String TAG = "LockAccessibility";
    private static LockAccessibilityService mInstance = null;
    //public String savePkgName;
    private CommLockInfoManager mLockInfoManager;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: bhoos...");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        Log.d(TAG, "onServiceConnected: accessibility connected");

        mLockInfoManager = CommLockInfoManager.getInstance(this);
        ////////////////////////////////////////////
       /* AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC|AccessibilityServiceInfo.FEEDBACK_VISUAL;
        config.flags = AccessibilityServiceInfo.DEFAULT;
        setServiceInfo(config);*/
        ////////////////////////////

        notification();

    }

    private boolean inWhiteList(String packageName) {
        return packageName.equals(AppConstants.THIS_APP_PACKAGE_NAME);
    }


    @Override
    public void onAccessibilityEvent(@NonNull AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent: event happend wtf if");

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null && event.getClassName() != null) {
                // public static boolean isActionLock = false;
                String mForegroundPackageName = event.getPackageName().toString();
                // lockApp(mForegroundPackageName);
                Log.d(TAG, "onAccessibilityEvent: catch::  " + mForegroundPackageName);
                passwordLock(mForegroundPackageName);
            }
        } else {
            Log.d(TAG, "onAccessibilityEvent: type window state no changed");
        }

    }


    private void lockApp(String packageName) {

        boolean lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        Log.d(TAG, "lockApp: " + lockState + "  package name");

        if (lockState && !TextUtils.isEmpty(packageName) && !inWhiteList(packageName)) {
            //savePkgName = SpUtil.getInstance().getString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, "");
            if (mLockInfoManager.isLockedPackageName(packageName)) {
                passwordLock(packageName);
            }
        }
    }

    private void passwordLock(String packageName) {
        Log.d(TAG, "passwordLock: lock this ");
        LockApplication.clearAllActivity();
        Intent intent = new Intent(this, GestureUnlockActivity.class);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
        intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_FINISH);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onInterrupt() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        boolean lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        if (lockState) {
            Intent intent = new Intent(this, LockRestarterBroadcastReceiver.class);
            sendBroadcast(intent);
        }
        Log.d(TAG, "onDestroy: some one destroyed the service");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        boolean lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        if (lockState) {
            Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass());
            restartServiceTask.setPackage(getPackageName());
            PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            myAlarmService.set(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + 1500,
                    restartPendingIntent);
        }
        Log.d(TAG, "onTaskRemoved: some one tried to remove task");
        super.onTaskRemoved(rootIntent);
    }


    private void notification() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setContentText("Trivia Hack most accurate answer")
                .setContentTitle("Tap to remove overlay screen")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true).setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1234, mBuilder.build());
    }

}
