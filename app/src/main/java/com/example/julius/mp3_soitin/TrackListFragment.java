package com.example.julius.mp3_soitin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.julius.mp3_soitin.Dialogs.ListDialog;
import com.example.julius.mp3_soitin.entities.Album;
import com.example.julius.mp3_soitin.entities.PlayList;
import com.example.julius.mp3_soitin.entities.Track;
import com.example.julius.mp3_soitin.entities.TrackContainer;
import com.example.julius.mp3_soitin.entities.TrackPlaylistJoin;

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

    private List<Track> raidat;

    private TrackContainer currentTrackContainer;

    public enum IdType {
        Playlist,
        Album
    }

    private OnFragmentInteractionListener mListener;

    private TextView maintextview;

    CustomListAdapter arrayAdapter;

    public TrackListFragment() {
        // Required empty public constructor
        raidat = new ArrayList<>();
    }

    @Override//https://stackoverflow.com/questions/9245408/best-practice-for-instantiating-a-new-android-fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fetch tracks from database
        arrayAdapter = new CustomListAdapter(getContext(),android.R.layout.simple_list_item_1, raidat,
                this);
        setListAdapter(arrayAdapter);
        /*if(currentTrackContainer == null){
            new MainActivity.LoadAsyncTask(Track.getAllTracks(AppDatabase.getInstance(getContext())), this).execute();
        }else{

        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_tracklist, container, false);

        maintextview = v.findViewById(R.id.raidatText);

        if(currentTrackContainer == null){
            new MainActivity.LoadAsyncTask(Track.getAllTracks(AppDatabase.getInstance(getContext())), this).execute();
        }else{
            String type;
            if(currentTrackContainer.getType() == IdType.Album)
                type = "Albumi : ";
            else
                type = "PlayList : ";
            maintextview.setText(type + currentTrackContainer.getName());
        }
        return v;
    }


    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        registerForContextMenu(this.getListView());
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
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo adapter;
        try{
            adapter = (AdapterView.AdapterContextMenuInfo) menuInfo;
        }catch (ClassCastException e) {
            Log.e("UUUU", "bad menuInfo in TrackList", e);
            return;
        }
        menu.setHeaderTitle(getListAdapter().getItem(adapter.position).toString());
        menu.add(Menu.NONE, 0, Menu.NONE, R.string.info);
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.addtoplaylist);
        menu.add(Menu.NONE, 2, Menu.NONE, R.string.removefromplaylist);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        int id;
        Track track;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            id = (int)getListAdapter().getItemId(info.position);
            track = (Track)getListAdapter().getItem(id);
        } catch (ClassCastException e) {
            Log.e("", "bad menuInfo", e);
            return false;
        }
        Log.d("UUUU", "id = " + id + " " + item.getTitle() + " " + item.getItemId());
        ListDialog dialog;
        switch(item.getItemId()){
            case 0:

                break;
            /*
            Ollaan lisäämässä kappaletta soittolistaan
             */
            case 1:
                dialog = ListDialog.newInstance(track, true);
                //dialog.setListener(this);
                dialog.show(this.getActivity().getSupportFragmentManager(), "NoticeDialogFragment");
                break;
            /*
            Ollaan poistamassa kappaletta soittolistasta
             */
            case 2:
                dialog = ListDialog.newInstance(track, false);
                //dialog.setListener(this);
                dialog.show(this.getActivity().getSupportFragmentManager(), "NoticeDialogFragment");
                break;
        }
        return true;
    }

    @Override
    public void onTaskCompleted(Object o) {
        raidat.clear();
        raidat.addAll((List<Track>) o);
        if(getListAdapter() == null){
            arrayAdapter = new CustomListAdapter(getContext(),android.R.layout.simple_list_item_1, raidat,
                    this);
            setListAdapter(arrayAdapter);
        }
        arrayAdapter.notifyDataSetChanged();

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
