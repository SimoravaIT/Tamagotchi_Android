package com.example.androidapp.ui.report;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentReportBinding;
import com.example.androidapp.sensors.SensorController;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static TextView dailyStepsTaskView;
    @SuppressLint("StaticFieldLeak")
    private static TextView monthlyStepsTaskView;
    @SuppressLint("StaticFieldLeak")
    private static TextView weeklyStepsTaskView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        dailyStepsTaskView = (TextView) root.findViewById(R.id.numberTodayStep);
        weeklyStepsTaskView=(TextView) root.findViewById(R.id.numberWeekSteps);
        monthlyStepsTaskView =(TextView) root.findViewById(R.id.numberMonthStep);

        dailyStepsTaskView.setText(String.valueOf(SensorController.getDailySteps(getContext())));
        weeklyStepsTaskView.setText(String.valueOf(SensorController.getWeeklySteps(getContext())));
        monthlyStepsTaskView.setText(String.valueOf(SensorController.getMonthlySteps(getContext())));

        dailyStepsTaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment= new DailyStepsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        weeklyStepsTaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new WeekStepsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        monthlyStepsTaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new MonthStepFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return root;
    }

    public static void showDailySteps(int value) {
        dailyStepsTaskView.setText(String.valueOf(value));
    }

    public static void showWeeklySteps(int value){
        weeklyStepsTaskView.setText(String.valueOf(value));
    }

    public static void showMonthlySteps(int value){
        monthlyStepsTaskView.setText(String.valueOf(value));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}

