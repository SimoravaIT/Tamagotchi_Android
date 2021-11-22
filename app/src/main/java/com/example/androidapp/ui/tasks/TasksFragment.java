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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.DatabaseHelper;
import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentTasksBinding;



public class TasksFragment extends Fragment  implements AdapterView.OnItemClickListener{


    private FragmentTasksBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final ListView tasks_listView = (ListView) root.findViewById(R.id.ListView_tasks);
        tasks_listView.setOnItemClickListener(this);
        tasks_listView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.task_layout, R.id.label, DatabaseHelper.ObtainTasksNames(getActivity())));


        //can be usefull for View context
        View view = inflater.inflate(R.layout.fragment_tasks,
                container, false);


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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        ListView myListView = (ListView) getView().findViewById(R.id.ListView_tasks);
        TextView t = (TextView) myListView.getChildAt((int) id);
        Log.i("HelloListView", "il nome è--->" + t.getText());
        Log.i("HelloListView", "descrizione  è--->" + DatabaseHelper.
                ObtainTaskDescription(getContext(), String.valueOf(t.getText())));
        String descr=DatabaseHelper.
                ObtainTaskDescription(getContext(), String.valueOf(t.getText()));
        Toast.makeText(getContext(),"DESCRIPTION: " + descr,Toast.LENGTH_LONG).show();
    }

}