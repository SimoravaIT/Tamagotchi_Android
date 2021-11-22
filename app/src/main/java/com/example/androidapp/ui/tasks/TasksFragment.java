package com.example.androidapp.ui.tasks;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.DatabaseHelper;
import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentTasksBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class TasksFragment extends Fragment {


    private FragmentTasksBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DatabaseHelper databaseHelper = new DatabaseHelper(root.getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        //listView definition
        final ListView myListView = (ListView) root.findViewById(R.id.ListView_tasks);

        myListView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.task_layout, R.id.label, ObtainTasks()));

        //can be usefull for View context
        View view = inflater.inflate(R.layout.fragment_tasks,
                container, false);


        return root;
    }

    private List<String> ObtainTasks() {
        List<String> temp = new LinkedList<String>();
        String[] col={DatabaseHelper.KEY_NAME};
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());//not sure getContext, check
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query(DatabaseHelper.TABLE_TASK_NAME,
                col, null, null, null,
                null, null );
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++) {
            temp.add(cursor.getString(0));
            cursor.moveToNext();
        }

            //verification of numbers
            Integer numTask = temp.size();
            Toast.makeText(getActivity(),"added" + String.valueOf(temp) + " tasks to the list",Toast.LENGTH_LONG).show();


            database.close();
            return temp;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}