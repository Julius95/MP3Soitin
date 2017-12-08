package com.example.julius.mp3_soitin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.julius.mp3_soitin.entities.Album;
import com.example.julius.mp3_soitin.entities.Track;
import com.example.julius.mp3_soitin.entities.TrackContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

/**
 * https://stackoverflow.com/questions/8482606/when-a-fragment-is-replaced-and-put-in-the-back-stack-or-removed-does-it-stay
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrackListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrackListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackListFragment extends ListFragment implements AsyncTaskListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TrackContainer currentTrackContainer;

    public enum IdType {
        Playlist,
        Album
    }

    private Stack<Album> history = new Stack();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SongsManager songsManager = new SongsManager();

    private OnFragmentInteractionListener mListener;

    public TrackListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrackListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackListFragment newInstance(String param1, String param2) {
        TrackListFragment fragment = new TrackListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override//https://stackoverflow.com/questions/9245408/best-practice-for-instantiating-a-new-android-fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d("UUUU", "Fragment OnCreate");
        //Fetch tracks from database
        if(currentTrackContainer == null){
            new MainActivity.LoadAsyncTask(Track.getAllTracks(AppDatabase.getInstance(getContext())), this).execute();
        }else{

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*if(currentAlbum==null){
            if(!history.isEmpty())
                history.pop();
            if(history.isEmpty() || history.peek() == null){
                new MainActivity.LoadAsyncTask(Track.getAllTracks(), this, getContext()).execute();
            }else{
                setCurrentAlbum(history.pop());
            }
        }*/
        if(currentTrackContainer == null){
            new MainActivity.LoadAsyncTask(Track.getAllTracks(AppDatabase.getInstance(getContext())), this).execute();
        }
        return inflater.inflate(R.layout.fragment_tracklist, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        Log.d("UUUU", "CallBack");
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause(){
        //history.push(currentAlbum);
        currentTrackContainer = null;
        super.onPause();
    }

    @Override
    public void onListItemClick(android.widget.ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        Log.d("UUUU", "CallBack");
        if (mListener != null) {
            mListener.onFragmentInteraction((Track)l.getAdapter().getItem(position));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    @Override
    public void onTaskCompleted(Object o) {
        List<Track> tracks = (List<Track>) o;
        ArrayAdapter<Track> arrayAdapter = new ArrayAdapter<Track>(
                getContext(),
                android.R.layout.simple_list_item_1,
                tracks);
        setListAdapter(arrayAdapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Track track);
    }

    public void setCurrentTrackContainer(TrackContainer newContainer){
        currentTrackContainer = newContainer;
        if(currentTrackContainer!=null) {
            Log.d("UUUU", "Haetaan tietokannasta");
            new MainActivity.LoadAsyncTask(Track.getTracksWithContainerId(currentTrackContainer, AppDatabase.getInstance(getContext())), this).execute();
        }else{
            history.push(null);
            new MainActivity.LoadAsyncTask(Track.getAllTracks(AppDatabase.getInstance(getContext())), this).execute();
        }
    }

    public static TrackListFragment newInstance(ArrayList<Track> tracks) {
        TrackListFragment myFragment = new TrackListFragment();
        if(tracks!=null){
            Bundle args = new Bundle();
            args.putParcelableArrayList("list", tracks);
            myFragment.setArguments(args);
        }
        return myFragment;
    }
}
