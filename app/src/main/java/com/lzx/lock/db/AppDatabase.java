package com.lzx.lock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lzx.lock.LockApplication;
import com.lzx.lock.R;
import com.lzx.lock.db.dao.AnswerDao;
import com.lzx.lock.db.entities.Answer;
import com.lzx.lock.db.entities.AnswerSubtype;
import com.lzx.lock.db.entities.AnswerType;

@Database(entities = {Answer.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AnswerDao answerDao();

    private static void initialPopulate(SupportSQLiteDatabase db) {
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_bird, "A bird", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_cat, "A cat", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_cow, "A cow", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_dog, "A dog", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_duck, "A duck", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_elephant, "An elephant", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_fish, "A fish", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_horse, "A horse", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_lion, "A lion", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_owl, "An owl", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_pig, "A pig", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_shark, "A shark", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.ANIMAL, R.drawable.question_whale, "A whale", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_airplane, "An airplane", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_bicycle, "A bicycle", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_boat, "A boat", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_bus, "A bus", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_car, "A car", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_helicopter, "A helicopter", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_rocket, "A rocket", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_train, "A train", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.VEHICLE, R.drawable.question_truck, "A truck", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_bread, "Bread", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_cheese, "Cheese", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_egg, "An egg", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_ice_cream, "Ice cream", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_milk, "Milk", false);
        insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_pizza, "A pizza", false);
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_cake, "A cake", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.FOOD, R.drawable.question_chocolate, "Chocolate", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.BODY_PART, R.drawable.question_nose, "Nose", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.BODY_PART, R.drawable.question_mouth, "Mouth", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.BODY_PART, R.drawable.question_eye, "Eye", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.BODY_PART, R.drawable.question_ear, "Ear", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.BODY_PART, R.drawable.question_hair, "Hair", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.BODY_PART, R.drawable.question_hand, "Hand", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.BODY_PART, R.drawable.question_leg, "Leg", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.BODY_PART, R.drawable.question_foot, "Foot", false);
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.PERSON_DESCRIPTION, R.drawable.question_man, "A man", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.PERSON_DESCRIPTION, R.drawable.question_woman, "A woman", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.PERSON_DESCRIPTION, R.drawable.question_boy, "A boy", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.PERSON_DESCRIPTION, R.drawable.question_girl, "A girl", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, null, R.drawable.question_ball, "A ball", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, null, R.drawable.question_fan, "A fan", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, null, R.drawable.question_tree, "A tree", false);
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            //insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.FRUIT, R.drawable.question_apple, "An apple", false);
            //insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.FRUIT, R.drawable.question_banana, "A banana", false);
            //insert(db, AnswerType.VERB_IDENTIFICATION, null, R.drawable.question_swim, "Swim", false);
            //insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.INSECT, R.drawable.question_ant, "An ant", false);
            //insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.INSECT, R.drawable.question_bee, "A bee", false);
            //insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.INSECT, R.drawable.question_spider, "A spider", false);
            //insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.BODY_PART, R.drawable.question_head, "Head", false);
            /*insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.RELATIONSHIP, R.drawable.question_my_mother, "My mother", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.RELATIONSHIP, R.drawable.question_my_father, "My father", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.RELATIONSHIP, R.drawable.question_my_brother, "My brother", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.RELATIONSHIP, R.drawable.question_me, "Me", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.RELATIONSHIP, R.drawable.question_my_grandmother1, "My grandmother", false);
            insert(db, AnswerType.NOUN_IDENTIFICATION, AnswerSubtype.RELATIONSHIP, R.drawable.question_my_grandmother2, "My grandmother", false);
            */
            //insert(db, AnswerType.ADJECTIVE_IDENTIFICATION, AnswerSubtype.COLOUR, R.drawable.question_yellow, "Yellow", false);
            //insert(db, AnswerType.ADJECTIVE_IDENTIFICATION, AnswerSubtype.COLOUR, R.drawable.question_yellow, "Yellow", false);
            // family relationships, my mother, my father,
        }
    };

    private static void insert(SupportSQLiteDatabase db, AnswerType type, AnswerSubtype subtype, int imageResId, String text, boolean plural/*, AnswerQuantifier quantifier*/) {
        ContentValues cv = new ContentValues();
        cv.put("type", type.ordinal());
        if (subtype != null)
            cv.put("subtype", subtype.ordinal());
        cv.put("imageResName", LockApplication.getInstance().getResources().getResourceEntryName(imageResId));
        cv.put("text", text);
        cv.put("plural", plural);
        cv.put("score", 0);
        cv.put("lowestScore", 0);
        cv.put("currentMaxConsecutiveCorrectGuesses", 0);
        db.insert("answer", SQLiteDatabase.CONFLICT_NONE, cv);
    }

    public static AppDatabase populate(Context applicationContext) {
        RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
            public void onCreate (SupportSQLiteDatabase db) {
                initialPopulate(db);
            }
        };
        return Room.databaseBuilder(applicationContext, AppDatabase.class, "reading-lock")
                .addCallback(rdc)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build();
    }
}
