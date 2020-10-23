package com.lzx.lock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lzx.lock.R;
import com.lzx.lock.db.dao.AnswerDao;
import com.lzx.lock.db.entities.Answer;
import com.lzx.lock.db.entities.AnswerSubtype;
import com.lzx.lock.db.entities.AnswerType;

@Database(entities = {Answer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AnswerDao answerDao();

    public static AppDatabase populate(Context applicationContext) {
        RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
            public void onCreate (SupportSQLiteDatabase db) {
                ContentValues cv = new ContentValues();

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_cat);
                cv.put("text", "cat");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_dog);
                cv.put("text", "dog");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_fish);
                cv.put("text", "fish");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_duck);
                cv.put("text", "duck");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_pig);
                cv.put("text", "pig");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_horse);
                cv.put("text", "horse");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_lion);
                cv.put("text", "lion");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_elephant);
                cv.put("text", "elephant");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_bird);
                cv.put("text", "bird");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_whale);
                cv.put("text", "whale");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);

                cv.put("type", AnswerType.THING_IDENTIFICATION.ordinal());
                cv.put("subtype", AnswerSubtype.ANIMAL.ordinal());
                cv.put("imageResId", R.drawable.question_shark);
                cv.put("text", "shark");
                cv.put("plural", false);
                db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);
            }
        };
        return Room.databaseBuilder(applicationContext, AppDatabase.class, "reading-lock")
                .addCallback(rdc)
                .build();
    }
}
