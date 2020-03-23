package com.example.fileread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private long startTime;
    private ArrayList<Integer> powerList=new ArrayList<>();
    private ArrayList<Long> timeList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView time=findViewById(R.id.time);
        final Button start=findViewById(R.id.start);
        final Button stop=findViewById(R.id.stop);
        stop.setClickable(false);


        final Handler handler=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                BatteryInfo newInfo=BatteryUtil.update();
                int power=(int)(newInfo.voltage*newInfo.current);

                if(powerList.size()==0||power!=powerList.get(powerList.size()-1)) {
                    Log.d(TAG, "run: power=" + power);
                    long nowTime=SystemClock.elapsedRealtime();
                    if(powerList.size()==0) {
                        startTime = nowTime;
                        time.setText(Long.toString(startTime));
                    }
                    timeList.add(nowTime-startTime);
                    powerList.add(power);
                }
                handler.postDelayed(this,50);
            }
        };
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(runnable);
                Log.d(TAG, "onClick: start");
                start.setClickable(false);
                stop.setClickable(true);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                Log.d(TAG, "onClick: powerList="+powerList);
                Log.d(TAG, "onClick: timeList="+timeList);
                startTime=0;
                powerList.clear();
                timeList.clear();
                start.setClickable(true);
                stop.setClickable(false);
            }
        });
    }

}
