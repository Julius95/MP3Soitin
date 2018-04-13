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
public interface GenreDao {
    @Query("SELECT * FROM genre")
    List<Genre> getAll();

    @Query("SELECT * FROM genre WHERE id IN (:Ids)")
    List<Genre> loadAllByIds(int[] Ids);

    @Query("SELECT * FROM genre WHERE genre_name = :g_name LIMIT 1")
    Genre findByName(String g_name);

    @Insert
    long insert(Genre genre);

    @Insert
    long[] insertAll(Genre... genres);

    @Delete
    void delete(Genre genres);

}
