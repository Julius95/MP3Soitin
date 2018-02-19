package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

/**
 * Created by Julius on 2.12.2017.
 */
@Dao
public interface ArtistDao {
    @Query("SELECT * FROM artist")
    List<Artist> getAll();

    @Query("SELECT * FROM artist WHERE id IN (:Ids)")
    List<Artist> loadAllByIds(int[] Ids);

    @Query("SELECT * FROM artist WHERE artist_name = :artist_name")
    Artist findByName(String artist_name);

    @Insert
    long insert(Artist artist);

    @Insert
    long[] insertAll(Artist... artists);

    @Delete
    void delete(Artist artist);

    @Transaction
    @Query("SELECT * FROM artist")
    List<ArtistWithAlbums> loadAllArtistsWithAlbums();

    @Transaction
    @Query("SELECT * FROM artist WHERE id = :id")
    List<ArtistWithAlbums> loadArtistsWithAlbums(long id);
}
