package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.util.Log;

import com.example.julius.mp3_soitin.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    public static Function<Void, Void> saveTrackPlayListJoinToDB(AppDatabase db, TrackPlaylistJoin tpj){
        return (Void v) -> {
            db.track_playList_JOIN_Dao().insert(tpj);
            return null;
        };
    }

    public static Function<Void, Void> deleteTrackPlayListJoin(AppDatabase db, Track track, PlayList playlist){
        return (Void v) -> {
            Log.d("UUUU" , "HAETTIIN " + db.track_playList_JOIN_Dao().get(track.getId(), playlist.getId()).toString());
            db.track_playList_JOIN_Dao().delete(track.getId(), playlist.getId());
            return null;
        };
    }

    public static Function<Void, List<Object>> getAcceptablePlaylistsForTrack(AppDatabase db, Track track){
        return (Void v) ->{
            List<Object> res = new ArrayList<Object>();
            res.addAll(db.track_playList_JOIN_Dao().getPlayListsThatCanAcceptThisTrack(track.getId()));
            return res;
        };
    }

    public static Function<Void, List<Object>> getPlaylistsThatIncludeTrack(AppDatabase db, Track track){
        return (Void v) ->{
            List<Object> res = new ArrayList<Object>();
            res.addAll(db.track_playList_JOIN_Dao().getPlayListsThatIncludeThisTrack(track.getId()));
            return res;
        };
    }

}
