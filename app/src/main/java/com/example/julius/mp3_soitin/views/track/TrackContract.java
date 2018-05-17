package com.example.julius.mp3_soitin.views.track;

import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.data.entities.TrackContainer;
import com.example.julius.mp3_soitin.views.BasePresenter;
import com.example.julius.mp3_soitin.views.BaseView;
import com.example.julius.mp3_soitin.views.dialogs.DialogTrackPlaylistUseCase;

import java.util.List;

/**
 * Created by Julius on 20.2.2018.
 */

public interface TrackContract {

    interface View {
        void showTracks(List<Track> tracks);
        void setWindowName(String newName);
        void resetWindowName();
        void setPresenter(Presenter presenter);
        void showDialog(DialogTrackPlaylistUseCase usecase, Track track, int mode);
    }

    interface Presenter extends BasePresenter<TrackContainer>{
        void openTrack(int index);
        void longPressedTrack(int index, int mode);
        void setTracks(List<Track> persistedTracks);
        void setContainer(TrackContainer container);
        TrackContainer getContainer();
        boolean shouldUsePersistedData();
    }

}
