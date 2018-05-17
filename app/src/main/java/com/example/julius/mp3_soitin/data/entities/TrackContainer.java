package com.example.julius.mp3_soitin.data.entities;

import com.example.julius.mp3_soitin.views.track.TrackListFragment;

import java.io.Serializable;

/**
 * Created by Julius on 8.12.2017.
 * Kaikki DAO-luokat, joilla on yhteys raita-tauluun implementoivat tämän rajapinnan.
 * Tarkoituksena taata, että TrackListFragment voi helposti hakea tyypin perusteella oikeasta tietokanta taulusta raidat.
 */



public interface TrackContainer extends Serializable{

    String getName();
    TrackListFragment.IdType getType();
    long getId();
}
