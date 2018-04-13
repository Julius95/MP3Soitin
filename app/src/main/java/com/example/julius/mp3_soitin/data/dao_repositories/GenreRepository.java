package com.example.julius.mp3_soitin.data.dao_repositories;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.Genre;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Julius on 25.3.2018.
 */

public class GenreRepository implements Repository<Genre> {

    private AppDatabase db;

    public GenreRepository(AppDatabase db){
        this.db = db;
    }

    @Override
    public void getAll(Consumer<List<Genre>> callback) {

    }

    @Override
    public void get(int id, Consumer<Genre> callback) {

    }

    @Override
    public void update(Genre genre) {

    }

    @Override
    public Genre findByName(String name) {
        return db.genreDao().findByName(name);
    }

    @Override
    public int delete(Genre genre) {
        return 0;
    }

    @Override
    public void deleteAsync(Genre genre) {

    }

    @Override
    public void add(Genre genre, Consumer<Long> callback) {
    }

    @Override
    public long add(Genre genre) {
        return db.genreDao().insert(genre);
    }

    @Override
    public void conversion(Function<AppDatabase, List<Genre>> conversionFunction, Consumer<List<Genre>> callback) {

    }
}
