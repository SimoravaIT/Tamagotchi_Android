package com.example.androidapp.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.example.androidapp.ui.home.HomeFragment;


public class TemperatureListener implements SensorEventListener {
    private Context context;
    public TemperatureListener(Context c){
        this.context=c;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        HomeFragment.temperatureChanged(sensorEvent.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
