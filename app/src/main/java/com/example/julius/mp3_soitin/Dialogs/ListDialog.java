package com.example.julius.mp3_soitin.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.AsyncTaskListener;
import com.example.julius.mp3_soitin.MainActivity;
import com.example.julius.mp3_soitin.entities.PlayList;
import com.example.julius.mp3_soitin.entities.Track;
import com.example.julius.mp3_soitin.entities.TrackPlaylistJoin;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Created by Julius on 9.12.2017.
 */

public class ListDialog extends DialogFragment implements AsyncTaskListener{
    private ListDialog.NoticeDialogListener listener;
    private List<PlayList> lista = new ArrayList<PlayList>();
    private ArrayAdapter<PlayList> arrayAdapter;

    /**
     * Tämä staattinen factory-metodi tarkoitettu raidan soittolista tietojen hakemiseen
     * fetchFromWhereTrackNotPresent on boolean arvo, joka määrä etsitäänkö soittolistoja joissa ko. raita on(true) vai ei(false)
     * @param track
     * @param fetchFromWhereTrackNotPresent
     * @return
     */
    public static ListDialog newInstance(Track track , boolean fetchFromWhereTrackNotPresent) {
        ListDialog listDialog = new ListDialog();
        if(track!=null){
            Bundle args = new Bundle();
            args.putParcelable("track", track);
            args.putBoolean("FetchingMethod", fetchFromWhereTrackNotPresent);
            listDialog.setArguments(args);
        }
        return listDialog;
    }

    @Override
    public void onTaskCompleted(Object o) {
        lista.addAll((List<PlayList>)o);
        /*for(PlayList pl : lista){
            Log.d("UUUU", "Ennen " + pl.getName()+ " " + pl.getId() + " exc id : " + excludedIds[0]);
        }
        List<PlayList> temp = lista.stream().filter(x -> Arrays.stream(excludedIds).noneMatch( e -> Long.compare(x.getId(), e) == 0)).collect(toList());
        for(PlayList pl : temp){
            Log.d("UUUU", "Jälkeen " + pl.getName()+ " " + pl.getId());
        }
        lista.clear();
        lista.addAll(temp);*/
        arrayAdapter.notifyDataSetChanged();
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(PlayList list);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Track track = getArguments().getParcelable("track");
        boolean mode = getArguments().getBoolean("FetchingMethod");
        //excludedIds = getArguments().getLongArray("excluded");
        if(arrayAdapter==null) {
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice, lista);
            if(mode){
                new MainActivity.LoadAsyncTask(TrackPlaylistJoin.getAcceptablePlaylistsForTrack(AppDatabase.getInstance(getContext()), track), this).execute();
            }else{
                new MainActivity.LoadAsyncTask(TrackPlaylistJoin.getPlaylistsThatIncludeTrack(AppDatabase.getInstance(getContext()), track), this).execute();
            }

        }
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
                        AlertDialog.Builder builderInner;
                        if(mode){
                            TrackPlaylistJoin tp = new TrackPlaylistJoin(track.getId(), playList.getId());
                            new MainActivity.AsyncTaskNoReturnValue(TrackPlaylistJoin.saveTrackPlayListJoinToDB(AppDatabase.getInstance(getContext()), tp)).execute();
                            builderInner = new AlertDialog.Builder(getContext());
                            builderInner.setMessage(playList.getName());
                            builderInner.setTitle("Raita " + track.getName() + " on lisätty soittolistaan " + playList.getName());
                        }else{
                            new MainActivity.AsyncTaskNoReturnValue(TrackPlaylistJoin.deleteTrackPlayListJoin(AppDatabase.getInstance(getContext()), track, playList)).execute();
                            builderInner = new AlertDialog.Builder(getContext());
                            builderInner.setMessage(playList.getName());
                            builderInner.setTitle("Raita " + track.getName() + " on poistettu soittolistasta " + playList.getName());
                        }
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
