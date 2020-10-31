package com.lzx.lock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lzx.lock.R;
import com.lzx.lock.db.dao.AnswerDao;
import com.lzx.lock.db.entities.Answer;
import com.lzx.lock.db.entities.AnswerSubtype;
import com.lzx.lock.db.entities.AnswerType;

@Database(entities = {Answer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AnswerDao answerDao();

    private static void insert(SupportSQLiteDatabase db, AnswerType type, AnswerSubtype subtype, int imageResId, String text, boolean plural/*, AnswerQuantifier quantifier*/) {
        ContentValues cv = new ContentValues();
        cv.put("type", type.ordinal());
        if (subtype != null)
            cv.put("subtype", subtype.ordinal());
        cv.put("imageResId", imageResId);
        cv.put("text", text);
        cv.put("plural", plural);
        cv.put("score", 0);
        db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);
    }

    public static AppDatabase populate(Context applicationContext) {
        RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
            public void onCreate (SupportSQLiteDatabase db) {
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_bird, "A bird", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_cat, "A cat", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_cow, "A cow", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_dog, "A dog", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_duck, "A duck", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_elephant, "An elephant", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_fish, "A fish", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_horse, "A horse", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_lion, "A lion", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_owl, "An owl", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_pig, "A pig", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_shark, "A shark", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_whale, "A whale", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_airplane, "An airplane", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_bicycle, "A bicycle", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_boat, "A boat", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_bus, "A bus", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_car, "A car", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_helicopter, "A helicopter", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_rocket, "A rocket", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_train, "A train", false);
                insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_truck, "A truck", false);
                //insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_egg, "An egg", false, true);
                //insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_ice_cream, "Ice cream", false, true);
                //insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_pizza, "A pizza", false, false);
                //insert(db, AnswerType.THING_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_milk, "Milk", false, false);
                //        cheese
                //        bread
                // banana
                // family relationships
            }
        };
        return Room.databaseBuilder(applicationContext, AppDatabase.class, "reading-lock")
                .addCallback(rdc)
                .build();
    }
}
