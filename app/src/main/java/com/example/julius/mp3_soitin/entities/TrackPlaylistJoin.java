package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by Julius on 4.12.2017.
 */
@Entity(tableName = "track_playlist_join",
        primaryKeys = { "trackId", "playlistId" },
        foreignKeys = {//track = track , genre = playlist
                @ForeignKey(entity = Track.class,
                        parentColumns = "id",
                        childColumns = "trackId"),
                @ForeignKey(entity = PlayList.class,
                        parentColumns = "id",
                        childColumns = "playlistId")
        })
public class TrackPlaylistJoin{
    public final long trackId;
    public final long playlistId;

    public TrackPlaylistJoin(long trackId, long playlistId) {
        this.trackId = trackId;
        this.playlistId = playlistId;
    }
}
