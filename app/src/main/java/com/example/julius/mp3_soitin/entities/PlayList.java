package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.TrackListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    @Override
    public String toString(){
        return name;
    }

    public static Function<Void, List<Object>> getAllPlayListsFromDB(AppDatabase db){
        return (Void v) -> {
            List<Object> playlists = new ArrayList<Object>();
            Log.d("UUUU", "Fetching from db");
            playlists.addAll(db.playListDao().getAll());
            for(Object t : playlists){
                PlayList a = (PlayList)t;
                Log.d("UUUU","PlayList " + a.getName());
            }
            return playlists;
        };
    }

    public static Function<Void, Object> savePlayListToDB(AppDatabase db, PlayList playList){
        return (Void v) -> {
            Log.d("UUUU", "saving to database db");
            playList.setId(db.playListDao().insert(playList));
            return playList;
        };
    }

}
