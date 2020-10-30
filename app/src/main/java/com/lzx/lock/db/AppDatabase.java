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

    private static void insert(SupportSQLiteDatabase db, AnswerType type, AnswerSubtype subtype, int imageResId, String text, boolean plural, boolean startsWithVowelSound) {
        ContentValues cv = new ContentValues();
        cv.put("type", type.ordinal());
        if (subtype != null)
            cv.put("subtype", subtype.ordinal());
        cv.put("imageResId", imageResId);
        cv.put("text", text);
        cv.put("plural", plural);
        cv.put("startsWithVowelSound", startsWithVowelSound);
        db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);
    }

    public static AppDatabase populate(Context applicationContext) {
        RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
            public void onCreate (SupportSQLiteDatabase db) {
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_bird, "bird", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_cat, "cat", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_dog, "dog", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_duck, "duck", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_elephant, "elephant", false, true);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_fish, "fish", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_horse, "horse", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_lion, "lion", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_owl, "owl", false, true);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_pig, "pig", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_shark, "shark", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_whale, "whale", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_boat, "boat", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_bus, "bus", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_car, "car", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_train, "train", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_truck, "truck", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_airplane, "airplane", false, true);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_bicycle, "bicycle", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_rocket, "rocket", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_helicopter, "helicopter", false, false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_egg, "egg", false, true);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_ice_cream, "ice cream", false, true);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_pizza, "pizza", false, false);

            }
        };
        return Room.databaseBuilder(applicationContext, AppDatabase.class, "reading-lock")
                .addCallback(rdc)
                .build();
    }
}
