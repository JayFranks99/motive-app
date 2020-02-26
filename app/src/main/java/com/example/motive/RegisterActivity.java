package com.example.motive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {


    Button RegisterButton2;
    private static final String TAG = "Registered" ;

    public void RegisterButtonClick (View view) {

        //views

        EditText usernameEditText = (EditText)findViewById(R.id.usernameEditText);

        EditText emailEditText = (EditText)findViewById(R.id.emailEditText);

        EditText passwordEditText = (EditText)findViewById(R.id.passswordEditText);

        EditText hallsEditText = (EditText)findViewById(R.id.hallsEditText);

        EditText blockEditText = (EditText)findViewById(R.id.blockEditText);

        EditText streetEditText = (EditText)findViewById(R.id.streetEditText);

        EditText postcodeEditText = (EditText)findViewById(R.id.postcodeEditText);

        EditText descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);

        EditText mainMotiveEditText = (EditText)findViewById(R.id.mainMotiveEditText);

        EditText motivesEditText = (EditText)findViewById(R.id.motivesEditText);

        EditText degreeEditText = (EditText)findViewById(R.id.degreeEditText);


        //function

        Log.i( "Info", "Register button pressed");

        Log.i("Username", usernameEditText.getText().toString());

        Log.i("Email", emailEditText.getText().toString());

        Log.i("Password", passwordEditText.getText().toString());

        Log.i("Halls", hallsEditText.getText().toString());

        Log.i("Block", blockEditText.getText().toString());

        Log.i("Street", streetEditText.getText().toString());

        Log.i("Postcode", postcodeEditText.getText().toString());

        Log.i("Description", descriptionEditText.getText().toString());

        Log.i("Main Motive", mainMotiveEditText.getText().toString());

        Log.i("Other Motives", motivesEditText.getText().toString());

        Log.i("Degree", degreeEditText.getText().toString());

        Toast.makeText(this, "Register success " + usernameEditText.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    //On Create Method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started.");

        RegisterButton2 = findViewById(R.id.registerButton2);

        RegisterButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(),
                        LogInActivity.class);
                startActivity(myIntent);
            }
        });


    }
}
