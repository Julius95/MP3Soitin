package com.example.julius.mp3_soitin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.julius.mp3_soitin.entities.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julius on 9.12.2017.
 */
//https://stackoverflow.com/questions/40862154/how-to-create-listview-items-button-in-each-row
public class CustomListAdapter extends BaseAdapter implements ListAdapter {
    private List<Track> list = new ArrayList<Track>();
    private Context context;

    public CustomListAdapter(List<Track> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Track getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return list.get(pos).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.tracklist_rowitem, null);
        }

        //Handle TextView and display string from your list
        TextView tvContact= (TextView)view.findViewById(R.id.list_item_string);
        tvContact.setText(list.get(position).toString());

        //Handle buttons and add onClickListeners
        Button callbtn= (Button)view.findViewById(R.id.add_btn);

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Log.d("UUUU", "BUTTON LIST");
            }
        });

        return view;
    }
}
