package com.example.julius.mp3_soitin.filescanning;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.Album;
import com.example.julius.mp3_soitin.data.entities.Artist;
import com.example.julius.mp3_soitin.data.entities.Genre;
import com.example.julius.mp3_soitin.data.entities.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Julius on 28.11.2017.
 */
//https://stackoverflow.com/questions/11327954/how-to-extract-metadata-from-mp3
public class MusicFileScanner extends AsyncTask< Void, Void, ScanResult> {
    //final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private String mp3Pattern = ".mp3";
    private MediaMetadataRetriever mmr;
    private Consumer<ScanResult> callback;

    //Database access objects. Each object represents a table.
    private Repository<Track> trackrepo;
    private Repository<Album> albumrepo;
    private Repository<Genre> genrerepo;
    private Repository<Artist> artistrepo;

    // Constructor
    public MusicFileScanner(Consumer<ScanResult> callback, Repository<Track> trackrepo, Repository<Album> albumrepo, Repository<Genre> genrerepo, Repository<Artist> artistrepo) {
        this.callback = callback;
        this.trackrepo = trackrepo;
        this.albumrepo = albumrepo;
        this.genrerepo = genrerepo;
        this.artistrepo = artistrepo;
        mmr = new MediaMetadataRetriever();
    }

    public MusicFileScanner(MediaMetadataRetriever mmr, Consumer<ScanResult> callback, Repository<Track> trackrepo, Repository<Album> albumrepo, Repository<Genre> genrerepo, Repository<Artist> artistrepo) {
        this.mmr = mmr;
        this.callback = callback;
        this.trackrepo = trackrepo;
        this.albumrepo = albumrepo;
        this.genrerepo = genrerepo;
        this.artistrepo = artistrepo;
    }

    @Override
    public ScanResult doInBackground(Void... voids) {
        int goodReads = 0, badReads = 0;
        List<BadFile> badfiles = new ArrayList<>();
        List<File> musicFiles = getPlayList();
        System.out.println("SIZE " + musicFiles.size());
        Genre genre;
        Artist artist;
        Album album;
        Track track;
        for(File file : musicFiles){
            mmr.setDataSource(file.getAbsolutePath());
            //Log.d("UUUU", "Inserting track " + t.getName() + " to database");
            //Genre
            //Artist
            String bufArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
            String bufAlbum = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String bufTrack = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String bufGenre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            System.out.println("GOT FROM MOCKS " + bufAlbum + " " + bufTrack);
            if(bufArtist == null || bufArtist.length()<1 ||
                bufAlbum == null || bufAlbum.length()<1 ||
                bufTrack == null || bufTrack.length()<1 ||
                bufGenre == null || bufGenre.length()<1){
                badfiles.add(new BadFile(bufTrack, bufAlbum, bufArtist, bufGenre, file.getAbsolutePath()));
                badReads++;
                continue;
            }
            genre = genrerepo.findByName(bufGenre);
            if(genre == null){
                Log.d("UUUU", "Inserting new Genre " + bufGenre);
                genre = new Genre(bufGenre);
                genre.setId(genrerepo.add(genre));
            }
            artist = artistrepo.findByName(bufArtist);
            if(artist == null){
                Log.d("UUUU", "Inserting new Artist " + bufArtist);
                artist = new Artist(bufArtist);
                artist.setId(artistrepo.add(artist));
            }
            //Album
            album = albumrepo.findByName(bufAlbum);
            if(album == null){
                Log.d("UUUU", "Inserting new Album " + bufAlbum);
                album = new Album(bufAlbum, "lolDatewhocares", 1, artist.getId());//String name, String Date, int nmr_of_tracks, long artistId
                album.setId(albumrepo.add(album));
            }else{
                album.setNmr_of_tracks(album.getNmr_of_tracks()+1);
                albumrepo.update(album);
            }
            //Track
            track = trackrepo.findByName(bufTrack);//Palauttaa NULL jos ei ole tietokannassa
            if(track == null){
                Log.d("UUUU", "Inserting Track " + bufTrack);
                trackrepo.add(new Track(bufTrack, file.getAbsolutePath(),Math.round(Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))/1000) ,album.getId()));
            }else{
                //TODO jos löytyy jo valmiiksi raita niin mitäs sitten?
                Log.d("UUUU", "EI OLE NULL " + track.getName());
            }
            goodReads++;
            //Log.d("UUUU", "Pituus " + Math.round(Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))/1000));
        }
        return new ScanResult(goodReads, badReads, badfiles);
    }

    @Override
    protected void onProgressUpdate(Void... voids) {
        super.onProgressUpdate();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // This runs in UI when background thread finishes
    @Override
    protected void onPostExecute(ScanResult result) {
        //Call callback here
        System.out.println("IS NULL " + callback != null ? " NOPE " : " YES :(");
        if(callback!=null)
            callback.accept(result);
        super.onPostExecute(result);
        // Do things like hide the progress bar or change a TextView
    }

    /**
     * Function to read all mp3 files and store the details in
     * ArrayList
     * */
    public List<File> getPlayList() {
        //String state = Environment.getExternalStorageState();
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
