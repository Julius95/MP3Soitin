package com.example.julius.mp3_soitin.views.album;

import android.util.Log;

import com.example.julius.mp3_soitin.FragmentSwitcher;
import com.example.julius.mp3_soitin.FragmentType;
import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.Album;
import com.example.julius.mp3_soitin.data.entities.ArtistWithAlbums;
import com.example.julius.mp3_soitin.data.entities.TrackContainer;
import com.example.julius.mp3_soitin.views.BasePresenter;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Julius on 20.2.2018.
 */

public class AlbumPresenter implements AlbumContract.Presenter {

    private Repository<Album> dataSource;

    private AlbumContract.View trackView;

    private FragmentSwitcher mainactivity;

    private boolean active;

    public AlbumPresenter(Repository<Album> dataSource, AlbumContract.View trackView, FragmentSwitcher mainactivity) {
        this.dataSource = dataSource;
        this.trackView = trackView;
        trackView.setPresenter(this);
        this.mainactivity = mainactivity;
    }

    @Override
    public void start() {
        active = true;
        dataSource.getAll((List<Album> albums) -> mainactivity.uiUpdate(() -> trackView.showAlbums(albums)));
    }

    @Override
    public void stop() {
        active = false;
    }

    @Override
    public void selectAlbum(Album album) {
        mainactivity.switchTo(FragmentType.Tracks, (BasePresenter s) -> s.setContentByHelperObject(album));
    }

    @Override
    public void setContentByHelperObject(ArtistWithAlbums artistWithAlbums) {

    }

    @Override
    public void setContentByID(int id) {

    }

    @Override
    public void refresh() {
        start();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
