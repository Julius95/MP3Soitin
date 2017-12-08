package com.example.julius.mp3_soitin.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class AlbumWithTracks {

    @Embedded
    public Album album;

    @Relation(parentColumn = "id", entityColumn = "albumId", entity = Track.class)
    public List<Track> tracks; // or use simply 'List pets;'


   /* Alternatively you can use projection to fetch a specific column (i.e. only name of the pets) from related Pet table. You can uncomment and try below;

   @Relation(parentColumn = "id", entityColumn = "userId", entity = Pet.class, projection = "name")
   public List<String> pets;
   */
}
