package com.example.julius.mp3_soitin.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by Julius on 4.12.2017.
 * MANY TO MANY SUHDELUOKKA
 */
@Entity(tableName = "track_genre_join",
        primaryKeys = { "trackId", "genreId" },
        foreignKeys = {//track = user , genre = repo
                @ForeignKey(entity = Track.class,
                        parentColumns = "id",
                        childColumns = "trackId"),
                @ForeignKey(entity = Genre.class,
                        parentColumns = "id",
                        childColumns = "genreId")
        })
public class TracksGenreJoin {
    public final long trackId;
    public final long genreId;

    public TracksGenreJoin(long trackId, long genreId) {
        this.trackId = trackId;
        this.genreId = genreId;
    }
}
