package com.example.androidapp.sensors;

import android.annotation.SuppressLint;

import android.content.Context;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

import com.example.androidapp.DatabaseController;
import com.example.androidapp.ui.report.ReportFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class SensorController {

    private final Date cDate = new Date();
    static int stepsCompleted = 0;

    public SensorController(Context context) {

        Log.d("STEPSENSOR: ", "sensor controller created");
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensorACC = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor mSensorStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        SensorEventListener listener = new StepCounterListener(context);
            if (mSensorACC != null) {
                Log.d("STEPSENSOR: ", "mSensorACC not null");
                mSensorManager.registerListener(listener, mSensorACC, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.d("STEPSENSOR: ", "mSensorACC not  found");
            }
            if (mSensorStepDetector != null) {
                Log.d("STEPSENSOR: ", "mSensorStepDetector not null");
                mSensorManager.registerListener(listener, mSensorStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.d("STEPSENSOR: ", "mSensorStepDetector not found");
            }
        }

    public int dailySteps(Context context){

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String fDate = formatter.format(calendar.getTime());
       // String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        stepsCompleted=DatabaseController.loadSingleStep(context, fDate);
        return stepsCompleted;
    }

    //TODO: method that returns all the steps
    public int totalSteps(){
        int total_steps=0;
        return total_steps;
    }


    class StepCounterListener<stepsCompleted> implements SensorEventListener {
        private final Context cxt;
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
            cxt=context;
            mACCStepCounter=dailySteps(cxt);
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
                    Log.d("ACC STEPS: ", "prova");
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
                        DatabaseController.insertStep( timePointList.get(i),day,hour,cxt);
                    }
                }
            }
        }
        private void countSteps(float step) {
            mAndroidStepCounter += (int) step;
        }
    }

}