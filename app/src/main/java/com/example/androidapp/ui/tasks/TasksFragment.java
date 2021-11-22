package com.example.androidapp.ui.tasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
        //SQLiteDatabase database = databaseHelper.getReadableDatabase();

        //listView definition
        final ListView myListView = (ListView) root.findViewById(R.id.ListView_tasks);

        //myListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.task_layout, R.id.label, databaseHelper.loadTasks(getContext())));
        databaseHelper.loadTasks(root.getContext());
        //can be usefull for View context
        View view = inflater.inflate(R.layout.fragment_tasks,
                container, false);

        //temp button for refresh
        //Button button = (Button) root.findViewById(R.id.button_populate);

    /*
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,ObtainTasks()));
            }
        });
    */

    //add datas to the db by force it
    /*
        DatabaseHelper databaseHelper = new DatabaseHelper(root.getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_NAME,"nomee");
        values.put(DatabaseHelper.KEY_DESCRIPTION, "descrizione");
        values.put(DatabaseHelper.KEY_REWARD, 400);
        values.put(DatabaseHelper.KEY_STEPS, 300);
        values.put(DatabaseHelper.KEY_LOCATION, "afgan");
        database.insert(DatabaseHelper.TABLE_TASK_NAME, null, values);
    */
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}