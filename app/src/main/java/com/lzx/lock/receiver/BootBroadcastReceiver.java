package com.lzx.lock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.lzx.lock.base.AppConstants;
import com.lzx.lock.services.BackgroundManager;
import com.lzx.lock.services.LoadAppListService;
import com.lzx.lock.utils.SpUtil;


public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            BackgroundManager.startService(context, LoadAppListService.class);
            BackgroundManager.startBackgroundLockService(context);
        }
    }
}
