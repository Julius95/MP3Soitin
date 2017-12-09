package com.example.julius.mp3_soitin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * Created by Julius on 9.12.2017.
 */

public class QueryDialog extends DialogFragment {

    private NoticeDialogListener listener;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(String newPlayListname);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.layout_dialog, null))
                // Add action buttons
                .setPositiveButton("Lisää", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(listener!=null){
                            Dialog dialogObj =Dialog.class.cast(dialog);
                            EditText etUsr=(EditText) dialogObj.findViewById(R.id.playlistName);
                            listener.onDialogPositiveClick(etUsr.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Takaisin", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        QueryDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    /*public static QueryDialog newInstance(NoticeDialogListener lis) {
        QueryDialog queryDialog = new QueryDialog();
        if(lis!=null){
            Bundle args = new Bundle();
            args.
            queryDialog.setArguments(args);
        }
        return queryDialog;
    }*/

    public void setListener(NoticeDialogListener lis){
        this.listener = lis;
    }

}
