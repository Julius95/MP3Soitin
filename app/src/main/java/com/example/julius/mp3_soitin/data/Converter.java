package com.example.julius.mp3_soitin.data;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.data.entities.Album;
import com.example.julius.mp3_soitin.data.entities.PlayList;
import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.data.entities.TrackContainer;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Julius on 23.3.2018.
 */

public class Converter {

    private Converter(){}

    public static Function<AppDatabase, List<Track>> getTracksFromPlaylist(PlayList playlist){
        return (AppDatabase db) -> db.track_playList_JOIN_Dao().getTracksFromPlayList(playlist.getId());
    }

    public static Function<AppDatabase, List<Track>> getTracksFromTrackAlbum(Album album){
        return (AppDatabase db) -> db.albumDao().loadAlbumWithTracks(album.getId()).tracks;
    }
}
