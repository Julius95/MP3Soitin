package com.example.julius.mp3_soitin.views.playlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ListView;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.AsyncTaskListener;
import com.example.julius.mp3_soitin.MainActivity;
import com.example.julius.mp3_soitin.R;
import com.example.julius.mp3_soitin.views.dialogs.QueryDialog;
import com.example.julius.mp3_soitin.data.entities.PlayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 8.12.2017.
 */

public class PlayListFragment extends ListFragment implements QueryDialog.NoticeDialogListener, PlaylistContract.View {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<PlayList> playlists = new ArrayList<PlayList>();
    private ArrayAdapter<PlayList> arrayAdapter;

    private PlaylistContract.Presenter presenter;

    public PlayListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_playlist.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayListFragment newInstance(String param1, String param2) {
        PlayListFragment fragment = new PlayListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1;
            String mParam2;
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        arrayAdapter = new ArrayAdapter<PlayList>(
                getContext(),
                android.R.layout.simple_list_item_1,
                playlists);
        setListAdapter(arrayAdapter);
       // new MainActivity.LoadAsyncTask(PlayList.getAllPlayListsFromDB(AppDatabase.getInstance(getContext())), this).execute();
    }

    @Override
    public void onResume() {
        Log.d("AAA", "PLAYLIST ONRESUME()");
        super.onResume();
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist,
                container, false);

        ListView playlist = (ListView) view.findViewById(R.id.playlistName);

        Button button = (Button) view.findViewById(R.id.addPlayList);
        button.setOnClickListener(view1 -> {
            QueryDialog dialog = new QueryDialog();
            dialog.setListener(this);
            dialog.show(getActivity().getSupportFragmentManager(), "NoticeDialogFragment");
        });
        return view;
    }

    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        registerForContextMenu(this.getListView());
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
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.removePlaylist);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        int id;
        PlayList playlist;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            id = (int)getListAdapter().getItemId(info.position);
            playlist = (PlayList)getListAdapter().getItem(id);
        } catch (ClassCastException e) {
            Log.e("", "bad menuInfo", e);
            return false;
        }
        Log.d("UUUU", "id = " + id + " " + item.getTitle() + " " + item.getItemId());
        switch(item.getItemId()){
            /*
            Ollaan poistamassa soittolista
             */
            case 1:
                new AlertDialog.Builder(getContext())
                    .setTitle("Soittolistan " + playlist.getName() + " poisto")
                    .setMessage("Haluatko poistaa valitun soittolistan?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            presenter.deletePlaylist(playlist);
                            playlists.remove(playlist);
                            arrayAdapter.notifyDataSetChanged();
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
                            builderInner.setTitle("Raita " + playlist.getName() + " on poistettu");
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
                break;
        }
        return true;
    }

    @Override
    public void onListItemClick(android.widget.ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        Log.d("UUUU", "CallBack");
        if (presenter != null) {
            presenter.openPlaylist(playlists.get(position));
        }
    }

    @Override
    public void onDialogPositiveClick(String newPlayListname) {
        Log.d("UUUU", "GOT MSG " + newPlayListname);
        presenter.savePlaylist(newPlayListname);
        //new MainActivity.SaveAsyncTask(PlayList.savePlayListToDB(AppDatabase.getInstance(getContext()), uusi), this).execute();
    }

    @Override
    public void showPlaylists(List<PlayList> playlists) {
        this.playlists.clear();
        this.playlists.addAll(playlists);
        if(getListAdapter()==null){
            arrayAdapter = new ArrayAdapter<PlayList>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    this.playlists);
            setListAdapter(arrayAdapter);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(PlaylistContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addPlaylist(PlayList pl) {
        playlists.add(pl);
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
    public interface OnPlaylistFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPlaylistFragmentInteractionListener(PlayList playList);
    }

}
