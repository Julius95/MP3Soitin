package com.example.julius.mp3_soitin;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.julius.mp3_soitin.data.entities.Album;
import com.example.julius.mp3_soitin.data.entities.AlbumDao;
import com.example.julius.mp3_soitin.data.entities.Artist;
import com.example.julius.mp3_soitin.data.entities.ArtistDao;
import com.example.julius.mp3_soitin.data.entities.Genre;
import com.example.julius.mp3_soitin.data.entities.GenreDao;
import com.example.julius.mp3_soitin.data.entities.PlayList;
import com.example.julius.mp3_soitin.data.entities.PlayListDao;
import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.data.entities.TrackDao;
import com.example.julius.mp3_soitin.data.entities.TrackPlaylistJoin;
import com.example.julius.mp3_soitin.data.entities.TrackPlaylistJoinDao;
import com.example.julius.mp3_soitin.data.entities.TracksGenreJoin;
import com.example.julius.mp3_soitin.data.entities.TracksGenreJoinDao;

/**
 * Created by Julius on 1.12.2017.
 */
@Database(entities = {Track.class, Album.class, Artist.class, Genre.class,
        PlayList.class, TracksGenreJoin.class, TrackPlaylistJoin.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase db = null;
    public abstract TrackDao trackDao();
    public abstract AlbumDao albumDao();
    public abstract ArtistDao artistDao();
    public abstract GenreDao genreDao();
    public abstract PlayListDao playListDao();
    public abstract TrackPlaylistJoinDao track_playList_JOIN_Dao();
    public abstract TracksGenreJoinDao track_genre_JOIN_Dao();

    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context){
        synchronized (sLock) {
            if (db == null) {
                db = Room.databaseBuilder(context,
                        AppDatabase.class, "database-name").fallbackToDestructiveMigration().build();
            }
            return db;
        }
    }
}
