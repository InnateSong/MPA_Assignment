package com.example.mpa_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checks if user is already logged in!
        checkCurrentUser();

        // Handler created to sleep for 3 seconds (loading screen will be displayed)
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        }, 3000);   //3 seconds
    }

    // CHECKS FOR ACTIVE USER
    public void checkCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Toast.makeText(getApplicationContext(), "User already signed in! Logging off now...", Toast.LENGTH_SHORT).show();

            // Sign user out for the time being (URGENT!! CHANGE THIS LATER)
            FirebaseAuth.getInstance().signOut();
        }
    }
}