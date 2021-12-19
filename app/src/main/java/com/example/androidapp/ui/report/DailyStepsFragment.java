package com.example.androidapp.ui.report;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.example.androidapp.databinding.FragmentDailyStepsBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class DailyStepsFragment extends Fragment {

    public Map<Integer, Integer> stepsByHour = null;
    public AnyChartView anyChartView;
    private FragmentDailyStepsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }

        binding = FragmentDailyStepsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String fDate = formatter.format(calendar.getTime());

        anyChartView = root.findViewById(R.id.hourBarChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingBar));
        Cartesian cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian);

        Button bott =(Button)root.findViewById(R.id.buttonBackDay);
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    //it must return cartesian
    public  Cartesian createColumnChart(){

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String fDate = formatter.format(calendar.getTime());
        stepsByHour=DatabaseController.loadStepsByHours(getContext(),fDate);
        Map<Integer, Integer> graph_map = new TreeMap<>();
        for(int i=0;i<24;i++){
            graph_map.put(i,0);
        }
        graph_map.putAll(stepsByHour);
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();

        for (Map.Entry<Integer,Integer> entry : graph_map.entrySet())
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        Column column = cartesian.column(data);
        column.fill("#74c5d6");
        column.stroke("#74c5d6");
        column.tooltip()
                .titleFormat("At hour: {%X}")
                .format("{%Value}{groupsSeparator: } Steps")
                .anchor(Anchor.RIGHT_TOP)
                .background("#da0a95");
        column.tooltip().position(Position.RIGHT_TOP).offsetX(0d).offsetY(0);


        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yScale().minimum(0);

        cartesian.yAxis(0).title("number of steps");
        cartesian.xAxis(0).title("Hour");
        cartesian.background().fill("#00000000");
        cartesian.animation(true);

        return cartesian;
    }

}