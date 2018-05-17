package com.example.julius.mp3_soitin.views.player;

import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.views.BasePresenter;

import java.util.List;

/**
 * Created by Julius on 20.2.2018.
 */

public interface PlayerContract {
    interface View{
        void setPresenter(Presenter presenter);
        void setCurrentTimeText(double currentTime);
        void setEndTimeText(double endTime);
        void setCurrentTimeSeekBar(double currentTime);
        void setEndTimeSeekBar(double endTime);
        void setTitleText(String title);
        void setToPlayStatus();
        void setToPauseStatus();
        void notifyUser(String msg);
        void disableButtons();
        //void updateView(double currentTime);
    }

    interface Presenter extends BasePresenter<PlayerPlayList> {
        /**
         * Tells the presenter to load the given track
         * @param track
         */
        void loadTrack(Track track);
        /*
        *When user presses the seekbar jump to the user specified location.
        *Presenter will also update the view.
         */
        void jumpTo(double time);

        void play();
        /**
         * If playing music return true otherwise false
         * @return
         */
        boolean isPlaying();

        /**
         * Stop playing music
         */
        void pausePlaying();

        void nextSong();

        void previousSong();
    }
}
