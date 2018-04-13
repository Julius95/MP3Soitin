package com.example.julius.mp3_soitin.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.example.julius.mp3_soitin.MainActivity;
import com.example.julius.mp3_soitin.data.entities.PlayList;
import com.example.julius.mp3_soitin.data.entities.Track;
import com.example.julius.mp3_soitin.data.entities.TrackPlaylistJoin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Custom listdialog used to show tracks
 */

public class ListDialog extends DialogFragment{
    private List<PlayList> lista = new ArrayList<>();
    private ArrayAdapter<PlayList> arrayAdapter;

    /**
     * Tämä staattinen factory-metodi tarkoitettu raidan soittolista tietojen hakemiseen
     * fetchFromWhereTrackNotPresent on boolean arvo, joka määrä etsitäänkö soittolistoja joissa ko. raita on(true) vai ei(false)
     * @param track
     * @param fetchFromWhereTrackNotPresent
     * @return
     */
    public static ListDialog newInstance(DialogTrackPlaylistUseCase usecase, Track track , boolean fetchFromWhereTrackNotPresent) {
        ListDialog listDialog = new ListDialog();
        if(track!=null){
            Bundle args = new Bundle();
            args.putParcelable("track", track);
            args.putBoolean("FetchingMethod", fetchFromWhereTrackNotPresent);
            args.putSerializable("usecase", usecase);
            listDialog.setArguments(args);
        }
        return listDialog;
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(PlayList list);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Track track = getArguments().getParcelable("track");
        boolean mode = getArguments().getBoolean("FetchingMethod");
        DialogTrackPlaylistUseCase usecase = (DialogTrackPlaylistUseCase)getArguments().getSerializable("usecase");
        if(arrayAdapter==null) {
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice, lista);



            if(mode){
                usecase.getPlayListsThatCanAcceptThisTrack((List<PlayList> result) -> {
                    lista.addAll(result);
                    arrayAdapter.notifyDataSetChanged();
                }, track);
            }else{
                usecase.getPlaylistsThatIncludeTrack((List<PlayList> result) -> {
                    lista.addAll(result);
                    arrayAdapter.notifyDataSetChanged();
                }, track);
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
                            usecase.saveTrackToPlaylist(track, playList);
                            builderInner = new AlertDialog.Builder(getContext());
                            builderInner.setMessage(playList.getName());
                            builderInner.setTitle("Raita " + track.getName() + " on lisätty soittolistaan " + playList.getName());
                        }else{
                            usecase.deleteTrackFromPlaylist(track, playList);
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
}
