package com.example.motive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {

    Button logInButtonFirst;
    Button registerButtonFirst;

    private static final String TAG = "LogInActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Log.d(TAG, "onCreate: started.");

        logInButtonFirst = findViewById(R.id.logInButton2);
        registerButtonFirst = findViewById(R.id.registerButton2);


        logInButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent;
                myIntent = new Intent(getBaseContext(),
                        LogInActivity.class);
                startActivity(myIntent);
            }
        });

        registerButtonFirst.setOnClickListener(new View.OnClickListener() {
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



