package com.example.julius.mp3_soitin;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

import com.example.julius.mp3_soitin.entities.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Julius on 28.11.2017.
 */
//https://stackoverflow.com/questions/11327954/how-to-extract-metadata-from-mp3
public class SongsManager {
    final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private String mp3Pattern = ".mp3";
    private Context context;
    private MediaMetadataRetriever mmr;

    // Constructor
    public SongsManager() {
        mmr = new MediaMetadataRetriever();
    }

    /**
     * Function to read all mp3 files and store the details in
     * ArrayList
     * */
    public List<File> getPlayList() {
        String state = Environment.getExternalStorageState();
        List<File> res = new ArrayList<File>();
        try {
            //File rootFolder = new File(Environment.getExternalStorageDirectory().toString() + "/Music");
            File rootFolder = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if(file.getName().endsWith(".mp3")) {
                    mmr.setDataSource(file.getAbsolutePath());
                    String buf = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
                    String buf2 = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
                    Log.d("UUUU", "Adding Track " + buf2 + buf + file.getName() + " " + file.getAbsolutePath());
                    res.add(file);
                }
            }
        } catch (Exception e) {
            Log.d("UUUU", "ERROR " + e.getMessage());
            res.clear();
        }
        return res;
    }

    public boolean isExternalStorageReadable(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }

    private void addSongToList(File song) {
        if (song.getName().endsWith(mp3Pattern)) {
            HashMap<String, String> songMap = new HashMap<String, String>();
            songMap.put("songTitle",
                    song.getName().substring(0, (song.getName().length() - 4)));
            songMap.put("songPath", song.getPath());

            // Adding each song to SongList
            songsList.add(songMap);
        }
    }
}
