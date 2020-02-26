package com.example.motive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class LogInActivity extends AppCompatActivity {

    Button loginButton;

    private static final String TAG = "Login Activity";


    public void LogInButtonClick(View view) {

        Log.i("Info", "Log in Button Clicked");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: started.");

        loginButton = findViewById(R.id.logInButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(),
                       MotiveHomeActivity.class);
                startActivity(myIntent);
            }
        });


    }

    public void logout(View view) {
    }
}


