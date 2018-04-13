package com.example.julius.mp3_soitin;

/**
 * Created by Julius on 20.2.2018.
 */

public enum FragmentType {
    Tracks,
    Albums,
    Playlist,
    Player;

    private final static FragmentType[] list = FragmentType.values();

    public static FragmentType getType(int i) {
        return list[i];
    }

    public static int toInt(FragmentType fragmenttype){
        for(int i = 0; i<list.length ; i++){
            if(list[i] == fragmenttype)
                return i;
        }
        return -1;
    }

    public static int size() {
        return list.length;
    }
}
