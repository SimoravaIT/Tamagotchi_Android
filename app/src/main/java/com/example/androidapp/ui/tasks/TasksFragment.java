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
       // final TextView textView = binding.textNotifications;
       //textView.setText("this is Task fragment");'

        //temp creation of data
        ArrayList<String> listOfTasks =new ArrayList<>();
        for(int i=0;i<20;i++)
            listOfTasks.add("Task numero"+i);
        final ListView myListView = (ListView) root.findViewById(R.id.ListView_tasks);
        final ArrayAdapter <String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,listOfTasks);
        myListView.setAdapter(adapter);

        View view = inflater.inflate(R.layout.fragment_tasks,
                container, false);
        Button button = (Button) root.findViewById(R.id.button_populate);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                List<String> tasks_from_db = new LinkedList<String>();
                // Get the readable database
                String[] col={DatabaseHelper.KEY_NAME};
                Cursor cursor = database.query(DatabaseHelper.TABLE_TASK_NAME,
                        col, null, null, null,
                        null, null );

                // iterate over returned elements
                cursor.moveToFirst();
                for (int index=0; index < cursor.getCount(); index++){
                    tasks_from_db.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                database.close();

                Integer numTask = tasks_from_db.size();

                button.setText(String.valueOf(numTask));

                final ArrayAdapter <String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,tasks_from_db);
                myListView.setAdapter(adapter);


            }
        });

//add datas to the db by force it
      /*  DatabaseHelper databaseHelper = new DatabaseHelper(root.getContext());
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