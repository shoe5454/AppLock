package com.lzx.lock.services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.lzx.lock.base.AppConstants;
import com.lzx.lock.receiver.LockRestarterBroadcastReceiver;
import com.lzx.lock.utils.SpUtil;

public class BackgroundManager {

    private static final int period = 15 * 1000;//15 minutes;


    private static boolean isServiceRunning(Context context, Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void startService(Context context, Class<?> serviceClass) {
        if (!isServiceRunning(context, serviceClass)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundServices(context, serviceClass);
            } else {
                context.startService(new Intent(context, serviceClass));
            }
        }
    }

    public static boolean isBackgroundServiceRunning(Context context) {
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_TYPE_ACCESSIBILITY)) {
                return isServiceRunning(context, LockAccessibilityService.class);
            } else {
                return isServiceRunning(context, LockService.class);
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startForegroundServices(Context context, Class<?> serviceClass) {
        context.startForegroundService(new Intent(context, serviceClass));
    }

    private static void startAlarmManager(Context context) {
        Intent intent = new Intent(context, LockRestarterBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 95374, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + period, pendingIntent);

    }

    private static void stopAlarmManager(Context context) {
        Intent intent = new Intent(context, LockRestarterBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 9574, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


    //TODO: make this private...
    public static void stopService(Context context, Class<?> serviceClass) {
        if (isServiceRunning(context, serviceClass)) {
            context.stopService(new Intent(context, serviceClass));
        }
    }


    public static void startBackgroundLockService(Context context) {
        startAlarmManager(context);
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_TYPE_ACCESSIBILITY)) {
                BackgroundManager.startService(context, LockAccessibilityService.class);
            } else {
                BackgroundManager.startService(context, LockService.class);
            }
        }
    }

    public static void stopBackgroundLockService(Context context) {
        stopAlarmManager(context);
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_TYPE_ACCESSIBILITY)) {
                BackgroundManager.stopService(context, LockAccessibilityService.class);
            } else {
                BackgroundManager.stopService(context, LockService.class);
            }
        }
    }
}
