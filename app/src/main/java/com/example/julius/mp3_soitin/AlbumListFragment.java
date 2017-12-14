package com.example.julius.mp3_soitin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.julius.mp3_soitin.entities.Album;
import com.example.julius.mp3_soitin.entities.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Julius on 7.12.2017.
 * Edit: override setUserVisibleHint on the fragment. This gives you a good place to determine where to do your setup of resources or clean up of resources when the fragment comes in and out of view (for example when you have a PagerAdapter that switches from FragmentA to FragmentB).
 */

public class AlbumListFragment extends ListFragment implements AsyncTaskListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //private SongsManager songsManager = new SongsManager();

    private OnAlbumFragmentInteractionListener mListener;

    public AlbumListFragment() {
        // Required empty public constructor
    }

    @Override//https://stackoverflow.com/questions/9245408/best-practice-for-instantiating-a-new-android-fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        new MainActivity.LoadAsyncTask(Album.getAllAlbumsFromDB(AppDatabase.getInstance(getContext())), this).execute();
        /*List<Track> tracks = getArguments().getParcelableArrayList("list");
        ArrayAdapter<Track> arrayAdapter = new ArrayAdapter<Track>(
                getContext(),
                android.R.layout.simple_list_item_1,
                tracks);
        setListAdapter(arrayAdapter);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tracklist, container, false);
        TextView textview = v.findViewById(R.id.raidatText);
        textview.setText("ALBUMIT");
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        Log.d("UUUU", "CallBack");
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onListItemClick(android.widget.ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        Log.d("UUUU", "CallBack");
        if (mListener != null) {
            mListener.onAlbumFragmentInteractionListener((Album)l.getAdapter().getItem(position));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlbumFragmentInteractionListener) {
            mListener = (OnAlbumFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public void onTaskCompleted(Object o) {
        List<Album> albums = (List<Album>) o;
        ArrayAdapter<Album> arrayAdapter = new ArrayAdapter<Album>(
                getContext(),
                android.R.layout.simple_list_item_1,
                albums);
        setListAdapter(arrayAdapter);
    }

    public interface OnAlbumFragmentInteractionListener {
        void onAlbumFragmentInteractionListener(Album album);
    }


}
