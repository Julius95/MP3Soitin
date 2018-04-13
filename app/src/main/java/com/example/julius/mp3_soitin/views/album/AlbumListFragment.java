package com.example.julius.mp3_soitin.views.album;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.AsyncTaskListener;
import com.example.julius.mp3_soitin.MainActivity;
import com.example.julius.mp3_soitin.R;
import com.example.julius.mp3_soitin.data.entities.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 7.12.2017.
 * Edit: override setUserVisibleHint on the fragment. This gives you a good place to determine where to do your setup of resources or clean up of resources when the fragment comes in and out of view (for example when you have a PagerAdapter that switches from FragmentA to FragmentB).
 */

public class AlbumListFragment extends ListFragment implements AlbumContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AlbumContract.Presenter presenter;

    private final List<Album> albums = new ArrayList<>();

    private ArrayAdapter<Album> arrayAdapter;

    public AlbumListFragment() {
        // Required empty public constructor
    }

    @Override//https://stackoverflow.com/questions/9245408/best-practice-for-instantiating-a-new-android-fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AAA", "ALBUM ONCREATE()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tracklist, container, false);
        TextView textview = v.findViewById(R.id.raidatText);
        textview.setText(R.string.albumit);
        //if(getListAdapter()==null){
            arrayAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    albums);
            setListAdapter(arrayAdapter);
        //}
        return v;
    }

    @Override
    public void onResume() {
        Log.d("AAA", "ALBUM ONRESUME()");
        super.onResume();
        presenter.start();
    }

    @Override
    public void onListItemClick(android.widget.ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        Log.d("UUUU", "CallBack");
        if (presenter != null) {
            presenter.selectAlbum((Album)l.getAdapter().getItem(position));
        }
    }

    public static AlbumListFragment newInstance(ArrayList<Album> album) {
        AlbumListFragment myFragment = new AlbumListFragment();
        if(album!=null){
            Bundle args = new Bundle();
           // args.putParcelableArrayList("list", album);
            myFragment.setArguments(args);
        }
        return myFragment;
    }

    @Override
    public void showAlbums(List<Album> result) {
        albums.clear();
        albums.addAll(result);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(AlbumContract.Presenter presenter) {
        this.presenter = presenter;
    }


}
