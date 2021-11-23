package com.example.androidapp.ui.tasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.LinkedList;
import java.util.List;


public class TasksFragment extends Fragment {


    private FragmentTasksBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DatabaseController databaseHelper = new DatabaseController(root.getContext());

        //listView definition
        final ListView myListView = (ListView) root.findViewById(R.id.ListView_tasks);

        TaskController tc = new TaskController(root.getContext());

        List<Task> tasks = databaseHelper.loadAvailableTasks(root.getContext());

        //can be useful for View context
        View view = inflater.inflate(R.layout.fragment_tasks,
                container, false);
        return root;

        //TODO: Implement the AVAILABLE (!!) tasks visualization.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}