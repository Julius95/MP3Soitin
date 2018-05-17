package com.example.julius.mp3_soitin.views.player;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.example.julius.mp3_soitin.FragmentSwitcher;
import com.example.julius.mp3_soitin.data.entities.Track;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * Created by Julius on 20.2.2018.
 */

public class PlayerPresenter implements PlayerContract.Presenter {

    private PlayerContract.View playerView;

    private FragmentSwitcher mainactivity;

    private PlayerPlayList playlist;

    private Track track;

    private final MediaPlayer mediaPlayer = new MediaPlayer();

    private double currentTime = 0;
    private double totalTime = 0;

    private int forwardTime = 5000;
    private int backwardTime = 5000;

    private Handler myHandler;

    private volatile boolean update;

    private boolean active = false;

    private Runnable UpdateSongTime;

    public PlayerPresenter(PlayerContract.View playerView, FragmentSwitcher mainactivity) {
        this.playerView = playerView;
        playerView.setPresenter(this);
        this.mainactivity = mainactivity;
        update = false;
        //setUpHandler();
    }

    @Override
    public void start() {
        active = true;
        if(track==null){
            playerView.notifyUser("NO TRACK SELECTED!");
            playerView.disableButtons();
            playerView.setTitleText("NO TRACK SELECTED");
            return;
        }
        totalTime = mediaPlayer.getDuration();
        currentTime = mediaPlayer.getCurrentPosition();//Kun mediaplyer reset tämä on taas nolla
        update = true;
        refreshView();
        if(UpdateSongTime == null) {
            UpdateSongTime = newRunnable();
            new Thread(UpdateSongTime).start();
        }
        //if(myHandler!=null)
          //  myHandler.postDelayed(UpdateSongTime,100);
        if(isPlaying())
            playerView.setToPlayStatus();
        else
            playerView.setToPauseStatus();
    }

    @Override
    public void stop() {
        update = active = false;
        UpdateSongTime = null;
        //myHandler.removeCallbacks(UpdateSongTime);
    }

    @Override
    public void setContentByHelperObject(PlayerPlayList playerPlayList) {
        playlist = playerPlayList;
        track = playlist.getCurrentTrack();
        Log.d("UUUU", "RECEIVED TRACK " + track.getName());
        mountSong();
    }


    @Override
    public void setContentByID(int id) {

    }

    @Override
    public void refresh() {
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public void loadTrack(Track track) {

    }

    @Override
    public void jumpTo(double time) {
        mediaPlayer.seekTo((int)time);
        playerView.setCurrentTimeText((int) time);
        playerView.setCurrentTimeSeekBar((int) time);
    }

    @Override
    public void play() {
        mediaPlayer.start();
        playerView.setToPlayStatus();
        playerView.setCurrentTimeSeekBar(currentTime);
        playerView.setEndTimeSeekBar(totalTime);
        update = true;
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void pausePlaying() {
        mediaPlayer.pause();
        playerView.setToPauseStatus();
    }

    @Override
    public void nextSong() {
        if(playlist!=null){
            track = playlist.getNextTrack();
            mountSong();
            refreshView();
            play();
        }
    }

    @Override
    public void previousSong() {
        if(playlist!=null){
            track = playlist.getPreviousTrack();
            mountSong();
            refreshView();
            play();
        }
    }

    private Runnable newRunnable(){
        return () ->{
            while(update){
                Log.d("UUUU", Thread.currentThread().getName());
                currentTime = mediaPlayer.getCurrentPosition();
                mainactivity.uiUpdate(()->{
                    playerView.setCurrentTimeText(currentTime);
                    playerView.setCurrentTimeSeekBar(currentTime);
                });
            }
        };
    }

    private void refreshView(){
        playerView.setCurrentTimeText(currentTime);
        playerView.setEndTimeSeekBar(totalTime);
        playerView.setCurrentTimeSeekBar(currentTime);
        playerView.setEndTimeText(totalTime);
        playerView.setTitleText(track.getName());
    }

    private boolean mountSong(){
        try {
            mediaPlayer.reset(); //Oleellinen
            Log.d("UUUU" , "Path : " + track.getPath());
            mediaPlayer.setDataSource(track.getPath());
            mediaPlayer.prepare();
            totalTime = mediaPlayer.getDuration();
            currentTime = mediaPlayer.getCurrentPosition();//Kun mediaplyer reset tämä on taas nolla
            //mediaPlayer.start();
        } catch (IOException e) {
            Log.d("UUUU", "Virhe " + e.getMessage());
            return false;
        }
        return true;
    }

    private void setUpHandler(){
        if(myHandler == null){
            Log.d("UUUU", "SET UP HANDELER");
            myHandler = new Handler();
            //handlerCreated = true;
            //myHandler.postDelayed(UpdateSongTime,100);
        }
    }
}
