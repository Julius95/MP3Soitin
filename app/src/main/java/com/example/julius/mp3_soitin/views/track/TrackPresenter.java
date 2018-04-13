package com.example.julius.mp3_soitin.views.track;

import com.example.julius.mp3_soitin.FragmentSwitcher;
import com.example.julius.mp3_soitin.FragmentType;
import com.example.julius.mp3_soitin.data.Converter;
import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.Album;
import com.example.julius.mp3_soitin.data.entities.PlayList;
import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.data.entities.TrackContainer;
import com.example.julius.mp3_soitin.views.dialogs.DialogTrackPlaylistUseCase;
import com.example.julius.mp3_soitin.views.player.PlayerPlayList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Julius on 19.2.2018.
 */

public class TrackPresenter implements TrackContract.Presenter, FragmentSwitcher.Screen<TrackContainer>{

    private Repository<Track> dataSource;

    private TrackContract.View<TrackContract.Presenter> trackView;

    private FragmentSwitcher mainactivity;

    private TrackContainer container;

    private final List<Track> tracks = new ArrayList<>();

    private DialogTrackPlaylistUseCase usecase;

    private boolean active = false;

    public TrackPresenter(Repository<Track> dataSource, TrackContract.View<TrackContract.Presenter> trackView, FragmentSwitcher mainactivity, DialogTrackPlaylistUseCase usecase) {
        this.dataSource = dataSource;
        this.trackView = trackView;
        trackView.setPresenter(this);
        this.mainactivity = mainactivity;
        this.usecase = usecase;
    }

    @Override
    public void start() {
        active = true;
        if(container!=null){
            trackView.setWindowName(container.getName());
            Consumer<List<Track>> callback = (List<Track> result) ->
            mainactivity.uiUpdate(() -> {
                trackView.showTracks(result);
                tracks.clear();
                tracks.addAll(result);
            });
            if(container.getType() == TrackListFragment.IdType.Album)
                dataSource.conversion(Converter.getTracksFromTrackAlbum((Album) container), callback);
            else
                dataSource.conversion(Converter.getTracksFromPlaylist((PlayList) container), callback);
        }else{
            trackView.resetWindowName();
            dataSource.getAll((List<Track> result) -> {
                mainactivity.uiUpdate(() -> {
                    trackView.showTracks(result);
                    tracks.clear();
                    tracks.addAll(result);
                });
            });
        }
    }

    @Override
    public void stop() {
        container = null;
        active = false;
    }

    @Override
    public void openTrack(int index) {
        mainactivity.switchTo(FragmentType.Player, (FragmentSwitcher.Screen s)->{
            PlayerPlayList pl = new PlayerPlayList("Tracks", new ArrayList<>(tracks));
            pl.setIndex(index);
            s.setContentByHelperObject(pl);
        });
    }

    @Override
    public void longPressedTrack(int index, int mode) {
        Track track = tracks.get(index);
        trackView.showDialog(usecase, track, mode);
    }

    @Override
    public void setContentByHelperObject(TrackContainer trackContainer) {
        container = trackContainer;
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
