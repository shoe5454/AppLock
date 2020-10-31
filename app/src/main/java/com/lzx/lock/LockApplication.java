package com.lzx.lock;

import com.lzx.lock.base.BaseActivity;
import com.lzx.lock.activities.lock.UnlockActivity;
import com.lzx.lock.db.AppDatabase;
import com.lzx.lock.utils.SpUtil;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

import io.github.subhamtyagi.crashreporter.CrashReporter;


public class LockApplication extends LitePalApplication {

    private static LockApplication application;
    private static List<BaseActivity> activityList;

    public static LockApplication getInstance() {
        return application;
    }

    AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        //Crash reporter utility
        CrashReporter.initialize(this, getCacheDir().getPath());

        db = AppDatabase.populate(this.getApplicationContext());

        SpUtil.getInstance().init(application);
        activityList = new ArrayList<>();
    }

    public AppDatabase getDb() { return db; }

    public void doForCreate(BaseActivity activity) {
        activityList.add(activity);
    }

    public void doForFinish(BaseActivity activity) {
        activityList.remove(activity);
    }

    public void clearAllActivity() {
        try {
            for (BaseActivity activity : activityList) {
                if (activity != null && !clearAllWhiteList(activity))
                    activity.clear();
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean clearAllWhiteList(BaseActivity activity) {
        return activity instanceof UnlockActivity;
    }
}
