package com.example.androidapp.ui;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androidapp.R;
import com.example.androidapp.Task;

import java.util.ArrayList;

public class TasksAdapter extends ArrayAdapter<Task> {
    private final Context context;
    private final ArrayList<Task> values;

    public TasksAdapter(Context context, ArrayList<Task> values) {
        super(context, R.layout.task_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.task_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView textView2 = (TextView) rowView.findViewById(R.id.collect);
        textView.setText((CharSequence) values.get(position).getDescription());
        RelativeLayout relativeLayout = (RelativeLayout) rowView.findViewById(R.id.relative_task);
        // Change the icon for Windows and iPhone
        textView2.setText("coins: "+String.valueOf(values.get(position).getReward()));
        if(values.get(position).isCompleted())
            relativeLayout.setBackgroundColor(Color.GREEN);

        return rowView;
    }
}
