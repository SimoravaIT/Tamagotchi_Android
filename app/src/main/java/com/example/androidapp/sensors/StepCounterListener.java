package com.example.androidapp.sensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.SystemClock;
import android.util.Log;

import com.example.androidapp.DatabaseController;
import com.example.androidapp.ui.report.ReportFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

class StepCounterListener<stepsCompleted> implements SensorEventListener {
    private Context context;
    public int mACCStepCounter ;
    ArrayList<Integer> mACCSeries = new ArrayList<Integer>();
    ArrayList<String> mTimeSeries = new ArrayList<String>();
    private int lastXPoint = 1;
    int stepThreshold = 10;
    public int mAndroidStepCounter = 0;
    public String timestamp;
    public String day;
    public String hour;


    public StepCounterListener(Context context) {
        this.context = context;

        // Load steps done today from the DB
        mACCStepCounter = SensorController.getDailySteps(context);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {

            case Sensor.TYPE_LINEAR_ACCELERATION:
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                long timeInMillis = System.currentTimeMillis() + (event.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                jdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                String date = jdf.format(timeInMillis);
                timestamp = date;
                day = date.substring(0, 10);
                hour = date.substring(11, 13);
                double accMag = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
                mACCSeries.add((int) accMag);
                mTimeSeries.add(timestamp);
                peakDetection();
                break;

            case Sensor.TYPE_STEP_DETECTOR:
                countSteps(event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void peakDetection() {
        int windowSize = 20;

        int highestValX = mACCSeries.size();
        if (highestValX - lastXPoint < windowSize) {
            return;
        }
        List<Integer> valuesInWindow = mACCSeries.subList(lastXPoint, highestValX);
        List<String> timesInWindow = mTimeSeries.subList(lastXPoint, highestValX);

        lastXPoint = highestValX;

        int forwardSlope = 0;
        int downwardSlope = 0;

        List<Integer> dataPointList = new ArrayList<Integer>();
        List<String> timePointList = new ArrayList<String>();

        for (int p = 0; p < valuesInWindow.size(); p++) {
            dataPointList.add(valuesInWindow.get(p));
            timePointList.add(timesInWindow.get(p));
        }

        for (int i = 1; i < dataPointList.size(); i++) {
            if (i < dataPointList.size() - 1) {
                forwardSlope = dataPointList.get(i + 1) - dataPointList.get(i);
                downwardSlope = dataPointList.get(i) - dataPointList.get(i - 1);

                if (forwardSlope < 0 && downwardSlope > 0 && dataPointList.get(i) > stepThreshold) {
                    mACCStepCounter += 1;
                    ReportFragment.showDailySteps(mACCStepCounter);
                    ReportFragment.showMonthlySteps(SensorController.getMonthlySteps(context));
                    ReportFragment.showTotalSteps(SensorController.getTotalSteps(context));
                    DatabaseController.insertStep(timePointList.get(i), day, hour, this.context);
                }
            }
        }
    }
    private void countSteps(float step) {
        mAndroidStepCounter += (int) step;
    }
}
