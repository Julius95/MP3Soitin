package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Julius on 4.12.2017.
 */
@Dao
public interface PlayListDao {
    @Query("SELECT * FROM playlist")
    List<PlayList> getAll();

    @Query("SELECT * FROM playlist WHERE id IN (:Ids)")
    List<PlayList> loadAllByIds(long[] Ids);

    @Query("SELECT * FROM playlist WHERE name LIKE :playlist_name LIMIT 1")
    PlayList findByName(String playlist_name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PlayList pl);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(PlayList... pls);

    @Delete()
    void delete(PlayList pl);
}
