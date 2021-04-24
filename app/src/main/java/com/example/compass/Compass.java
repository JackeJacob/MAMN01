package com.example.compass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    private TextView DegreeValues;
    private Sensor accSensor, magnetSensor, vector;
    boolean havesensor= false, havesensor2=false;
    private int azimuth = 0;
    float[] lastAccelerometer = new float[3];
    float[] lastMagnetometer = new float[3];
    private boolean lastAccelerometerSet;
    private boolean lastMagnetometerSet;
    float rMat[] = new float[9];
    float orientation[] = new float[9];
    private float LOWPASS_FACTOR = 0.25f;
    ConstraintLayout backgroundColor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        compassimage =  findViewById(R.id.compass);
        DegreeValues =  findViewById(R.id.values);
        backgroundColor = findViewById(R.id.background);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

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

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            azimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            System.arraycopy(lowPass(event.values, lastMagnetometer), 0, lastMagnetometer,0, event.values.length);
            lastMagnetometerSet = true;
        }

        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(lowPass(event.values, lastAccelerometer),0,lastAccelerometer,0,event.values.length);
            lastAccelerometerSet = true;
        }

        if(lastAccelerometerSet && lastMagnetometerSet){
            SensorManager.getRotationMatrix(rMat,null,lastAccelerometer,lastMagnetometer);
            SensorManager.getOrientation(rMat,orientation);
            azimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }
       azimuth = Math.round(azimuth);

        DegreeValues.setText("Heading towards " + Float.toString(azimuth) + "°");

        RotateAnimation rotation = new RotateAnimation(DegreeStart, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotation.setFillAfter(true);
        rotation.setDuration(200);

        compassimage.startAnimation(rotation);
        DegreeStart = -azimuth;

        if(azimuth >= 345 || azimuth <= 15){  //north
            backgroundColor.setBackgroundColor(Color.CYAN);
        }
        else if (azimuth >=75 && azimuth <=105){ //west
            backgroundColor.setBackgroundColor(Color.MAGENTA);
        }
        else if (azimuth >=165 && azimuth <=195){ //south
            backgroundColor.setBackgroundColor(Color.YELLOW);
        }
        else if (azimuth >=255 && azimuth <=285){ //east
            backgroundColor.setBackgroundColor(Color.GREEN);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected float[] lowPass (float[] input, float[] output) {
        if (output == null)
            return input;
        for (int i = 0; i < input.length; i++) {
            output[i] += LOWPASS_FACTOR * (input[i] - output[i]);
        }
        return output;
    }


}
