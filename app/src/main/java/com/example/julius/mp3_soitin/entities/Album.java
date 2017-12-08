package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.util.Log;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.TrackListFragment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Julius on 1.12.2017.
 */
@Entity(tableName = "album")
public class Album implements TrackContainer{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "album_name")
    private String name;

    @ColumnInfo(name = "album_date")
    private String pvm;

    @ColumnInfo(name = "nmr_of_tracks")
    private int nmr_of_tracks;

    @ColumnInfo(name = "artist_id")
    private long artist_id;

    @Ignore
    public Album(String name, String Date, int nmr_of_tracks, long artistId) {
        this.name = name;
        this.pvm = pvm;
        this.nmr_of_tracks = nmr_of_tracks;
        artist_id = artistId;
    }

    public Album(){}

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

    public String getPvm() {
        return pvm;
    }

    public void setPvm(String pvm) {
        this.pvm = pvm;
    }

    public int getNmr_of_tracks() {
        return nmr_of_tracks;
    }

    public void setNmr_of_tracks(int nmr_of_tracks) {
        this.nmr_of_tracks = nmr_of_tracks;
    }

    public long getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(long artist_id) {
        this.artist_id = artist_id;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public TrackListFragment.IdType getType() {
        return TrackListFragment.IdType.Album;
    }

    public static Function<Void, List<Object>> getAllAlbumsFromDB(AppDatabase db){
        Function<Void, List<Object>> f = (Void v) -> {
            List<Object> albums = new ArrayList<Object>();
            albums.addAll(db.albumDao().getAll());
            for(Object t : albums){
                Album a = (Album)t;
                Log.d("UUUU","Albumi " + a.getName());
            }
            return albums;
        };
        return f;
    }

}

