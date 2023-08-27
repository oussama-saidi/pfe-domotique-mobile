package com.pfedomotique.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class LandingActivity extends AppCompatActivity {

    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        // initiate progress bar and start button
     timer = new Timer();
     timer.schedule(new TimerTask() {
         @Override
         public void run() {
             Intent intent = new Intent(LandingActivity.this,MainActivity.class);
             startActivity(intent);
             finish();
         }
     },2000);
    }
}