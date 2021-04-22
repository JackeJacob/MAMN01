package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCompass(View view){
        startActivity(new Intent(MainScreen.this, Compass.class));
    }
    public void openAccValue(View view){
        startActivity(new Intent(MainScreen.this, Accelerometer.class));
    }
}
