package com.example.androidapp.ui.report;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentReportBinding;
import com.example.androidapp.sensors.SensorController;


public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static TextView dailyStepsTaskView;
    @SuppressLint("StaticFieldLeak")
    private static TextView monthlyStepsTaskView;
    @SuppressLint("StaticFieldLeak")
    private static TextView totalStepsTaskView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dailyStepsTaskView = (TextView) root.findViewById(R.id.numberTodayStep);
        monthlyStepsTaskView =(TextView) root.findViewById(R.id.numberMonthStep);
        totalStepsTaskView=(TextView) root.findViewById(R.id.numberTotalStep);

        totalStepsTaskView.setText(String.valueOf(SensorController.getTotalSteps(getContext())));
        monthlyStepsTaskView.setText(String.valueOf(SensorController.getMonthlySteps(getContext())));
        dailyStepsTaskView.setText(String.valueOf(SensorController.getDailySteps(getContext())));

        return root;
    }

    public static void showDailySteps(int value) {

        dailyStepsTaskView.setText(String.valueOf(value));
    }

    public static void showMonthlySteps(int value){

        monthlyStepsTaskView.setText(String.valueOf(value));
    }
    public static void showTotalSteps(int value){

        totalStepsTaskView.setText(String.valueOf(value));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}

