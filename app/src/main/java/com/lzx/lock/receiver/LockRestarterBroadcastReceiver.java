package com.lzx.lock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lzx.lock.base.AppConstants;
import com.lzx.lock.services.BackgroundManager;
import com.lzx.lock.utils.SpUtil;

public class LockRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        if (lockState) {
            BackgroundManager.startBackgroundLockService(context);
        }
    }
}
