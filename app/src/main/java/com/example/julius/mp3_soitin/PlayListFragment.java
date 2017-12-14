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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.julius.mp3_soitin.Dialogs.QueryDialog;
import com.example.julius.mp3_soitin.entities.PlayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 8.12.2017.
 */

public class PlayListFragment extends ListFragment implements AsyncTaskListener, QueryDialog.NoticeDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<PlayList> playlists = new ArrayList<PlayList>();
    private ArrayAdapter<PlayList> arrayAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnPlaylistFragmentInteractionListener mListener;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        new MainActivity.LoadAsyncTask(PlayList.getAllPlayListsFromDB(AppDatabase.getInstance(getContext())), this).execute();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlaylistFragmentInteractionListener) {
            mListener = (OnPlaylistFragmentInteractionListener) context;
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
    public void onListItemClick(android.widget.ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        Log.d("UUUU", "CallBack");
        if (mListener != null) {
            mListener.onPlaylistFragmentInteractionListener((PlayList) l.getAdapter().getItem(position));
        }
    }

    @Override
    public void onDialogPositiveClick(String newPlayListname) {
        Log.d("UUUU", "GOT MSG " + newPlayListname);
        PlayList uusi = new PlayList(newPlayListname);
        new MainActivity.SaveAsyncTask(PlayList.savePlayListToDB(AppDatabase.getInstance(getContext()), uusi), this).execute();
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
    @Override
    public void onTaskCompleted(Object o) {
        if(o instanceof PlayList){
            playlists.add((PlayList) o);
            arrayAdapter.notifyDataSetChanged();
            return;
        }
        playlists.clear();
        playlists.addAll((List<PlayList>) o);
        arrayAdapter = new ArrayAdapter<PlayList>(
                getContext(),
                android.R.layout.simple_list_item_1,
                playlists);
        setListAdapter(arrayAdapter);
    }

}
