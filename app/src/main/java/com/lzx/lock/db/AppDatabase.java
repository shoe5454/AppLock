package com.lzx.lock.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.lzx.lock.db.dao.AnswerDao;
import com.lzx.lock.db.entities.Answer;

@Database(entities = {Answer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AnswerDao answerDao();
}
