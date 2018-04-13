package com.example.julius.mp3_soitin.data.dao_repositories;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.AlbumWithTracks;
import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.data.entities.TrackContainer;
import com.example.julius.mp3_soitin.data.entities.TrackPlaylistJoin;
import com.example.julius.mp3_soitin.views.track.TrackListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Julius on 19.2.2018.
 */

public class TrackRepository implements Repository<Track> {
    private AppDatabase db;

    public TrackRepository(AppDatabase db){
        this.db = db;
    }

    @Override
    public void getAll(Consumer<List<Track>> callback) {
        CompletableFuture.supplyAsync(()-> db.trackDao().getAll()).thenAccept(callback);
    }

    @Override
    public void get(int id, Consumer<Track> callback) {
        CompletableFuture.supplyAsync(() -> db.trackDao().loadById(id)).thenAccept(callback);
    }

    @Override
    public void update(Track track) {

    }

    @Override
    public Track findByName(String name) {
        return db.trackDao().findByName(name);
    }

    @Override
    public int delete(Track track) {
        return db.trackDao().delete(track);
    }

    @Override
    public void deleteAsync(Track track) {

    }

    @Override
    public void add(Track track, Consumer<Long> callback) {
        CompletableFuture.supplyAsync(() -> db.trackDao().insert(track)).thenAccept(callback);
    }

    @Override
    public long add(Track track) {
        return db.trackDao().insert(track);
    }

    @Override
    public void conversion(Function<AppDatabase, List<Track>> conversionFunction, Consumer<List<Track>> callback) {
        CompletableFuture.supplyAsync(() -> conversionFunction.apply(db)).thenAccept(callback);
    }


}
