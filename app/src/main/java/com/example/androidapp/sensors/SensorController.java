package com.example.androidapp.sensors;

import android.annotation.SuppressLint;

import android.content.Context;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.androidapp.DatabaseController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class SensorController {
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

    public static int getDailySteps(Context context){

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String fDate = formatter.format(calendar.getTime());

        stepsCompleted=DatabaseController.loadStepsForTheDay(context, fDate);

        return stepsCompleted;
    }
    public static int getMonthlySteps(Context context){
        long timeInMillis1 = System.currentTimeMillis();
        long timeInMillis2 = System.currentTimeMillis()-(long)30*1000*60*60*24;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String dateEnd = jdf.format(timeInMillis1).substring(0, 10);;
        String dateStart = jdf.format(timeInMillis2).substring(0, 10);;
        Log.d("DATES","range searched->  "+dateStart+"  to  "+dateEnd);
        return DatabaseController.loadStepsBetweenDates(context,dateStart,dateEnd);
    }
    public static int getTotalSteps(Context context){
        return DatabaseController.loadCountTotalSteps(context);
    }
}