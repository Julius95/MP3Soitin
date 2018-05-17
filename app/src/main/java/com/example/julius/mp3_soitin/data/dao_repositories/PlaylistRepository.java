package com.example.julius.mp3_soitin.data.dao_repositories;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.PlayList;
import com.example.julius.mp3_soitin.data.entities.TrackPlaylistJoin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Julius on 18.3.2018.
 */

public class PlaylistRepository implements Repository<PlayList> {

    private AppDatabase db;

    public PlaylistRepository(AppDatabase db){
        this.db = db;
    }

    @Override
    public void getAll(Consumer<List<PlayList>> callback) {
        CompletableFuture.supplyAsync(() -> db.playListDao().getAll()).thenAccept(callback);
    }

    @Override
    public void get(int id, Consumer<PlayList> callback) {

    }

    @Override
    public void update(PlayList playList) {

    }

    @Override
    public PlayList findByName(String name) {
        return null;
    }

    @Override
    public int delete(PlayList playList) {
        return db.playListDao().delete(playList);
    }

    @Override
    public void delete(PlayList playList, Consumer<Integer> callback) {
        CompletableFuture.supplyAsync(() -> db.playListDao().delete(playList)).thenAccept(callback);
    }

    @Override
    public void deleteAsync(PlayList playList) {
        CompletableFuture.runAsync(() -> db.playListDao().delete(playList));
    }

    @Override
    public void add(PlayList playList, Consumer<Long> callback) {
        CompletableFuture.supplyAsync(() -> db.playListDao().insert(playList)).thenAccept(callback);
    }

    @Override
    public long add(PlayList playList) {
        return 0;
    }

    @Override
    public void conversion(Function<AppDatabase, List<PlayList>> conversionFunction, Consumer<List<PlayList>> callback) {

    }


}
