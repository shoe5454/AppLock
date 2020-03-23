package com.lzx.lock;

import com.lzx.lock.activities.lock.GestureUnlockActivity;
import com.lzx.lock.base.BaseActivity;
import com.lzx.lock.utils.SpUtil;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;


public class LockApplication extends LitePalApplication {


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


    @Override
    public void onCreate() {
        super.onCreate();
        SpUtil.getInstance().init(this);
        activityList = new ArrayList<>();
    }
}
