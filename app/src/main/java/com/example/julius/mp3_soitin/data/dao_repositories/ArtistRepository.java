package com.example.julius.mp3_soitin.data.dao_repositories;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.Artist;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Julius on 25.3.2018.
 */

public class ArtistRepository implements Repository<Artist> {

    private AppDatabase db;

    public ArtistRepository(AppDatabase db){
        this.db = db;
    }

    @Override
    public void getAll(Consumer<List<Artist>> callback) {

    }

    @Override
    public void get(int id, Consumer<Artist> callback) {

    }

    @Override
    public void update(Artist artist) {

    }

    @Override
    public Artist findByName(String name) {
        return db.artistDao().findByName(name);
    }

    @Override
    public int delete(Artist artist) {
        return 0;
    }

    @Override
    public void deleteAsync(Artist artist) {

    }

    @Override
    public void add(Artist artist, Consumer<Long> callback) {

    }

    @Override
    public long add(Artist artist) {
        return db.artistDao().insert(artist);
    }

    @Override
    public void conversion(Function<AppDatabase, List<Artist>> conversionFunction, Consumer<List<Artist>> callback) {

    }
}
