package com.example.julius.mp3_soitin.views.playlist;

import com.example.julius.mp3_soitin.data.entities.PlayList;
import com.example.julius.mp3_soitin.views.BasePresenter;
import com.example.julius.mp3_soitin.views.player.PlayerPlayList;

import java.util.List;

/**
 * Created by Julius on 18.3.2018.
 */

public interface PlaylistContract {

    interface View{
        void showPlaylists(List<PlayList> playlists);
        void setPresenter(PlaylistContract.Presenter presenter);
        void addPlaylist(PlayList pl);
    }

    interface Presenter extends BasePresenter {
        void openPlaylist(PlayList pl);
        void deletePlaylist(PlayList playlist);
        void savePlaylist(String newPlaylistName);
    }
}
