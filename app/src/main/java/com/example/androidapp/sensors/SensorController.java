package com.example.androidapp.sensors;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidapp.DatabaseController;
import com.example.androidapp.ui.report.ReportFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SensorController {

    public TextView textNumberTodayStep;

    private Date cDate = new Date();
    private String fDate;
    private Context cxt;
    // ACC sensors.
    private Sensor mSensorACC;
    private SensorManager mSensorManager;
    private SensorEventListener listener;
    private DatabaseController databaseOpenHelper;
    private SQLiteDatabase database;

    // Step Detector sensor
    private Sensor mSensorStepDetector;

    // Completed steps of the date
    static int stepsCompleted = 0;

    public SensorController(Context context,TextView textView) {
        this.cxt = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorACC = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        Log.d("TRYEXEC: ", "vero viene costruito sensorController");
        DatabaseController databaseOpenHelper = new DatabaseController(context);
        database = databaseOpenHelper.getWritableDatabase();
        listener = new StepCounterListener(database, textView);

        Log.d("TRYEXEC: ", "start the listener");
            // Check if the Accelerometer sensor exists
            if (mSensorACC != null) {
                Log.d("TRYEXEC: ", "mSensorACC not null");
                // Register the ACC listener
                mSensorManager.registerListener(listener, mSensorACC, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(context, "Accelerator not available", Toast.LENGTH_SHORT).show();
            }
            // Check if the Step detector sensor exists
            if (mSensorStepDetector != null) {
                Log.d("TRYEXEC: ", "mSensorStepDetector not null");
                // Register the ACC listener
                mSensorManager.registerListener(listener, mSensorStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.d("TRYEXEC: ", "mSensorStepDetector = null");
                Toast.makeText(context, "Step sens not available", Toast.LENGTH_SHORT).show();
            }
        }

    public void SensorController(Activity activity){
        mSensorManager=(SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
    }

    public int dailySteps(){
        fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        stepsCompleted=DatabaseController.loadSingleStep(this.cxt,fDate);
        return stepsCompleted;
    }

    //TODO: method that returns all the steps
    public int totalSteps(){
        int total_steps=0;
        return total_steps;
    }


    class StepCounterListener<stepsCompleted> implements SensorEventListener {

        private long lastUpdate = 0;

        //Get the number of stored steps for the current day
        public int mACCStepCounter = dailySteps();

        ArrayList<Integer> mACCSeries = new ArrayList<Integer>();
        ArrayList<String> mTimeSeries = new ArrayList<String>();

        private double accMag = 0d;
        private int lastXPoint = 1;
        int stepThreshold = 10;

        public int mAndroidStepCounter = 0;

        TextView stepsCountTextView;

        SQLiteDatabase database;
        public String timestamp;
        public String day;
        public String hour;

        // Get the database, TextView and ProgressBar as args
        public StepCounterListener(SQLiteDatabase db, TextView tv) {
            database = db;
            stepsCountTextView = tv;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {

                // Case of the ACC
                case Sensor.TYPE_LINEAR_ACCELERATION:

                    // Get x,y,z
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    //////////////////////////// -- PRINT ACC VALUES -- ////////////////////////////////////
                    // Timestamp
                    long timeInMillis = System.currentTimeMillis() + (event.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000;

                    // Convert the timestamp to date
                    SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    jdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                    String date = jdf.format(timeInMillis);

                /*
                // print a value every 1000 ms
                long curTime = System.currentTimeMillis();
                if ((curTime - lastUpdate) > 1000) {
                    lastUpdate = curTime;

                    Log.d("ACC", "X: " + String.valueOf(x) + " Y: " + String.valueOf(y) + " Z: "
                            + String.valueOf(z) + " t: " + String.valueOf(date));

                }
                */

                    // Get the date, the day and the hour
                    timestamp = date;
                    day = date.substring(0, 10);
                    hour = date.substring(11, 13);

                    ////////////////////////////////////////////////////////////////////////////////////////

                    /// STEP COUNTER ACC ////
                    accMag = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));

                    //Update the Magnitude series
                    mACCSeries.add((int) accMag);

                    //Update the time series
                    mTimeSeries.add(timestamp);


                    // Calculate ACC peaks and steps
                    peakDetection();

                    break;

                // case Step detector
                case Sensor.TYPE_STEP_DETECTOR:

                    // Calculate the number of steps
                    countSteps(event.values[0]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //
        }


        public void peakDetection() {
            int windowSize = 20;

            /* Peak detection algorithm derived from: A Step Counter Service for Java-Enabled Devices Using a Built-In Accelerometer, Mladenov et al.
             */
            int highestValX = mACCSeries.size(); // get the length of the series
            if (highestValX - lastXPoint < windowSize) { // if the segment is smaller than the processing window skip it
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
                dataPointList.add(valuesInWindow.get(p)); // ACC Magnitude data points
                timePointList.add(timesInWindow.get(p)); // Timestamps
            }

            for (int i = 0; i < dataPointList.size(); i++) {
                if (i == 0) {
                } else if (i < dataPointList.size() - 1) {
                    forwardSlope = dataPointList.get(i + 1) - dataPointList.get(i);
                    downwardSlope = dataPointList.get(i) - dataPointList.get(i - 1);

                    if (forwardSlope < 0 && downwardSlope > 0 && dataPointList.get(i) > stepThreshold) {

                        // Update the number of steps
                        mACCStepCounter += 1;
                        //   Log.d("ACC STEPS: ", String.valueOf(mACCStepCounter));

                        // Update the TextView and the ProgressBar
                        stepsCountTextView.setText(String.valueOf(mACCStepCounter));
                        //Insert the data in the database
                        // ContentValues values = new ContentValues();
                        //values.put(DatabaseController.KEY_TIMESTAMP, timePointList.get(i));
                        //values.put(StepAppOpenHelper.KEY_DAY, day);
                        //values.put(StepAppOpenHelper.KEY_HOUR, hour);
                        //database.insert(StepAppOpenHelper.TABLE_NAME, null, values);
                    }

                }
            }
        }

        // Calculate the number of steps from the step detector
        private void countSteps(float step) {
            //Step count
            mAndroidStepCounter += (int) step;
            Log.d("NUM STEPS ANDROID", "Num.steps: " + String.valueOf(mAndroidStepCounter));
        }
    }

}