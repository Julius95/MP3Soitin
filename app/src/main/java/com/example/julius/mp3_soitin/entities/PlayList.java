package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.julius.mp3_soitin.TrackListFragment;

/**
 * Created by Julius on 4.12.2017.
 */
@Entity(tableName = "playlist")
public class PlayList implements TrackContainer{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @Ignore
    public PlayList(String name) {
        this.name = name;
    }

    public PlayList(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public TrackListFragment.IdType getType() {
        return TrackListFragment.IdType.Playlist;
    }

    public void setName(String name) {
        this.name = name;
    }

}
