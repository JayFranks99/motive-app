package com.example.motive;

import android.content.Intent;
import android.nfc.Tag;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG1 = "TAG";
    public static final String TAG2 = "TAG";
    EditText mUsername;
    EditText mEmail;
    EditText mPassword;
    EditText mHalls;
    EditText mStreetName;
    EditText mBlock;
    EditText mPostcode;
    EditText mUserBio;
    EditText mMainMotive;
    EditText mOtherMotives;
    EditText mDegree;
    Button mRegisterButton;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //On Create Method

    private static final String TAG = "Register Activity";

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
        mPostcode = findViewById(R.id.postcodeEditText);
        mUserBio = findViewById(R.id.descriptionEditText);
        mMainMotive = findViewById(R.id.mainMotiveEditText);
        mOtherMotives = findViewById(R.id.motivesEditText);
        mDegree = findViewById(R.id.degreeEditText);
        mRegisterButton = findViewById(R.id.registerButton2);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),MotiveHomeActivity.class));
            finish();
        }


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = mUsername.getText().toString();
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String halls = mHalls.getText().toString();
                final String streetName = mStreetName.getText().toString();
                final String block = mBlock.getText().toString();
                final String postcode = mPostcode.getText().toString();

                final String userBio = mUserBio.getText().toString();
                final String mainMotive = mMainMotive.getText().toString();
                final String otherMotives = mOtherMotives.getText().toString();
                final String degree = mDegree.getText().toString();

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

                if (password.length() < 6) {
                    mPassword.setError("Password must be at least 6 characters long");
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

                if(TextUtils.isEmpty(postcode)) {
                    mPostcode.setError("Postcode is required.");
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

                            //send verification link
                            //display pop up making them verify email

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                Toast.makeText(RegisterActivity.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"Email not send " + e.getMessage());
                                }
                            });


                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();

                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("username", username);
                            user.put("email", email);
                            user.put("halls", halls);
                            user.put("street name", streetName);
                            user.put("block", block);
                            user.put("postcode", postcode);
                            user.put("user bio", userBio);
                            user.put("main motive", mainMotive);
                            user.put("other motives", otherMotives);
                            user.put("degree", degree);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG1, "onSuccess: user profile is created for "+ userID);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                Log.d(TAG1,"onFailure: " + e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),MotiveHomeActivity.class));

                        }else {

                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });



            }
        });

    }
}
