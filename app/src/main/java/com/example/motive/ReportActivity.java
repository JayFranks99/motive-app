package com.example.motive;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {



    private static final String TAG = "Report Activity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Log.d(TAG, "onCreate: started.");



    }
}
