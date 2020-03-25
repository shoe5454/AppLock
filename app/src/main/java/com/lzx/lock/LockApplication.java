package com.lzx.lock;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.lzx.lock.activities.lock.GestureUnlockActivity;
import com.lzx.lock.base.AppConstants;
import com.lzx.lock.base.BaseActivity;
import com.lzx.lock.utils.SpUtil;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;


public class LockApplication extends LitePalApplication {


    static List<String> homes;//i.e. launchers
    // a list of "this app" activities that are currently running
    private static List<BaseActivity> activityList;

    public static void addActivity(BaseActivity activity) {
        activityList.add(activity);
    }

    public static void finishActivity(BaseActivity activity) {
        activityList.remove(activity);
        activity.finish();
    }

    public static void clearAllActivity() {
        try {
            for (BaseActivity activity : activityList) {
                if (activity != null && !clearAllWhiteList(activity))
                    activity.finish();
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean clearAllWhiteList(BaseActivity activity) {
        return activity instanceof GestureUnlockActivity;
    }

    @NonNull
    public static List<String> getHomes() {
        return homes;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SpUtil.getInstance().init(this);
        activityList = new ArrayList<>();

        homes = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            homes.add(ri.activityInfo.packageName);
        }

    }


    public void passwordLock(String packageName) {
        // LockApplication.clearAllActivity();

        Intent intent = new Intent(this, GestureUnlockActivity.class);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
        intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_FINISH);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d(TAG, "passwordLock: starting lock..");
        startActivity(intent);
    }

    private static final String TAG = "LockApplication";


}
