package com.example.androidapp.sensors;

import android.annotation.SuppressLint;

import android.content.Context;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.core.math.MathUtils;

import com.example.androidapp.DatabaseController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;

public class SensorController {
    static int stepsCompleted = 0;

    public SensorController(Context context) {

        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensorACC = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor mSensorStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor mSensorTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        SensorEventListener stepListener = new StepCounterListener(context);


        if (mSensorStepDetector != null) {
            Log.d("SENSOR: ", "mSensorStepDetector not null");
            mSensorManager.registerListener(stepListener, mSensorStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.d("SENSOR: ", "mSensorStepDetector not found");
            if (mSensorACC != null) {
                Log.d("SENSOR: ", "mSensorACC not null");
                mSensorManager.registerListener(stepListener, mSensorACC, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.d("SENSOR: ", "mSensorACC not  found");
            }
        }

        SensorEventListener tempListener = new TemperatureListener(context);
        if(mSensorTemperature!=null){
            Log.d("SENSOR: ", "mSensorTemperature is not null");
            mSensorManager.registerListener(tempListener, mSensorTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        } else{
            Log.d("SENSOR: ", "mSensorTemperature not  found");
        }


    }

    public static int getDailySteps(Context context){
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String fDate = formatter.format(calendar.getTime());

        stepsCompleted=DatabaseController.loadStepsForTheDay(context, fDate);

        return stepsCompleted;
    }
    public static int getMonthlySteps(Context context){
        long timeInMillis1 = System.currentTimeMillis();
        long timeInMillis2 = System.currentTimeMillis()-(long)30*1000*60*60*24;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String dateEnd = jdf.format(timeInMillis1).substring(0, 10);;
        String dateStart = jdf.format(timeInMillis2).substring(0, 10);;

        Map<String,Integer> temp = DatabaseController.loadStepsByDates(context,dateStart,dateEnd);
        return temp.values().stream().mapToInt(Integer::intValue).sum();
    }

    public static int getTotalSteps(Context context){
        return DatabaseController.loadCountTotalSteps(context);
    }

    public static int getWeeklySteps(Context context) {
        long timeInMillis1 = System.currentTimeMillis();
        long timeInMillis2 = System.currentTimeMillis()-(long)7*1000*60*60*24;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String dateEnd = jdf.format(timeInMillis1).substring(0, 10);;
        String dateStart = jdf.format(timeInMillis2).substring(0, 10);;
        Map<String,Integer> temp = DatabaseController.loadStepsByDates(context,dateStart,dateEnd);
        return temp.values().stream().mapToInt(Integer::intValue).sum();
    }
}