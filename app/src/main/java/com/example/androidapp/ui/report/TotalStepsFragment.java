package com.example.androidapp.ui.report;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.androidapp.DatabaseController;
import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentTotalStepsBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import com.anychart.core.cartesian.series.Column;

public class TotalStepsFragment extends Fragment {
    public Map<String, Integer> stepByDays = null;
    public  TextView numStepsTextView;
    public AnyChartView anyChartView;
    private FragmentTotalStepsBinding binding;
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

            numStepsTextView = root.findViewById(R.id.total_steps_stat);
            numStepsTextView.setText(String.valueOf(DatabaseController.loadCountTotalSteps(getContext())));

            anyChartView = root.findViewById(R.id.totalBarChart);
            anyChartView.setProgressBar(root.findViewById(R.id.loadingTotalBar));
            anyChartView.setBackgroundColor("#00000000");
            Cartesian cartesian = createColumnChart();
            anyChartView.setChart(cartesian);

            Button bott =(Button)root.findViewById(R.id.buttonBackTotal);
            bott.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment= new ReportFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            return root;
    }

    private Cartesian createColumnChart() {

        stepByDays=DatabaseController.loadStepsByDay(getContext());
        Map<String, Integer> graph_map = new TreeMap<>();
        graph_map.putAll(stepByDays);
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();
        for (Map.Entry<String,Integer> entry : graph_map.entrySet())
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        Column column = cartesian.column(data);
        column.fill("#1EB980");
        column.stroke("#1EB980");

        column.tooltip()
                .titleFormat("At day: {%X}")
                .format("{%Value}{groupsSeparator: } Steps")
                .anchor(Anchor.RIGHT_TOP);
        column.tooltip().position(Position.RIGHT_TOP).offsetX(0d).offsetY(5);


        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yScale().minimum(0);

        cartesian.yAxis(0).title("number of steps");
        cartesian.xAxis(0).title("Days");
        cartesian.background().fill("#00000000");
        cartesian.animation(true);

        return cartesian;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}