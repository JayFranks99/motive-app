package com.example.motive;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class EventOneActivity extends AppCompatActivity {

    ImageView backButton;


    @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event1);

        backButton = findViewById(R.id.backImageView100);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(),
                        MotiveHomeActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
