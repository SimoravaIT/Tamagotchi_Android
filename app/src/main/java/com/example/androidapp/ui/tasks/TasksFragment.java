package com.example.androidapp.ui.tasks;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentTasksBinding;

import java.util.ArrayList;


public class TasksFragment extends Fragment {


    private FragmentTasksBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
       // final TextView textView = binding.textNotifications;
       //textView.setText("this is Task fragment");'

        //temp creation of data
        ArrayList<String> listOfTasks =new ArrayList<>();
        for(int i=0;i<20;i++)
            listOfTasks.add("Task numero"+i);
        final ListView myListView = (ListView) root.findViewById(R.id.ListView_tasks);
        final ArrayAdapter <String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,listOfTasks);
        myListView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}