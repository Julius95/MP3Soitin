package com.example.julius.mp3_soitin;

import android.Manifest;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.julius.mp3_soitin.entities.Album;
import com.example.julius.mp3_soitin.entities.AlbumWithTracks;
import com.example.julius.mp3_soitin.entities.Artist;
import com.example.julius.mp3_soitin.entities.ArtistWithAlbums;
import com.example.julius.mp3_soitin.entities.Genre;
import com.example.julius.mp3_soitin.entities.PlayList;
import com.example.julius.mp3_soitin.entities.Track;
import com.example.julius.mp3_soitin.entities.TracksGenreJoin;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

//https://developer.android.com/training/permissions/requesting.html
//http://vinsol.com/blog/2014/09/19/transaction-backstack-and-its-management/
public class MainActivity extends AppCompatActivity implements TrackListFragment.OnFragmentInteractionListener , AlbumListFragment.OnAlbumFragmentInteractionListener
, PlayListFragment.OnPlaylistFragmentInteractionListener {

    private MainActivityFragment musicPlayerFragment = new MainActivityFragment();

    private SongsManager songsManager = new SongsManager();

    private AppDatabase db;

    private TabLayout tabLayout;

    private TrackListFragment trackListFragment = TrackListFragment.newInstance(null);

    private AlbumListFragment albumListFragment = AlbumListFragment.newInstance(null);

    private PlayListFragment playlistFragment = PlayListFragment.newInstance(null,null);

    //private Stack<Integer> history = new Stack();
    //
    private ArrayList<Track> tracks;

    private String tag = "visible_fragment";

    private boolean backButtonPressed = false;

    private int activeTabPosition = 0;
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1112;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = AppDatabase.getInstance(getApplicationContext());
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentBackStackFragment = getSupportFragmentManager().findFragmentByTag(tag);
            if(currentBackStackFragment == null)
                return;
            if(currentBackStackFragment instanceof TrackListFragment){
                Log.d("UUUU", "TrackListFragment!!!");
                activeTabPosition = 0;
                tabLayout.getTabAt(activeTabPosition).select();
            }else if(currentBackStackFragment instanceof AlbumListFragment){
                activeTabPosition = 1;
                tabLayout.getTabAt(activeTabPosition).select();
            }else if(currentBackStackFragment instanceof PlayListFragment){
                activeTabPosition = 2;
                tabLayout.getTabAt(activeTabPosition).select();
            }
            if(backButtonPressed){
                //pop history stack here
                backButtonPressed = false;
            }
        });//https://why-android.com/2016/03/29/learn-how-to-use-the-onbackstackchangedlistener/

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tabLayout=(TabLayout)findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText("Tracks").setIcon(R.drawable.ic_audiotrack_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText("Albums").setIcon(R.drawable.ic_album_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText("PlayLists").setIcon(R.drawable.ic_playlist_play_black_24dp));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                updateTabSelection(position, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //tabLayout.addOnTabSelectedListener();
        changeFragment(trackListFragment);
    }

    //https://stackoverflow.com/questions/37335850/select-a-tab-without-invoking-ontabselectedlistener
    private void updateTabSelection(int position, boolean update) {
        if (position == activeTabPosition) {
            return;
        }

        activeTabPosition = position;
        if (update) {
            if(position == 0) {
                Log.d("UUUU", "Clicked 1");
                trackListFragment.setCurrentTrackContainer(null);//Null kuvaa tässä, että ei ole albumia ja ladataan kaikki raidat tietokannasta
                changeFragment(trackListFragment);
            }
            else if(position == 1){
                Log.d("UUUU", "Clicked 2");
                changeFragment(albumListFragment);
            }else if(position == 2){
                Log.d("UUUU", "Clicked 3");
                changeFragment(playlistFragment);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_scan){
            new ScanAsyncTask().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    ContentResolver musicResolver = getContentResolver();
                    Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    //String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
                    Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
                    if(musicCursor!=null && musicCursor.moveToFirst()) {
                        //get columns
                        int titleColumn = musicCursor.getColumnIndex
                                (android.provider.MediaStore.Audio.Media.TITLE);
                        int idColumn = musicCursor.getColumnIndex
                                (android.provider.MediaStore.Audio.Media._ID);
                        int artistColumn = musicCursor.getColumnIndex
                                (android.provider.MediaStore.Audio.Media.ARTIST);

                        int durationColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.DURATION);


                        do {
                            try {
                                long thisId = musicCursor.getLong(idColumn);
                                String thisTitle = musicCursor.getString(titleColumn);
                                String thisArtist = musicCursor.getString(artistColumn);
                                String duration = musicCursor.getString(durationColumn);
                                Log.d("UUUU", "- " + thisTitle + duration);
                                if (!thisArtist.equalsIgnoreCase("<unknown>")) {
                                    //save track
                                }

                            } catch (Exception e) {

                            }
                        }
                        while (musicCursor.moveToNext());
                    }
                } else {
                    Log.d("UUUU", "PERMISSION DENIED");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onFragmentInteraction(Track track) {
        Log.d("UUUU", "Selected Track " + track.getName());
        musicPlayerFragment.setTrack(track);
        changeFragment(musicPlayerFragment);
    }

    private void changeFragment(Fragment newFragement){
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.FragmentContainer, newFragement, tag);
        tran.addToBackStack(null);
        tran.commit();
    }

    @Override
    public void onAlbumFragmentInteractionListener(Album album) {
        Log.d("UUUU", "Got album : " + album.getName());
        trackListFragment.setCurrentTrackContainer(album);
        //tabLayout.getTabAt(0).select();
        changeFragment(trackListFragment);
    }

    @Override
    public void onBackPressed() {
        backButtonPressed = true;
        super.onBackPressed();
    }

    @Override
    public void onPlaylistFragmentInteractionListener(PlayList playList) {
        trackListFragment.setCurrentTrackContainer(playList);
        changeFragment(trackListFragment);
    }

    //https://stackoverflow.com/questions/21590189/dry-a-case-with-asynctasks
    private class ScanAsyncTask extends AsyncTask< Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... voids) {
            List<File> musicFiles = songsManager.getPlayList();
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            for(File file : musicFiles){
                mmr.setDataSource(file.getAbsolutePath());
                //Log.d("UUUU", "Inserting track " + t.getName() + " to database");
                String buf = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);//METADATA_KEY_ALBUMARTIST
                //Genre
                Genre genre = db.genreDao().findByName(buf);
                if(genre == null){
                    Log.d("UUUU", "Inserting new Genre " + buf);
                    genre = new Genre(buf);
                    genre.setId(db.genreDao().insert(genre));
                }
                //Artist
                buf = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
                Artist artist = db.artistDao().findByName(buf);
                if(artist == null){
                    Log.d("UUUU", "Inserting new Artist " + buf);
                    artist = new Artist(buf);
                    artist.setId(db.artistDao().insert(artist));
                }
                //Album
                buf = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                Album album = db.albumDao().findByName(buf);
                if(album == null){
                    Log.d("UUUU", "Inserting new Album " + buf);
                    album = new Album(buf, "lolDatewhocares", 1, artist.getId());//String name, String Date, int nmr_of_tracks, long artistId
                    album.setId(db.albumDao().insert(album));
                }else{
                    album.setNmr_of_tracks(album.getNmr_of_tracks()+1);
                    db.albumDao().updateAlbums(album);
                }
                //Track
                buf = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                Track track = db.trackDao().findByName(buf);//Palauttaa NULL jos ei ole tietokannassa
                if(track == null){
                    Log.d("UUUU", "Inserting Track " + buf);
                    db.trackDao().insert(new Track(buf, file.getAbsolutePath(), album.getId()));
                }else{
                    Log.d("UUUU", "EI OLE NULL " + track.getName());
                }
            }
            return "Done";
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }
    //https://stackoverflow.com/questions/25647881/android-asynctask-example-and-explanation
    //https://stackoverflow.com/questions/20455644/object-cannot-be-cast-to-void-in-asynctask
    /*
    Tämä AsyncLuokka hakee tietokannasta dataa AppDatabase-oliota käyttämällä
     */
    public static class LoadAsyncTask extends AsyncTask<Void, Void, List<Object>>
    {
        private Function<Void, List<Object>> function;
        private AsyncTaskListener listener;

        public LoadAsyncTask(Function<Void, List<Object>> f, AsyncTaskListener listener){
            function = f;
            this.listener = listener;
        }

        @Override
        protected List<Object> doInBackground(Void... voids) {
            return function.apply(null);
        }

        @Override
        protected void onPostExecute(List<Object> result) {
            listener.onTaskCompleted(result);
        }
    }

    public static class SaveAsyncTask extends AsyncTask<Void, Void, Object>
    {
        private Function<Void, Object> function;
        private AsyncTaskListener listener;

        public SaveAsyncTask(Function<Void, Object> f, AsyncTaskListener listener){
            function = f;
            this.listener = listener;
        }

        @Override
        protected Object doInBackground(Void... voids) {
            return function.apply(null);
        }

        @Override
        protected void onPostExecute(Object result) {
            listener.onTaskCompleted(result);
        }
    }

    public static class AsyncTaskNoReturnValue extends AsyncTask<Void, Void, Void>
    {
        private Function<Void, Void> function;

        public AsyncTaskNoReturnValue(Function<Void, Void> f){
            function = f;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return function.apply(null);
        }
    }

}
