package com.lzx.lock.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lzx.lock.db.entities.Answer;

import java.util.List;

@Dao
public interface AnswerDao {
    @Query("SELECT * FROM answer")
    List<Answer> getAll();

    @Insert
    void insertAll(Answer... answers);

    @Insert
    public Long insert(Answer answer);
}
