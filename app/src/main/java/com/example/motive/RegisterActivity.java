package com.example.motive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    EditText mUsername;
    EditText mEmail;
    EditText mPassword;
    EditText mHalls;
    EditText mStreetName;
    EditText mBlock;
    EditText mUserBio;
    EditText mMainMotive;
    EditText mOtherMotives;
    EditText mDegree;
    Button mRegisterButton;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    //On Create Method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.usernameEditText);
        mEmail = findViewById(R.id.emailEditText);
        mPassword = findViewById(R.id.passwordEditText);
        mHalls = findViewById(R.id.hallsEditText);
        mStreetName = findViewById(R.id.streetEditText);
        mBlock = findViewById(R.id.blockEditText);
        mUserBio = findViewById(R.id.descriptionEditText);
        mMainMotive = findViewById(R.id.mainMotiveEditText);
        mOtherMotives = findViewById(R.id.motivesEditText);
        mDegree = findViewById(R.id.degreeEditText);
        mRegisterButton = findViewById(R.id.registerButton2);

        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),MotiveHomeActivity.class));
            finish();
        }


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String halls = mHalls.getText().toString().trim();
                String streetName = mStreetName.getText().toString().trim();
                String block = mBlock.getText().toString().trim();
                String userBio = mUserBio.getText().toString().trim();
                String mainMotive = mMainMotive.getText().toString().trim();
                String otherMotives = mOtherMotives.getText().toString().trim();
                String degree = mDegree.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("Username is required.");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required.");
                    return;
                }

                if(TextUtils.isEmpty(halls)) {
                    mHalls.setError("Halls name is required.");
                    return;
                }

                if(TextUtils.isEmpty(streetName)) {
                    mStreetName.setError("Street name is required.");
                    return;
                }

                if(TextUtils.isEmpty(block)) {
                    mBlock.setError("Block name or house number is required.");
                    return;
                }
                if(TextUtils.isEmpty(userBio)) {
                    mUserBio.setError("A user bio must be entered");
                    return;
                }

                if(TextUtils.isEmpty(mainMotive)) {
                    mMainMotive.setError("A main motive is required");
                    return;
                }

                if(TextUtils.isEmpty(otherMotives)) {
                    mOtherMotives.setError("Other motives are required");
                    return;
                }

                if(TextUtils.isEmpty(degree)) {
                    mDegree.setError("Degree is required");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MotiveHomeActivity.class));

                        }else {

                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

    }
}
