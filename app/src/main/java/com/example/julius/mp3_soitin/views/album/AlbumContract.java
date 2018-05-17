package com.example.julius.mp3_soitin.views.album;

import com.example.julius.mp3_soitin.data.entities.Album;
import com.example.julius.mp3_soitin.data.entities.ArtistWithAlbums;
import com.example.julius.mp3_soitin.views.BasePresenter;

import java.util.List;

/**
 * Created by Julius on 20.2.2018.
 */

public interface AlbumContract {
    interface View{
        void showAlbums(List<Album> albums);
        void setPresenter(AlbumContract.Presenter presenter);
    }

    interface Presenter extends BasePresenter<ArtistWithAlbums> {
        void selectAlbum(Album album);
    }
}
