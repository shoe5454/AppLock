package com.lzx.lock;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lzx.lock.base.BaseActivity;
import com.lzx.lock.activities.lock.GestureUnlockActivity;
import com.lzx.lock.db.AppDatabase;
import com.lzx.lock.db.entities.Answer;
import com.lzx.lock.db.entities.AnswerSubtype;
import com.lzx.lock.db.entities.AnswerType;
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

        RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
            public void onCreate (SupportSQLiteDatabase db) {
                ContentValues cv = new ContentValues();
                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_cat);
                cv.put("text", "cat");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);
                /*LockApplication.this.db.answerDao().insert(new Answer()
                        .withType(AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL)
                        .withDetail(R.drawable.question_cat, "cat", false));*/
            }
        };
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "reading-lock")
                .addCallback(rdc)
                .build();

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
        return activity instanceof GestureUnlockActivity;
    }
}
