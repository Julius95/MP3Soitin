package com.example.julius.mp3_soitin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.julius.mp3_soitin.data.entities.Track;

import java.util.List;

/**
 * Created by Julius on 9.12.2017.
 */

public class CustomListAdapter extends ArrayAdapter<Track> {

    private ListFragment callback;

    private int size = 0;


    public CustomListAdapter(Context context, int resource, List<Track> items, ListFragment listener) {
        super(context, resource, items);
        callback = listener;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.tracklist_rowitem, null);

            //Tämä on tärkeä sillä mahdollistaa contextmenun luomisen pitkästä listan elementin painalluksesta
            v.setLongClickable(true);
        }
        //if(getCount()<=position)
           // return v;
        Track track = getItem(position);
        if (track != null) {
            TextView tt1 = v.findViewById(R.id.id);
            TextView tt2 = v.findViewById(R.id.categoryId);
            TextView tt3 = v.findViewById(R.id.description);

            tt1.setText(track.getName());
            int minuutit = track.getLength()/60;
            int sekunnit = track.getLength() % 60;
            tt2.setText(minuutit + " : " + (sekunnit<10 ? "0"+sekunnit : sekunnit));
        }

        if(callback!=null)
            v.setOnClickListener(view -> callback.onListItemClick(callback.getListView(), callback.getView(), position, getItem(position).getId()));

        return v;
    }
}
