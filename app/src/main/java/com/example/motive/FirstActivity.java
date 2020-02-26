package com.example.motive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {

    Button logInButton;
    Button registerButton;

    private static final String TAG = "LogInActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Log.d(TAG, "onCreate: started.");

        logInButton = findViewById(R.id.logInButton);
        registerButton = findViewById(R.id.registerButton2);


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent;
                myIntent = new Intent(getBaseContext(),
                        LogInActivity.class);
                startActivity(myIntent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                Intent myIntent2;
                myIntent2 = new Intent(getBaseContext(),
                        RegisterActivity.class);
                startActivity(myIntent2);
            }
        });


    }
}



