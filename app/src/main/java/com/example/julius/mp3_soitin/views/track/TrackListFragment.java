package com.example.julius.mp3_soitin.views.track;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.julius.mp3_soitin.CustomListAdapter;
import com.example.julius.mp3_soitin.R;
import com.example.julius.mp3_soitin.views.dialogs.DialogTrackPlaylistUseCase;
import com.example.julius.mp3_soitin.views.dialogs.ListDialog;
import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.data.entities.TrackContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * https://stackoverflow.com/questions/8482606/when-a-fragment-is-replaced-and-put-in-the-back-stack-or-removed-does-it-stay
 */
public class TrackListFragment extends ListFragment implements TrackContract.View{


    private TrackContract.Presenter presenter;

    private TextView maintextview;

    private CustomListAdapter arrayAdapter;

    private final String defaultName = "Tracks";

    private String name = defaultName;

    private final ArrayList<Track> tracks = new ArrayList<>();

    public TrackListFragment() {}


    @Override
    public void onResume() {
        if(presenter!=null)
            presenter.start();
        super.onResume();
        maintextview.setText(name);
    }

    @Override
    public void showTracks(List<Track> tracks) {
        this.tracks.clear();
        this.tracks.addAll(tracks);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void setWindowName(String newName) {
        name = newName;
        maintextview.setText(name);
    }

    @Override
    public void resetWindowName() {
        name = defaultName;
        maintextview.setText(name);
    }

    @Override
    public void setPresenter(TrackContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDialog(DialogTrackPlaylistUseCase usecase, Track track, int mode) {
        ListDialog dialog;
        switch(mode){
            case 0:

                break;

            //lisätään kappaletta soittolistaan

            case 1:
                dialog = ListDialog.newInstance(usecase, track, true);
                //dialog.setListener(this);
                dialog.show(this.getActivity().getFragmentManager(), "NoticeDialogFragment");
                break;

            //poistetaan kappaletta soittolistasta

            case 2:
                dialog = ListDialog.newInstance(usecase, track, false);
                //dialog.setListener(this);
                dialog.show(this.getActivity().getFragmentManager(), "NoticeDialogFragment");
                break;
        }
    }

    public enum IdType {
        Playlist,
        Album
    }

    @Override//https://stackoverflow.com/questions/9245408/best-practice-for-instantiating-a-new-android-fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayAdapter = new CustomListAdapter(getContext(),android.R.layout.simple_list_item_1, tracks,
                this);
        setListAdapter(arrayAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_tracklist, container, false);

        maintextview = v.findViewById(R.id.raidatText);

        Log.d("AAA", "TRACK ON CREATE VIEW");
        if(savedInstanceState!=null){
            Log.d("AAA", "TRACKLIST ON SAVEDINSTANCE");
            if(presenter.shouldUsePersistedData()){
                presenter.setContainer((TrackContainer) savedInstanceState.getSerializable("container"));
                presenter.setTracks(savedInstanceState.getParcelableArrayList("tracks"));
            }
            savedInstanceState.clear();
        }
        return v;
    }


    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        registerForContextMenu(this.getListView());
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onListItemClick(android.widget.ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        Log.d("UUUU", "CallBack");
        if (presenter != null) {
            presenter.openTrack(position);
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.stop();
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
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        Log.d("AAA", "TRACKLIST ON SAVINGSTATE");
        outState.putParcelableArrayList("tracks", tracks);
        outState.putSerializable("container", presenter.getContainer());
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
        presenter.longPressedTrack(id, item.getItemId());
        return true;
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
