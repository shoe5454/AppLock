package com.lzx.lock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lzx.lock.base.AppConstants;
import com.lzx.lock.services.BackgroundManager;
import com.lzx.lock.utils.SpUtil;

public class LockRestarterBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "LockRestart";
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        if (lockState) {

            Log.d(TAG, "onReceive:  i am restarting the service");
            BackgroundManager.startBackgroundLockService(context);
        }
    }
}
