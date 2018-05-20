package com.example.julius.mp3_soitin.testApplication;

import android.content.Context;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.MainActivity;
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

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@Module
public abstract class TestApplicationModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();
}

@Module
class mockDb{
    @Provides
    Repository<Track> trackrepo(){

        //Track fake database rows

        List<Track> result = new ArrayList<>();

        //"Database rows"
        Track track1 = new Track("test", "C:/", 2);
        Track track2 = new Track("test2", "C:/2", 4);
        //Add them to result array
        result.add(track1);
        result.add(track2);

        TrackRepository res = Mockito.mock(TrackRepository.class);
        doAnswer((Answer<Void>) invocation -> {
            System.out.println("Getting fake tracks");
            ((Consumer)invocation.getArguments()[0]).accept(new ArrayList<>(result));
            return null;
        }).when(res).getAll(isA(Consumer.class));

        return res;
    }

    @Provides
    Repository<Album> albumrepo(){
        return Mockito.mock(AlbumRepository.class);
    }

    @Provides
    Repository<PlayList> playListrepo(){
        return Mockito.mock(PlaylistRepository.class);
    }

    @Provides
    Repository<Genre> genrerepo(){
        return Mockito.mock(GenreRepository.class);
    }

    @Provides
    Repository<Artist> artistrepo(){
        return Mockito.mock(ArtistRepository.class);
    }

    @Provides
    AppDatabase db(){
        return Mockito.mock(AppDatabase.class);
    }
}

@Component(modules = { AndroidInjectionModule.class, TestApplicationModule.class, mockDb.class})
interface TestApplicationComponent extends AndroidInjector<TestApplication> {
    @Component.Builder
    interface Builder {

        TestApplicationComponent build();
        @BindsInstance
        TestApplicationComponent.Builder appDatabase(Context context);
    }
}



