package com.lzx.lock.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lzx.lock.db.entities.Answer;

import java.util.Collection;
import java.util.List;

@Dao
public interface AnswerDao {
    @Query("SELECT COUNT(uid) FROM answer")
    int getCount();

    @Query("SELECT * FROM answer")
    List<Answer> getAll();

    @Query("SELECT * FROM answer ORDER BY score")
    List<Answer> getAllOrderByScore();

    @Query("SELECT * FROM answer WHERE uid = :uid")
    Answer getByUid(int uid);

    @Query("SELECT * FROM answer WHERE type = :type")
    List<Answer> getByType(Integer type);

    @Query("SELECT * FROM answer WHERE type = :type AND subtype = :subtype")
    List<Answer> getByTypeAndSubtype(Integer type, Integer subtype);

    @Insert
    void insertAll(Answer... answers);

    @Insert
    public Long insert(Answer answer);

    @Update
    public void update(Answer answer);

    @Update
    public int updateAll(Collection<Answer> answers);
}
