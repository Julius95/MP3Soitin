package com.example.julius.mp3_soitin.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Julius on 4.12.2017.
 */
@Entity(tableName = "genre")
public class Genre {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "genre_name")
    private String name;

    @Ignore
    public Genre(String name) {
        this.name = name;
    }

    public Genre(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
