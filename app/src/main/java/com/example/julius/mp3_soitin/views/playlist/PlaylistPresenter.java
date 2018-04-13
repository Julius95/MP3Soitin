package com.example.julius.mp3_soitin.views.playlist;

import com.example.julius.mp3_soitin.FragmentSwitcher;
import com.example.julius.mp3_soitin.FragmentType;
import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.PlayList;

import java.util.List;

/**
 * Created by Julius on 18.3.2018.
 */

public class PlaylistPresenter implements PlaylistContract.Presenter, FragmentSwitcher.Screen<PlayList> {

    private Repository<PlayList> dataSource;

    private PlaylistContract.View playlistView;

    private FragmentSwitcher mainactivity;

    private boolean active = false;

    public PlaylistPresenter(Repository<PlayList> dataSource, PlaylistContract.View playlistView, FragmentSwitcher mainactivity) {
        this.dataSource = dataSource;
        this.playlistView = playlistView;
        playlistView.setPresenter(this);
        this.mainactivity = mainactivity;
    }

    @Override
    public void start() {
        active = true;
        dataSource.getAll((List<PlayList> playlists) -> mainactivity.uiUpdate(() -> playlistView.showPlaylists(playlists)));
    }

    @Override
    public void stop() {
        active = false;
    }

    @Override
    public void openPlaylist(PlayList pl) {
        mainactivity.switchTo(FragmentType.Tracks, (FragmentSwitcher.Screen screen)->{
            screen.setContentByHelperObject(pl);
        });
    }

    @Override
    public void deletePlaylist(PlayList playlist) {
        dataSource.delete(playlist);
    }

    @Override
    public void savePlaylist(String newPlaylistName) {
        PlayList pl = new PlayList(newPlaylistName);
        dataSource.add(pl,
            //Callback function
            (Long id) -> {
            pl.setId(id);
            mainactivity.uiUpdate(() -> playlistView.addPlaylist(pl));
        });
    }

    @Override
    public void setContentByHelperObject(PlayList playList) {

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
}
