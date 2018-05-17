package com.example.julius.mp3_soitin.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.julius.mp3_soitin.R;
import com.example.julius.mp3_soitin.filescanning.BadFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Created by Julius on 26.3.2018.
 */

public class SimpleListDialog extends DialogFragment {
    private List<BadFile> lista = new ArrayList<>();
    private ArrayAdapter<BadFile> arrayAdapter;

    /**
     * @return
     */
    public static SimpleListDialog newInstance(ArrayList<BadFile> list, Serializable callback) {
        SimpleListDialog listDialog = new SimpleListDialog();
        if(list!=null){
            Bundle args = new Bundle();
            args.putParcelableArrayList("list", list);
            //Consumer<Object> o = (oi) -> {};
            Serializable r = (Consumer<Object> & Serializable)callback;
            //Serializable c = (Serializable & Consumer<Object>);
            args.putSerializable("consumer", callback);
            listDialog.setArguments(args);
        }
        return listDialog;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List list = getArguments().getParcelableArrayList("list");
        Consumer<List<BadFile>> consumer = (Consumer<List<BadFile>>) getArguments().getSerializable("consumer");
        if(arrayAdapter==null) {
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice, this.lista);
            lista.addAll(list);
            arrayAdapter.notifyDataSetChanged();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = inflater.inflate(R.layout.badfile_row_layout, null);
        builder.setView(convertView);
        ListView lv = convertView.findViewById(R.id.listViewBadFiles);
        lv.setAdapter(arrayAdapter);
        // Add action buttons
        builder.setPositiveButton("Tallenna vioista huolimatta", (dialog, id) -> {
            CompletableFuture.supplyAsync(() -> lista).thenAccept(consumer);
        })
        .setNegativeButton("Älä tallenna", (dialog, id) -> SimpleListDialog.this.getDialog().cancel());
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        return builder.create();
    }
}
