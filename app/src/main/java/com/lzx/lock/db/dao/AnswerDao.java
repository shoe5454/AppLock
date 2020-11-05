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

    @Query("SELECT * FROM answer ORDER BY score, lowestScore")
    List<Answer> getOrderByScoreThenLowestScore();

    @Query("SELECT * FROM answer WHERE currentMaxConsecutiveCorrectGuesses <= :currentMaxConsecutiveCorrectGuesses ORDER BY score, lowestScore")
    List<Answer> getByMaxCurrentMaxConsecutiveCorrectGuessesOrderByScoreThenLowestScore(int currentMaxConsecutiveCorrectGuesses);

    @Query("SELECT * FROM answer WHERE uid = :uid")
    Answer getByUid(int uid);

    @Query("SELECT * FROM answer WHERE type = :type")
    List<Answer> getByType(Integer type);

    @Query("SELECT * FROM answer WHERE type = :type AND uid <> :uid")
    List<Answer> getByTypeAndUidNotEquals(int type, int uid);

    @Query("SELECT * FROM answer WHERE type = :type AND subtype = :subtype")
    List<Answer> getByTypeAndSubtype(Integer type, Integer subtype);

    @Query("SELECT * FROM answer WHERE type = :type AND subtype = :subtype AND uid <> :uid")
    List<Answer> getByTypeAndSubtypeAndUidNotEquals(int type, Integer subtype, int uid);

    @Insert
    void insertAll(Answer... answers);

    @Insert
    public Long insert(Answer answer);

    @Update
    public void update(Answer answer);

    @Update
    public int updateAll(Collection<Answer> answers);

}
