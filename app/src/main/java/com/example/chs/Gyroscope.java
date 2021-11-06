package com.example.chs;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;

import java.util.List;

public class Gyroscope {
    public interface Listener{
        void onRotation(float tx, float ty, float ts);
    }
    private Listener listener;
    public void setListener(Listener l){
        listener = l;
    }
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor sensor;

    Gyroscope(Context context){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(listener != null){
                    listener.onRotation(event.values[0],event.values[1],event.values[2]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }
    public void register(){
        sensorManager.registerListener(sensorEventListener,sensor,sensorManager.SENSOR_DELAY_NORMAL);

    }
    public void unregister(){
        sensorManager.unregisterListener(sensorEventListener);
    }
}
