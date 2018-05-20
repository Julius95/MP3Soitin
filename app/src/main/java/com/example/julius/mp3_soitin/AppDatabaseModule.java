package com.example.julius.mp3_soitin;

import android.content.Context;

import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.dao_repositories.AlbumRepository;
import com.example.julius.mp3_soitin.data.dao_repositories.ArtistRepository;
import com.example.julius.mp3_soitin.data.dao_repositories.GenreRepository;
import com.example.julius.mp3_soitin.data.dao_repositories.PlaylistRepository;
import com.example.julius.mp3_soitin.data.dao_repositories.TrackRepository;
import com.example.julius.mp3_soitin.data.entities.Album;
import com.example.julius.mp3_soitin.data.entities.Artist;
import com.example.julius.mp3_soitin.data.entities.Genre;
import com.example.julius.mp3_soitin.data.entities.PlayList;
import com.example.julius.mp3_soitin.data.entities.Track;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Module
public class AppDatabaseModule {

    @Provides
    Repository<Track> trackrepo(AppDatabase db){
        return new TrackRepository(db);
    }

    @Provides
    Repository<Album> albumrepo(AppDatabase db){
        return new AlbumRepository(db);
    }

    @Provides
    Repository<PlayList> playListrepo(AppDatabase db){
        return new PlaylistRepository(db);
    }

    @Provides
    Repository<Genre> genrerepo(AppDatabase db){
        return new GenreRepository(db);
    }

    @Provides
    Repository<Artist> artistrepo(AppDatabase db){
        return new ArtistRepository(db);
    }
}

@Component(modules={AppDatabaseModule.class})
interface MainActivityComponent {

    void inject(MainActivity acitivity);

    @Component.Builder
    interface Builder {

        MainActivityComponent build();
        @BindsInstance
        Builder appDatabase(AppDatabase db);
    }
}
