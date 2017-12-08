package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by Julius on 2.12.2017.
 */

public class ArtistWithAlbums {

    @Embedded
    public Artist artist;

    @Relation(parentColumn = "id", entityColumn = "artist_id", entity = Album.class)
    public List<Album> getAlbums;
}
