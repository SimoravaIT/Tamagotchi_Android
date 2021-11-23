package com.example.androidapp.ui.tasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidapp.DatabaseController;
import com.example.androidapp.R;
import com.example.androidapp.Task;
import com.example.androidapp.TaskController;
import com.example.androidapp.databinding.FragmentTasksBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class TasksFragment extends Fragment implements AdapterView.OnItemClickListener{


    private FragmentTasksBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DatabaseController databaseHelper = new DatabaseController(root.getContext());

        //listView definition
        final ListView myListView = (ListView) root.findViewById(R.id.ListView_tasks);
        myListView.setOnItemClickListener(this);

        TaskController tc = new TaskController(root.getContext());

        List<Task> tasks = databaseHelper.loadAvailableTasks(root.getContext());
        ArrayList<String> list_tasks_avaiable=tasks_names(tasks);
        myListView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.task_layout, R.id.label,list_tasks_avaiable));
        //can be useful for View context
        View view = inflater.inflate(R.layout.fragment_tasks,
                container, false);
        return root;

        //TODO: Implement the AVAILABLE (!!) tasks visualization.
    }

    private ArrayList tasks_names(List<Task> list) {
        ArrayList<String> array_temp = new ArrayList<>();
        for(Task t:list){
            array_temp.add(t.getKey());
        }
        return array_temp;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        //  ListView myListView = (ListView) getView().findViewById(R.id.ListView_tasks);
        //Log.i("HelloListView", "You are here: "+myListView.getChildCount());
        //LinearLayout ll = (LinearLayout) myListView.getChildAt(position);
        //MaterialTextView text = (MaterialTextView) ll.getChildAt(0);
        //Log.i("HelloListView", "my name is --->" + text.getText());
        //Log.i("HelloListView", "my description is--->" + DatabaseHelper.
        //      ObtainTaskDescription(getContext(), String.valueOf(text.getText())));
        //String descr=DatabaseHelper.
        //      ObtainTaskDescription(getContext(), String.valueOf(text.getText()));

        //Toast.makeText(getActivity(),"DESCRIPTION: " + descr,Toast.LENGTH_LONG).show();
    }
}
