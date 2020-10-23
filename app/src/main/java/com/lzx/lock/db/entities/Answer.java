package com.lzx.lock.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Answer {
    @PrimaryKey
    public int uid;

    @ColumnInfo
    public int type;

    @ColumnInfo
    public int subtype;

    @ColumnInfo
    public int imageResId;

    @ColumnInfo
    public String text;

    @ColumnInfo
    public boolean plural;

    public Answer withType(AnswerType type, AnswerSubtype subtype) {
        this.type = type.ordinal();
        this.subtype = subtype.ordinal();
        return this;
    }

    public Answer withDetail(int imageResId, String text, boolean plural) {
        this.imageResId = imageResId;
        this.text = text;
        this.plural = plural;
        return this;
    }
}