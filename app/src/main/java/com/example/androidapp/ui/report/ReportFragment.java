package com.example.androidapp.ui.report;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidapp.DatabaseController;
import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentReportBinding;
import com.example.androidapp.sensors.SensorController;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;
    public static TextView steps_taskView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SensorController sensor_controller=new SensorController(root.getContext());

        steps_taskView = (TextView) root.findViewById(R.id.numberTodayStep);
        steps_taskView.setText(String.valueOf(sensor_controller.dailySteps(getContext())));

        return root;
    }

    public static void showDailySteps(int value) {
        steps_taskView.setText(String.valueOf(value));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
