package com.example.julius.mp3_soitin.filescanning;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Julius on 26.3.2018.
 */

public class BadFile implements Parcelable {
    private String trackName, albumName, artistName, genreName, path;

    BadFile(String trackName, String albumName, String artistName, String genreName, String path) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.artistName = artistName;
        this.genreName = genreName;
        this.path = path;
    }

    private BadFile(Parcel in) {
        trackName = in.readString();
        albumName = in.readString();
        artistName = in.readString();
        genreName = in.readString();
        path = in.readString();
    }

    public static final Creator<BadFile> CREATOR = new Creator<BadFile>() {
        @Override
        public BadFile createFromParcel(Parcel in) {
            return new BadFile(in);
        }

        @Override
        public BadFile[] newArray(int size) {
            return new BadFile[size];
        }
    };

    public String getTrackName() {
        return trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getGenreName() {
        return genreName;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.trackName);
        parcel.writeString(this.albumName);
        parcel.writeString(this.artistName);
        parcel.writeString(this.genreName);
        parcel.writeString(this.path);
    }

    @Override
    public String toString(){
        return trackName;
    }
}
