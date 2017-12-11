package com.example.julius.mp3_soitin.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.AsyncTaskListener;
import com.example.julius.mp3_soitin.MainActivity;
import com.example.julius.mp3_soitin.entities.PlayList;
import com.example.julius.mp3_soitin.entities.Track;
import com.example.julius.mp3_soitin.entities.TrackPlaylistJoin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 9.12.2017.
 */

public class ListDialog extends DialogFragment implements AsyncTaskListener{
    private ListDialog.NoticeDialogListener listener;
    private List<PlayList> lista = new ArrayList<PlayList>();
    private ArrayAdapter<PlayList> arrayAdapter;

    public static ListDialog newInstance(Track track) {
        ListDialog listDialog = new ListDialog();
        if(track!=null){
            Bundle args = new Bundle();
            args.putParcelable("track", track);
            listDialog.setArguments(args);
        }
        return listDialog;
    }

    @Override
    public void onTaskCompleted(Object o) {
        lista.addAll((List<PlayList>)o);
        arrayAdapter.notifyDataSetChanged();
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(PlayList list);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(arrayAdapter==null) {
            arrayAdapter = new ArrayAdapter<PlayList>(getContext(), android.R.layout.select_dialog_singlechoice, lista);
            new MainActivity.LoadAsyncTask(PlayList.getAllPlayListsFromDB(AppDatabase.getInstance(getContext())), this).execute();
        }
        Track track = getArguments().getParcelable("track");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setNegativeButton("Takaisin", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ListDialog.this.getDialog().cancel();
                    }
                })
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PlayList playList = arrayAdapter.getItem(which);
                        TrackPlaylistJoin tp = new TrackPlaylistJoin(track.getId(), playList.getId());
                        //AppDatabase.getInstance(getContext()).track_playList_JOIN_Dao().insert(tp);
                        new MainActivity.AsyncTaskNoReturnValue(TrackPlaylistJoin.saveTrackPlayListJoinToDB(AppDatabase.getInstance(getContext()), tp)).execute();
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
                        builderInner.setMessage(playList.getName());
                        builderInner.setTitle("Raita " + track.getName() + " on lisätty soittolistaan " + playList.getName());
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
        return builder.create();
    }

    public void setListener(ListDialog.NoticeDialogListener lis){
        this.listener = lis;
    }
}
