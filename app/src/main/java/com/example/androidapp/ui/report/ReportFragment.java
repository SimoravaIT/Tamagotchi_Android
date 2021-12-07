package com.example.androidapp.ui.report;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentReportBinding;
import com.example.androidapp.sensors.SensorController;
import com.google.android.material.bottomnavigation.BottomNavigationView;


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
        container.removeAllViews();
        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);
        dailyStepsTaskView = (TextView) root.findViewById(R.id.numberTodayStep);
        monthlyStepsTaskView =(TextView) root.findViewById(R.id.numberMonthStep);
        totalStepsTaskView=(TextView) root.findViewById(R.id.numberTotalStep);

        totalStepsTaskView.setText(String.valueOf(SensorController.getTotalSteps(getContext())));
        monthlyStepsTaskView.setText(String.valueOf(SensorController.getMonthlySteps(getContext())));
        dailyStepsTaskView.setText(String.valueOf(SensorController.getDailySteps(getContext())));

        dailyStepsTaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("changefragment","i clicked the steps of today");

                Fragment fragment= new DailyStepsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//Intent intent = new Intent(getActivity(), MainActivity2.class);
                //startActivity(intent);

            }
        });
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

