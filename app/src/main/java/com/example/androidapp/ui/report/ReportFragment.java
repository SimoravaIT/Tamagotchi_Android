package com.example.androidapp.ui.report;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.DatabaseHelper;
import com.example.androidapp.databinding.FragmentReportBinding;

import java.util.LinkedList;
import java.util.List;


public class ReportFragment extends Fragment {


    private FragmentReportBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView textView = binding.textDashboard;
        textView.setText("this is Report Fragment");


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}