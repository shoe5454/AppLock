package com.lzx.lock.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lzx.lock.db.entities.Answer;

import java.util.List;

@Dao
public interface AnswerDao {
    @Query("SELECT COUNT(uid) FROM answer")
    int getCount();

    @Query("SELECT * FROM answer")
    List<Answer> getAll();

    @Query("SELECT * FROM answer WHERE uid = :uid")
    Answer getByUid(int uid);

    @Query("SELECT * FROM answer WHERE type = :type ORDER BY uid")
    List<Answer> getByTypeOrderByUid(Integer type);

    @Query("SELECT * FROM answer WHERE type = :type AND subtype = :subtype ORDER BY uid")
    List<Answer> getByTypeAndSubtypeOrderByUid(Integer type, Integer subtype);

    @Insert
    void insertAll(Answer... answers);

    @Insert
    public Long insert(Answer answer);
}
