package com.example.julius.mp3_soitin;


import android.media.MediaMetadataRetriever;

import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.Album;
import com.example.julius.mp3_soitin.data.entities.Artist;
import com.example.julius.mp3_soitin.data.entities.Genre;
import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.filescanning.BadFile;
import com.example.julius.mp3_soitin.filescanning.MusicFileScanner;
import com.example.julius.mp3_soitin.filescanning.ScanResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static android.media.MediaMetadataRetriever.METADATA_KEY_ALBUM;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_GENRE;
import static android.media.MediaMetadataRetriever.METADATA_KEY_TITLE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Here we test the functionality of our MusicFileScanner class. Testable instance is a partial mock.
 */

public class ScanTest {

    @Mock private MediaMetadataRetriever retriever;
    @Mock private Repository<Track> trackRepo;
    @Mock private Repository<Album> albumRepo;
    @Mock private Repository<Artist> artistRepo;
    @Mock private Repository<Genre> genreRepo;
    @Mock private Consumer<ScanResult> consumer;

    private MusicFileScanner spy;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        spy = spy(new MusicFileScanner(retriever, consumer, trackRepo, albumRepo, genreRepo, artistRepo));
    }

    @Test
    public void addition_isCorrect() throws Exception {
        List<File> list = new ArrayList<>();
        list.add(new File("file1"));
        list.add(new File("file2"));
        list.add(new File("file3"));
        doReturn(list).when(spy).getPlayList();
        when(trackRepo.findByName(anyString())).thenReturn(null);
        when(albumRepo.findByName(anyString())).thenReturn(null);
        when(artistRepo.findByName(anyString())).thenReturn(null);
        when(genreRepo.findByName(anyString())).thenReturn(null);
        when(retriever.extractMetadata(METADATA_KEY_ALBUMARTIST)).thenReturn("Artist_1", "Artist_2", "Artist_3");
        when(retriever.extractMetadata(METADATA_KEY_ALBUM)).thenReturn("ALBUM_1", "ALBUM_2", "ALBUM_3");
        when(retriever.extractMetadata(METADATA_KEY_TITLE)).thenReturn("TITLE_1", "TITLE_2", "TITLE_3");
        when(retriever.extractMetadata(METADATA_KEY_GENRE)).thenReturn("GENRE_1", "GENRE_2", "GENRE_3");
        when(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)).thenReturn("200000");
        when(trackRepo.add(any(Track.class))).thenReturn(1L, 2L, 3L);
        when(albumRepo.add(any(Album.class))).thenReturn(1L, 2L, 3L);
        when(artistRepo.add(any(Artist.class))).thenReturn(1L, 2L, 3L);
        when(genreRepo.add(any(Genre.class))).thenReturn(1L, 2L, 3L);
        System.out.println("Start simple add test");

        //https://stackoverflow.com/questions/33556172/why-doesnt-mocking-work-with-asynctask
        ScanResult sr = spy.doInBackground();

        //verify that there were no problems in scanning
        assertEquals(3, sr.getSuccessfulReads());
        assertEquals(0, sr.getFailedReads());
        assertEquals(0, sr.getBadFiles().size());


        verify(retriever, times(3)).setDataSource(anyString());
        verify(retriever, times(15)).extractMetadata(anyInt());

        //Verify the add methods were called
        verify(trackRepo, times(3)).add(any(Track.class));
        verify(albumRepo, times(3)).add(any(Album.class));
        verify(artistRepo, times(3)).add(any(Artist.class));
        verify(genreRepo, times(3)).add(any(Genre.class));
    }
}
