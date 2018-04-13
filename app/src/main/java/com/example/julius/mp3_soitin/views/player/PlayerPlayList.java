package com.example.julius.mp3_soitin.views.player;

import com.example.julius.mp3_soitin.data.entities.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple data structure which mp3 player presenter uses to cycle tracks
 */

public class PlayerPlayList {
    private String name;
    private List<Track> tracks;
    private int index = 0;

    public PlayerPlayList(String name, List<Track> tracks) {
        this.name = name;
        this.tracks = tracks;
    }

    public Track getNextTrack(){
        if(tracks.isEmpty())
            return null;
        index++;
        if(index>=tracks.size())
            index = 0;
        return tracks.get(index);
    }

    public Track getPreviousTrack(){
        if(tracks.isEmpty())
            return null;
        index--;
        if(index<0)
            index = tracks.size()-1;
        return tracks.get(index);
    }

    public Track getCurrentTrack(){
        if(tracks.isEmpty())
            return null;
        if(index>=0 && index<tracks.size())
            return tracks.get(index);
        index = 0;
        return tracks.get(index);
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int size(){
        return tracks.size();
    }

    public String getName(){
        return name;
    }
}
