package com.example.androidapp.ui.report;

import android.annotation.SuppressLint;
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
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.androidapp.DatabaseController;
import com.example.androidapp.R;
import com.example.androidapp.databinding.FragmentMonthStepBinding;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class MonthStepFragment extends Fragment {
    public Map<String, Integer> stepsby30Days = null;
    private FragmentMonthStepBinding binding;

    public AnyChartView anyChartView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }

        binding = FragmentMonthStepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);


        anyChartView = root.findViewById(R.id.monthBarChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingMonthBar));
        Cartesian cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian);

        Button bott =(Button)root.findViewById(R.id.buttonBackMonth);
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
    public  Cartesian createColumnChart(){
        Cartesian cartesian = AnyChart.column();
        long timeInMillis1 = System.currentTimeMillis();
        long timeInMillis2 = System.currentTimeMillis()-(long)30*1000*60*60*24;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String dateEnd = jdf.format(timeInMillis1).substring(0, 10);
        String dateStart = jdf.format(timeInMillis2).substring(0, 10);

        stepsby30Days=DatabaseController.loadStepsByDates(getContext(),dateStart,dateEnd);
        Map<String, Integer> graph_map = new TreeMap<>();
        graph_map.putAll(stepsby30Days);
        List<DataEntry> data = new ArrayList<>();
        for (Map.Entry<String,Integer> entry : graph_map.entrySet())
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        Column column = cartesian.column(data);
        column.fill("#74c5d6");
        column.stroke("#74c5d6");

        column.tooltip()
                .titleFormat("{%X}")
                .format("{%Value}{groupsSeparator: } Steps")
                .anchor(Anchor.RIGHT_TOP)
                .background("#da0a95");
        column.tooltip().position(Position.RIGHT_TOP).offsetX(0d).offsetY(0);


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