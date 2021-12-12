package com.example.androidapp.ui;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androidapp.R;
import com.example.androidapp.Task;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;

public class TasksAdapter extends ArrayAdapter<Task> {
    private final Context context;
    private final ArrayList<Task> values;

    public TasksAdapter(Context context, ArrayList<Task> values) {
        super(context, R.layout.new_task_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.new_task_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.taskTitle);
        TextView textView2 = (TextView) rowView.findViewById(R.id.taskReward);
        textView.setText((CharSequence) values.get(position).getDescription());
        ConstraintLayout constraintLayout = (ConstraintLayout) rowView.findViewById(R.id.taskRow);

        textView2.setText(String.valueOf(values.get(position).getReward()));

        //TODO: modify the color when completed
        if(values.get(position).isCompleted()) {
            textView.setBackgroundResource(R.drawable.task_background_curve);
            textView.setTextColor(Color.parseColor("#ffffff"));
        }
        return rowView;
    }
}
