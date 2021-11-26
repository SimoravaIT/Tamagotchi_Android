package com.example.androidapp.sensors;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import com.example.androidapp.DatabaseController;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorController  implements SensorEventListener{

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

    // Completed steps
    static int stepsCompleted = 0;

    public SensorController(Context context) {
        this.cxt=context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorACC = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Log.d("TRYEXEC: ", "vero viene costruito sensorController");
        DatabaseController databaseOpenHelper =   new DatabaseController(context);
        database = databaseOpenHelper.getWritableDatabase();
        listener = new StepCounterListener();
    }


    public void SensorController(Activity activity){
        mSensorManager=(SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
    }

    public int dailySteps(){
        fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        return DatabaseController.loadSingleStep(this.cxt,fDate);
    }
    public int totalSteps(){
        int total_steps=0;
        return total_steps;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    //TODO: remaining methods for TimeOutside()
}
