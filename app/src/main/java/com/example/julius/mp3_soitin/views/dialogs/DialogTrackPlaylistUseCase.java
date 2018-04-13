package com.example.julius.mp3_soitin.views.dialogs;


import com.example.julius.mp3_soitin.data.entities.PlayList;
import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.data.entities.TrackPlaylistJoin;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Julius on 22.3.2018.
 */

public interface DialogTrackPlaylistUseCase extends Serializable{
    void getPlayListsThatCanAcceptThisTrack(Consumer<List<PlayList>> callback, Track track);
    void getPlaylistsThatIncludeTrack(Consumer<List<PlayList>> callback, Track track);
    void saveTrackToPlaylist(Track track, PlayList playlist);
    void deleteTrackFromPlaylist(Track track, PlayList playlist);
}
