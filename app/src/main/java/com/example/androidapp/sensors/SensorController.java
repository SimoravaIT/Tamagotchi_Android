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
}