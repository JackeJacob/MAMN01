package com.example.compass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.hardware.SensorEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.SensorEventListener;
import android.os.Bundle;

public class Compass extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private ImageView compassimage;
    private float DegreeStart = 0f;
    TextView DegreeValues;
    Sensor accSensor, magnetSensor, vector;
    float[] gravity;
    float[] geomagnetic;
    private int azimuth = 0;
    private float[] accelerometer = new float[3];
    private float[] magnetometer = new float[3];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        compassimage = (ImageView) findViewById(R.id.compass);
        DegreeValues = (TextView) findViewById(R.id.values);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        vector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, vector, SensorManager.SENSOR_DELAY_GAME);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);

        DegreeValues.setText("Heading towards " + Float.toString(degree) + "°");

        RotateAnimation rotation = new RotateAnimation(DegreeStart, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotation.setFillAfter(true);
        rotation.setDuration(200);

        compassimage.startAnimation(rotation);
        DegreeStart = -azimuth;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
