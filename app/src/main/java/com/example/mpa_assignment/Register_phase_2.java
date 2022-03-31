package com.example.mpa_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

public class Register_phase_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phase2);

        Intent intent = new Intent(this, MainScreen.class);

        // Handler used to create a loading screen effect
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Call next activity
                startActivity(intent);
            }
        }, 3000);   //5 seconds
    }
}