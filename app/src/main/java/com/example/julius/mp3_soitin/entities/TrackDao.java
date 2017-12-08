package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TrackDao {
    @Query("SELECT * FROM track")
    List<Track> getAll();

    @Query("SELECT * FROM track WHERE id IN (:Ids)")
    List<Track> loadAllByIds(int[] Ids);

    @Query("SELECT * FROM track WHERE track_name = :track_name")
    Track findByName(String track_name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Track track);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Track... tracks);

    @Delete
    void delete(Track track);
}
