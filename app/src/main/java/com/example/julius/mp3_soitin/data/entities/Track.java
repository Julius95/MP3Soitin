package com.example.julius.mp3_soitin.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.views.track.TrackListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Julius on 30.11.2017.
 */
@Entity(tableName = "track")
public class Track implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "track_name")
    private String name;

    @ColumnInfo(name = "track_path")
    private String path;

    @ColumnInfo(name = "length")
    private int length;

    @ColumnInfo(name = "albumId")
    public long albumId; // Album id

    @Ignore
    public Track(String name, String path, int length) {
        this.name = name;
        this.path = path;
        this.length = length;
    }

    @Ignore
    public Track(String name, String path, int length, long albumId) {
        this.name = name;
        this.path = path;
        this.albumId = albumId;
        this.length = length;
        Log.d("UUUU", "PITUUS " + length);
    }

    public Track(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public int getLength() {return length;}

    public void setLength(int length) {this.length = length;}

    @Override
    public String toString(){
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.path);
        parcel.writeLong(this.albumId);
    }

    public Track(Parcel in) {
        id = in.readInt();
        name = in.readString();
        path = in.readString();
        albumId = in.readLong();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    /*public static AsyncTask<AppDatabase, Void, List<Track>> getAsyncLoad(){
        return new AsyncTask<AppDatabase, Void, List<Track>>() {
            @Override
            protected List<Track> doInBackground(AppDatabase... appDatabases) {
                return appDatabases[0].trackDao().getAll();
            }
        };
    }*/

    public static Function<Void, List<Object>> getAllTracks(AppDatabase database){
        return (Void v) -> {
            List<Object> tracks = new ArrayList<Object>();
            tracks.addAll(database.trackDao().getAll());
            return tracks;
        };
    }

    public static Function< Void, List<Object>> getTracksWithContainerId(TrackContainer tc, AppDatabase db){
        return (Void v) -> {
            Log.d("UUUU", "GETTING CONTAINER");
            List<Object> tracks = new ArrayList<Object>();
            AlbumWithTracks awt;
            if(tc.getType() == TrackListFragment.IdType.Album) {
                awt = db.albumDao().loadAlbumWithTracks(tc.getId());
                tracks.addAll(awt.tracks);
            }else {
                List<TrackPlaylistJoin> eb = db.track_playList_JOIN_Dao().getAll();
                for(TrackPlaylistJoin tj : eb){
                    Log.d("UUUU", "SAIN " + tj.playlistId + " " + tj.trackId);
                }
                tracks.addAll(db.track_playList_JOIN_Dao().getTracksFromPlayList(tc.getId()));
            }
            for(Object t : tracks){
                Log.d("UUUU","Jee from single album");
            }
            return tracks;
        };
    }
}
