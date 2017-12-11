package com.example.julius.mp3_soitin;

import android.content.Context;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.julius.mp3_soitin.Dialogs.ListDialog;
import com.example.julius.mp3_soitin.entities.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 9.12.2017.
 */

public class CustomListAdapter extends ArrayAdapter<Track> {

    private ListFragment callback;

    public CustomListAdapter(Context context, int textViewResourceId, ListFragment listener ) {
        super(context, textViewResourceId);
        callback = listener;
    }

    public CustomListAdapter(Context context, int resource, List<Track> items, ListFragment listener) {
        super(context, resource, items);
        callback = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.tracklist_rowitem, null);
        }
        Track track = getItem(position);
        if (track != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.id);
            TextView tt2 = (TextView) v.findViewById(R.id.categoryId);
            TextView tt3 = (TextView) v.findViewById(R.id.description);

            tt1.setText(track.getName());

            Button callbtn = (Button)v.findViewById(R.id.add_btn);

            callbtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    Log.d("UUUU", "BUTTON LIST");
                    ListDialog dialog = ListDialog.newInstance(track);
                    if(callback!=null && callback instanceof ListDialog.NoticeDialogListener)
                        dialog.setListener((ListDialog.NoticeDialogListener) callback);
                    //dialog.setListener(this);
                    dialog.show(callback.getActivity().getSupportFragmentManager(), "NoticeDialogFragment");
                }
            });
        }

        if(callback!=null)
            v.setOnClickListener(view -> callback.onListItemClick(callback.getListView(), callback.getView(), position, getItem(position).getId()));

        return v;
    }
}
