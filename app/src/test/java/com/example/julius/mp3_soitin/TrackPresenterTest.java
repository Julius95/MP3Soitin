package com.example.julius.mp3_soitin;

import com.example.julius.mp3_soitin.data.Converter;
import com.example.julius.mp3_soitin.data.Repository;
import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.data.entities.TrackContainer;
import com.example.julius.mp3_soitin.views.BaseView;
import com.example.julius.mp3_soitin.views.dialogs.DialogTrackPlaylistUseCase;
import com.example.julius.mp3_soitin.views.track.TrackContract;
import com.example.julius.mp3_soitin.views.track.TrackListFragment;
import com.example.julius.mp3_soitin.views.track.TrackPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class TrackPresenterTest {

    @Mock
    TrackContract.View mockView;

    @Mock
    FragmentSwitcher switcher;

    @Mock
    Repository<Track> dataSource;

    @Mock
    DialogTrackPlaylistUseCase mockDialog;

    @Captor
    ArgumentCaptor<Consumer<List<Track>>> captor;

    ArgumentMatcher<List<Track>> matcher;

    ArgumentMatcher<Track> singleTrack;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        //Run the ui-update callback method on the same thread
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Runnable)invocation.getArguments()[0]).run();
                return null;
            }
        }).when(switcher).uiUpdate(isA(Runnable.class));
    }

    /**
     * Method that mock our database.
     * Should be called after a method that starts a database read operation.
     */
    private void databaseSimulation1(){
        verify(dataSource).getAll(captor.capture());
        List<Track> result = new ArrayList<>();

        //"Database rows"
        Track track1 = new Track("test", "C:/", 2);
        Track track2 = new Track("test2", "C:/2", 4);
        //Add them to result array
        result.add(track1);
        result.add(track2);

        //Provide a response for the callback consumer
        captor.getValue().accept(result);

        //Update matcher to watch out for these items
        matcher = argument ->
                argument.size() == result.size() &&
                argument.get(0).equals(track1) &&
                argument.get(1).equals(track2);
    }

    private void dataBaseSimulation2(){
        verify(dataSource).conversion(any(Function.class),captor.capture());
        List<Track> result = new ArrayList<>();

        //"Database rows"
        Track track1 = new Track("test", "C:/", 2);
        Track track2 = new Track("test2", "C:/2", 4);
        //Add them to result array
        result.add(track1);
        result.add(track2);

        //Provide a response for the callback consumer
        captor.getValue().accept(result);

        //Update matcher to watch out for these items
        matcher = argument ->
                argument.size() == result.size() &&
                        argument.get(0).equals(track1) &&
                        argument.get(1).equals(track2);
    }

    @Test
    public void basicLoadAndViewUpdate(){
        TrackPresenter classUnderTest = new TrackPresenter(dataSource, mockView, switcher, mockDialog);
        classUnderTest.start();

        //Mock database read query
        databaseSimulation1();

        verify(mockView).showTracks(argThat(matcher));
    }

    @Test
    public void longPressTrack(){
        TrackPresenter classUnderTest = new TrackPresenter(dataSource, mockView, switcher, mockDialog);
        classUnderTest.start();

        databaseSimulation1();

        classUnderTest.longPressedTrack(0, 2);
        verify(mockView).showDialog(eq(mockDialog), argThat((argument -> argument.getName().equals("test"))), eq(2));
        classUnderTest.longPressedTrack(1, 0);
        verify(mockView).showDialog(eq(mockDialog), argThat((argument -> argument.getName().equals("test2"))), eq(0));
    }

    @Test
    public void loadDataWithHelperObject(){
        TrackPresenter classUnderTest = new TrackPresenter(dataSource, mockView, switcher, mockDialog);
        classUnderTest.setContentByHelperObject(new TrackContainer() {
            @Override
            public String getName() {
                return "TestContainer";
            }

            @Override
            public TrackListFragment.IdType getType() {
                return TrackListFragment.IdType.Album;
            }

            @Override
            public long getId() {
                return 1;
            }
        });

        classUnderTest.start();
        dataBaseSimulation2();
        verify(mockView).showTracks(argThat(matcher));
        verify(mockView).setWindowName(eq("TestContainer"));
        assertEquals(false, classUnderTest.shouldUsePersistedData());
    }

    @Test
    public void selectTrack(){
        TrackPresenter classUnderTest = new TrackPresenter(dataSource, mockView, switcher, mockDialog);
        classUnderTest.start();

        databaseSimulation1();

        classUnderTest.openTrack(0);

        verify(switcher).switchTo(eq(FragmentType.Player), any(Consumer.class));
    }
}
