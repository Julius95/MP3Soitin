package com.example.julius.mp3_soitin.dataSourceTests;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.data.dao_repositories.TrackRepository;
import com.example.julius.mp3_soitin.data.entities.Track;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;


import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.timeout;

/**
 * Created by Julius on 23.3.2018.
 */

public class TrackSourceTest {
    private TrackRepository mTrackRepo;

    /**
     * This comparator returns 1 if given tracks are equal 0 otherwise
     */
    private final Comparator<Track> comparator = new Comparator<Track>() {
        @Override
        public int compare(Track track, Track t1) {
            if(track.getId() == t1.getId() && track.getName().equals(t1.getName()) &&
                track.getPath().equals(t1.getPath()))
                return 1;
            return 0;
        }
    };

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        AppDatabase mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mTrackRepo = new TrackRepository(mDb);
    }

    @After
    public void closeDb() throws IOException {
        //mDb.close();
    }

    @Test
    public void simpleAsyncInsertAndLoadTest(){
        Consumer<Long> callback = Mockito.mock(Consumer.class);
        Consumer<Track> callbackLoad = Mockito.mock(Consumer.class);

        Track t1 = new Track("TEST", "C:\\Test\\hope\\it\\works", 124);
        Track t2 = new Track("TEST2", "2", 300);

        //Starting with simple insertions
        mTrackRepo.add(t1, callback);
        Mockito.verify(callback, timeout(500)).accept(argThat(argument -> argument.equals(new Long(1))));
        mTrackRepo.add(t2, callback);
        Mockito.verify(callback, timeout(500)).accept(argThat(argument -> argument.equals(new Long(2))));

        t1.setId(1);
        t2.setId(2);

        //Now load the inserted tracks
        mTrackRepo.get(1, callbackLoad);
        Mockito.verify(callbackLoad, timeout(500)).accept(argThat(argument -> comparator.compare(argument, t1) == 1));
        mTrackRepo.get(2, callbackLoad);
        Mockito.verify(callbackLoad, timeout(500)).accept(argThat(argument -> comparator.compare(argument, t2) == 1));
    }

    @Test
    public void synchronousInsertAndDeleteTest(){
        Track t1 = new Track("TEST", "C:\\Test\\hope\\it\\works", 124);
        t1.setId(Math.toIntExact(mTrackRepo.add(t1)));
        assertEquals(1, mTrackRepo.delete(t1));
        mTrackRepo.getAll((List<Track> t) -> {assertEquals(t, null);});
    }

}
