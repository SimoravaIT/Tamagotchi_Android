package com.example.androidapp.ui.report;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChartView;
import com.example.androidapp.DatabaseController;
import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentTotalStepsBinding;

import java.util.Map;

public class MonthStepFragment extends Fragment {
    private FragmentTotalStepsBinding binding;
    public Map<String, Integer> stepByDays = null;
    public TextView numStepsTextView;
    public AnyChartView anyChartView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }

        binding = FragmentTotalStepsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);


        numStepsTextView = root.findViewById(R.id.month_steps_stat);
      //  numStepsTextView.setText(String.valueOf(DatabaseController.loadCountTotalSteps(getContext())));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}