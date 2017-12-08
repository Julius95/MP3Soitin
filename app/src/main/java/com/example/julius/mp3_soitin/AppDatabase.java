package com.example.julius.mp3_soitin;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.julius.mp3_soitin.entities.Album;
import com.example.julius.mp3_soitin.entities.AlbumDao;
import com.example.julius.mp3_soitin.entities.Artist;
import com.example.julius.mp3_soitin.entities.ArtistDao;
import com.example.julius.mp3_soitin.entities.Genre;
import com.example.julius.mp3_soitin.entities.GenreDao;
import com.example.julius.mp3_soitin.entities.PlayList;
import com.example.julius.mp3_soitin.entities.PlayListDao;
import com.example.julius.mp3_soitin.entities.Track;
import com.example.julius.mp3_soitin.entities.TrackDao;
import com.example.julius.mp3_soitin.entities.TrackPlaylistJoin;
import com.example.julius.mp3_soitin.entities.TrackPlaylistJoinDao;
import com.example.julius.mp3_soitin.entities.TracksGenreJoin;
import com.example.julius.mp3_soitin.entities.TracksGenreJoinDao;

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

    public static AppDatabase getInstance(Context context){
        if(db == null) {
            db = Room.databaseBuilder(context,
            AppDatabase.class, "database-name").fallbackToDestructiveMigration().build();
        }
        return db;
    }
}
