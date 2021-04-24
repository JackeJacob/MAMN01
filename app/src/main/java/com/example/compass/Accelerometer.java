package com.example.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Accelerometer extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    Sensor accelerometer;
    float x, y, z;
    TextView acceleration;
    MediaPlayer highValue, lowValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Accelerometer.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        acceleration = findViewById(R.id.accelerate);

        highValue = MediaPlayer.create(Accelerometer.this, R.raw.highvalue);
        lowValue = MediaPlayer.create(Accelerometer.this, R.raw.lowvalue);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];
        acceleration.setText("X: " + x +"\nY: " + y + "\nZ: " + z);
        playSound();
    }

    private void playSound() {
        if(y > 3 && x > 3 && z > 3){
            highValue.start();
        }
       else if(y< 0 && x < 0 && z < 0){
            lowValue.start();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
