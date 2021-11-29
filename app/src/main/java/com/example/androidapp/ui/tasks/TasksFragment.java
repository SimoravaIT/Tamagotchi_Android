package com.example.androidapp.ui.tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidapp.DatabaseController;
import com.example.androidapp.R;
import com.example.androidapp.Task;
import com.example.androidapp.TaskController;
import com.example.androidapp.databinding.FragmentTasksBinding;
import com.example.androidapp.ui.TasksAdapter;

import java.util.ArrayList;
import java.util.List;


public class TasksFragment extends Fragment implements AdapterView.OnItemClickListener{


    private FragmentTasksBinding binding;
    List<Task> tasksList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        DatabaseController databaseHelper = new DatabaseController(root.getContext());

        //listView definition
        final ListView myListView = (ListView) root.findViewById(R.id.ListView_tasks);
        myListView.setOnItemClickListener(this);

        TaskController tc = new TaskController(root.getContext());

        tasksList = databaseHelper.loadAvailableTasks(root.getContext());
        ArrayList<Task> array_list_tasks=new ArrayList<>();
        array_list_tasks.addAll(tasksList);
        TasksAdapter adapter = new TasksAdapter(getActivity(),array_list_tasks);
        myListView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Log.i("TaskFragment", "You clicked Item: " + id + " at position:" + position);
       Task selected_task= tasksList.get(position);
        if(selected_task.isCompleted()==false)
            Toast.makeText(getActivity(),"This task still need to be completed",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(),"You earn " + selected_task.getReward() + "coins",Toast.LENGTH_SHORT).show();
    }
}
