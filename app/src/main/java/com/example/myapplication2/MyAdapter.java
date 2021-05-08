package com.example.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Times> {

    /*String [] times;
    Boolean [] cbox;*/
    Context mcontext;
    MainActivity a;

    public MyAdapter(Context context, ArrayList<Times> times) {
        super(context, 0, times);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Times times = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_items, parent, false);
        }
        // Lookup view for data population
        TextView ttimes = (TextView) convertView.findViewById(R.id.textView);
        //CheckBox tcbox = (CheckBox) convertView.findViewById(R.id.checkalarm);
        // Populate the data into the template view using the data object
        ttimes.setText(times.clock);
        //tcbox.setChecked(true);




        return convertView;
    }




}
