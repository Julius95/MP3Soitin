package com.example.julius.mp3_soitin.data.dao_repositories;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.Album;
import com.example.julius.mp3_soitin.data.entities.ArtistWithAlbums;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Julius on 23.2.2018.
 */

public class AlbumRepository implements Repository<Album> {

    private AppDatabase db;

    public AlbumRepository(AppDatabase db){
        this.db = db;
    }

    @Override
    public void getAll(Consumer<List<Album>> callback) {
        CompletableFuture.supplyAsync(() -> db.albumDao().getAll()).thenAccept(callback);
    }

    @Override
    public void get(int id, Consumer<Album> callback) {

    }

    @Override
    public void update(Album album) {
        db.albumDao().updateAlbums(album);
    }

    @Override
    public Album findByName(String name) {
        return db.albumDao().findByName(name);
    }

    @Override
    public int delete(Album album) {
        return db.albumDao().delete(album);
    }

    @Override
    public void delete(Album album, Consumer<Integer> callback) {

    }

    @Override
    public void deleteAsync(Album album) {

    }

    @Override
    public void add(Album album, Consumer<Long> callback) {

    }

    @Override
    public long add(Album album) {
        return db.albumDao().insert(album);
    }

    @Override
    public void conversion(Function<AppDatabase, List<Album>> conversionFunction, Consumer<List<Album>> callback) {

    }


}
