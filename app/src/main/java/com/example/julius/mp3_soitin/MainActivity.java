package com.example.julius.mp3_soitin;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import com.example.julius.mp3_soitin.data.entities.TrackPlaylistJoin;
import com.example.julius.mp3_soitin.filescanning.BadFile;
import com.example.julius.mp3_soitin.filescanning.ScanResult;
import com.example.julius.mp3_soitin.filescanning.MusicFileScanner;
import com.example.julius.mp3_soitin.views.BasePresenter;
import com.example.julius.mp3_soitin.views.album.AlbumListFragment;
import com.example.julius.mp3_soitin.views.album.AlbumPresenter;
import com.example.julius.mp3_soitin.views.dialogs.DialogTrackPlaylistUseCase;
import com.example.julius.mp3_soitin.views.dialogs.ListDialog;
import com.example.julius.mp3_soitin.views.dialogs.SimpleListDialog;
import com.example.julius.mp3_soitin.views.player.PlayerFragment;
import com.example.julius.mp3_soitin.views.player.PlayerPresenter;
import com.example.julius.mp3_soitin.views.playlist.PlayListFragment;
import com.example.julius.mp3_soitin.views.playlist.PlaylistPresenter;
import com.example.julius.mp3_soitin.views.track.TrackListFragment;
import com.example.julius.mp3_soitin.views.track.TrackPresenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

//https://developer.android.com/training/permissions/requesting.html
//http://vinsol.com/blog/2014/09/19/transaction-backstack-and-its-management/
public class MainActivity extends AppCompatActivity implements FragmentSwitcher {

    private PlayerFragment musicPlayerFragment;

    @Inject AppDatabase db;

    private TabLayout tabLayout;

    private TrackListFragment trackListFragment;

    private AlbumListFragment albumListFragment;

    private PlayListFragment playlistFragment;

    @Inject
    Repository<Track> trackrepo;
    @Inject
    Repository<Album> albumrepo;
    @Inject
    Repository<PlayList> playlistrepo;
    @Inject
    Repository<Genre> genrerepo;
    @Inject
    Repository<Artist> artistrepo;

    private String tag = "visible_fragment";

    private boolean backButtonPressed = false;

    private int activeTabPosition = 0;
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1112;

    private HashMap<FragmentType, BasePresenter> map = new HashMap<>();
    private HashMap<FragmentType, Fragment> viewmap = new HashMap<>();

    private AlertDialog.Builder builder1;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private void activateFragmentTab(Fragment f){
        TabLayout.Tab tab;
        if(f instanceof TrackListFragment){
            Log.d("UUUU", "TrackListFragment!!!");
            activeTabPosition = 0;
        }else if(f instanceof AlbumListFragment){
            activeTabPosition = 1;
            //tabLayout.getTabAt(activeTabPosition).select();
        }else if(f instanceof PlayListFragment){
            activeTabPosition = 2;
            //tabLayout.getTabAt(activeTabPosition).select();
        }else if(f instanceof PlayerFragment){
            activeTabPosition = 3;
        }
        tab = tabLayout.getTabAt(activeTabPosition);
        if(tab != null)
            tab.select();
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("AAA", "POSITION " + position + " IS NULL " + (viewmap.get(FragmentType.Tracks) == null));
            switch(position){
                case 0: return viewmap.get(FragmentType.Tracks);
                case 1: return viewmap.get(FragmentType.Albums);
                case 2: return viewmap.get(FragmentType.Playlist);
                case 3: return viewmap.get(FragmentType.Player);
                default: return viewmap.get(FragmentType.Tracks);
            }
        }

        @Override
        public int getCount() {
            return viewmap.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        trackListFragment = TrackListFragment.newInstance(null);
        albumListFragment = AlbumListFragment.newInstance(null);
        playlistFragment = PlayListFragment.newInstance(null,null);
        musicPlayerFragment = new PlayerFragment();
        /*DaggerMainActivityComponent
        .builder()
        .appDatabase(AppDatabase.getInstance(getBaseContext()))
        .build()
        .inject(this);*/
        //db = AppDatabase.getInstance(getApplicationContext());
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentBackStackFragment = getSupportFragmentManager().findFragmentByTag(tag);
            if(currentBackStackFragment == null)
                return;
            activateFragmentTab(currentBackStackFragment);
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        tabLayout = findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText("Tracks").setIcon(R.drawable.ic_audiotrack_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText("Albums").setIcon(R.drawable.ic_album_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText("PlayLists").setIcon(R.drawable.ic_playlist_play_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText("Player").setIcon(R.drawable.ic_play_arrow_black_24dp));
        tabLayout.setTabMode(MODE_SCROLLABLE);
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
        DialogTrackPlaylistUseCase usecase = new DialogTrackPlaylistUseCase(){
            @Override
            public void getPlayListsThatCanAcceptThisTrack(Consumer<List<PlayList>> callback, Track track) {
                CompletableFuture.supplyAsync(()-> db.track_playList_JOIN_Dao().getPlayListsThatCanAcceptThisTrack(track.getId()))
                        .thenAccept((List<PlayList> res) -> uiUpdate(() -> callback.accept(res)));
            }

            @Override
            public void getPlaylistsThatIncludeTrack(Consumer<List<PlayList>> callback, Track track) {
                CompletableFuture.supplyAsync(()-> db.track_playList_JOIN_Dao().getPlayListsThatIncludeThisTrack(track.getId()))
                        .thenAccept((List<PlayList> res) -> uiUpdate(() -> callback.accept(res)));
            }

            @Override
            public void saveTrackToPlaylist(Track track, PlayList playlist) {
                TrackPlaylistJoin tjp = new TrackPlaylistJoin(track.getId(), playlist.getId());
                CompletableFuture.runAsync(()-> db.track_playList_JOIN_Dao().insert(tjp));
            }

            @Override
            public void deleteTrackFromPlaylist(Track track, PlayList playlist) {
                CompletableFuture.runAsync(()-> db.track_playList_JOIN_Dao().delete(track.getId(), playlist.getId()));
            }
        };
        /*trackrepo = new TrackRepository(db);
        albumrepo = new AlbumRepository(db);
        playlistrepo = new PlaylistRepository(db);
        genrerepo = new GenreRepository(db);
        artistrepo = new ArtistRepository(db);*/
        builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> dialog.cancel());
        map.put(FragmentType.Tracks, new TrackPresenter(trackrepo, trackListFragment, this, usecase));
        map.put(FragmentType.Albums, new AlbumPresenter(albumrepo, albumListFragment, this));
        map.put(FragmentType.Player, new PlayerPresenter(musicPlayerFragment, this));
        map.put(FragmentType.Playlist, new PlaylistPresenter(playlistrepo, playlistFragment,this));
        viewmap.put(FragmentType.Albums, albumListFragment);
        viewmap.put(FragmentType.Tracks, trackListFragment);
        viewmap.put(FragmentType.Player, musicPlayerFragment);
        viewmap.put(FragmentType.Playlist, playlistFragment);
        mPagerAdapter.notifyDataSetChanged();
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                map.get(FragmentType.getType(position)).refresh();
            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    //https://stackoverflow.com/questions/37335850/select-a-tab-without-invoking-ontabselectedlistener
    private void updateTabSelection(int position, boolean update) {
        if (position == activeTabPosition) {
            return;
        }

        activeTabPosition = position;
        if (update) {
            switchTo(FragmentType.getType(position));
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
            //new ScanAsyncTask().execute();
            new MusicFileScanner((ScanResult sr) -> {
                Serializable callback = (Consumer<List<BadFile>> & Serializable)(List<BadFile> files) -> {
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    for(BadFile bf : files) {
                        mmr.setDataSource(bf.getPath());
                        Genre genre = genrerepo.findByName(bf.getGenreName());
                        if (genre == null) {
                            Log.d("UUUU", "Inserting new Genre " + "Default");
                            genre = new Genre("Default");
                            genre.setId(genrerepo.add(genre));
                        }
                        Artist artist = artistrepo.findByName(bf.getArtistName());
                        if (artist == null) {
                            Log.d("UUUU", "Inserting new Artist Default");
                            artist = new Artist("Default");
                            artist.setId(artistrepo.add(artist));
                        }
                        //Album
                        Album album = albumrepo.findByName(bf.getAlbumName());
                        if (album == null) {
                            if(bf.getAlbumName()==null)
                                album = new Album("Default", "lolDatewhocares", 1, artist.getId());
                            else
                                album = new Album(bf.getAlbumName(), "lolDatewhocares", 1, artist.getId());
                            Log.d("UUUU", "Inserting new Album Default");
                            album.setId(albumrepo.add(album));
                        } else {
                            album.setNmr_of_tracks(album.getNmr_of_tracks() + 1);
                            albumrepo.update(album);
                        }
                        //Track
                        Track track = trackrepo.findByName(bf.getTrackName());//Palauttaa NULL jos ei ole tietokannassa
                        if (track == null) {
                            Log.d("UUUU", "Inserting Track ");
                            trackrepo.add(new Track(bf.getTrackName(), bf.getPath(), Math.round(Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000), album.getId()));
                        } else {
                            //TODO jos löytyy jo valmiiksi raita niin mitäs sitten?
                            Log.d("UUUU", "EI OLE NULL " + track.getName());
                        }
                    }
                };
                SimpleListDialog dialog = SimpleListDialog.newInstance((ArrayList<BadFile>) sr.getBadFiles(), callback);
                dialog.show(getSupportFragmentManager(), "SimpleNoticeDialogFragment");
                for (BasePresenter screen : map.values()) {
                    if(screen.isActive())
                        screen.refresh();
                }
            }, trackrepo, albumrepo, genrerepo, artistrepo).execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
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
                    musicCursor.close();
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

    private void changeFragment(Fragment newFragement){
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.FragmentContainer, newFragement, tag);
        tran.addToBackStack(null);
        tran.commit();
    }

    @Override
    public void onBackPressed() {
        backButtonPressed = true;
        if(map.get(FragmentType.getType(mPager.getCurrentItem())).onBackPressed())
            super.onBackPressed();
    }

    @Override
    public void switchTo(FragmentType type) {
        //activateFragmentTab(viewmap.get(type));
        //changeFragment(viewmap.get(type));
        //https://stackoverflow.com/questions/38722325/fragmentmanager-is-already-executing-transactions-when-is-it-safe-to-initialise/38722520?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        //3rd answer
        Handler uiHandler = new Handler();
        uiHandler.post(() -> mPager.setCurrentItem(FragmentType.toInt(type)));
    }

    @Override
    public void switchTo(FragmentType type, Consumer<BasePresenter> c) {
        c.accept(map.get(type));
        mPager.setCurrentItem(FragmentType.toInt(type));
    }

    @Override
    public void uiUpdate(Runnable run) {
        runOnUiThread(run);
    }

}
