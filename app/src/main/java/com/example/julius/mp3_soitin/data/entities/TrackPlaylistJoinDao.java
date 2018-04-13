package com.example.julius.mp3_soitin.data.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Julius on 4.12.2017.
 */
@Dao
public interface TrackPlaylistJoinDao {
    @Insert
    void insert(TrackPlaylistJoin trackPlaylistJoin);

    @Delete
    void delete(TrackPlaylistJoin trackPlaylistJoin);

    @Query("SELECT * FROM track_playlist_join")
    List<TrackPlaylistJoin> getAll();

    @Query("SELECT * FROM track_playlist_join WHERE track_playlist_join.trackId = :trackId AND track_playlist_join.playlistId = :playlistId")
    List<TrackPlaylistJoin> get(final long trackId, final long playlistId);

    @Query("DELETE FROM track_playlist_join WHERE track_playlist_join.trackId = :trackId AND track_playlist_join.playlistId = :playlistId")
    void delete(final long trackId, final long playlistId);

    @Query("SELECT * FROM track INNER JOIN track_playlist_join ON track.id=track_playlist_join.trackId WHERE track_playlist_join.playlistId=:playlistId")
    List<Track> getTracksFromPlayList(final long playlistId);

    @Query("SELECT * FROM playlist INNER JOIN track_playlist_join ON playlist.id=track_playlist_join.playlistId WHERE track_playlist_join.trackId=:trackId")
    List<PlayList> getPlayListsThatIncludeThisTrack(final long trackId);

    //SELECT * FROM Table1 WHERE id NOT IN (SELECT id FROM Table2)
    @Query("SELECT * FROM playlist WHERE playlist.id NOT IN (SELECT track_playlist_join.playlistId FROM track_playlist_join WHERE track_playlist_join.trackId=:trackId)")
    List<PlayList> getPlayListsThatCanAcceptThisTrack(final long trackId);
}
