package com.lzx.lock.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity
public class Answer {
    @PrimaryKey
    public int uid;

    @ColumnInfo
    public int type;

    @ColumnInfo
    public Integer subtype;

    @ColumnInfo
    public int imageResId;

    @ColumnInfo
    public String text;

    @ColumnInfo
    public boolean plural;

    //@ColumnInfo
    //public int quantifier;

    @ColumnInfo
    public int score;

    @ColumnInfo
    public int lowestScore;

    @ColumnInfo
    public Long lowestScoreTimestamp;

    @ColumnInfo
    public int currentMaxConsecutiveCorrectGuesses;

    @ColumnInfo
    public Long lastCorrectGuessTimestamp;


    /*public Answer withType(AnswerType type, AnswerSubtype subtype) {
        this.type = type.ordinal();
        this.subtype = subtype.ordinal();
        return this;
    }

    public Answer withDetail(int imageResId, String text, boolean plural) {
        this.imageResId = imageResId;
        this.text = text;
        this.plural = plural;
        return this;
    }*/
}
