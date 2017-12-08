package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Julius on 4.12.2017.
 */
@Dao
public interface TracksGenreJoinDao {
    @Insert
    void insert(TracksGenreJoin tracksGenreJoin);

    @Query("SELECT * FROM track INNER JOIN track_genre_join ON track.id=track_genre_join.trackId WHERE track_genre_join.genreId=:genreId")
    List<Track> getTracksForGenres(final long genreId);

    @Query("SELECT * FROM genre INNER JOIN track_genre_join ON genre.id=track_genre_join.genreId WHERE track_genre_join.trackId=:trackId")
    List<Genre> getGenresForTracks(final long trackId);
}
