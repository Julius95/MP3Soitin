package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Julius on 2.12.2017.
 */

@Dao
public interface AlbumDao {
    @Query("SELECT * FROM album")
    List<Album> getAll();

    @Query("SELECT * FROM album WHERE id IN (:Ids)")
    List<Album> loadAllByIds(int[] Ids);

    @Query("SELECT * FROM album WHERE album_name LIKE :album_name LIMIT 1")
    Album findByName(String album_name);

    @Insert
    long insert(Album albums);

    @Insert
    long[] insertAll(Album... albums);

    @Update
    public void updateAlbums(Album album);

    @Delete
    void delete(Album album);

    @Transaction
    @Query("SELECT * FROM album")
    public List<AlbumWithTracks> loadAlbumsWithTracks();

    @Transaction
    @Query("SELECT * FROM album WHERE id = :id")
    public AlbumWithTracks loadAlbumWithTracks(long id);

}
