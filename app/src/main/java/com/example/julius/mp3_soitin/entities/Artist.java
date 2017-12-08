package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Julius on 2.12.2017.
 */
@Entity(tableName = "artist")
public class Artist {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "artist_name")
    private String name;

    @Ignore
    public Artist(String name) {
        this.name = name;
    }

    public Artist() {}

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
