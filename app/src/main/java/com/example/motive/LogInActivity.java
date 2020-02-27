package com.example.motive;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LogInActivity extends AppCompatActivity {

    EditText mEmail;
    EditText mPassword;
    Button mLoginButton;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    TextView forgotTextLink;

        @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                mEmail = findViewById(R.id.emailEditText);
                mPassword = findViewById(R.id.passwordEditText);
                mLoginButton = findViewById(R.id.logInButton2);
                forgotTextLink = findViewById(R.id.forgotPassword);


                fAuth = FirebaseAuth.getInstance();
                progressBar = findViewById(R.id.progressBar);


                mLoginButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String email = mEmail.getText().toString().trim();
                        String password = mPassword.getText().toString().trim();

                        if (TextUtils.isEmpty(email)) {
                            mEmail.setError("Email is required.");
                            return;
                        }

                        if(TextUtils.isEmpty(password)) {
                            mPassword.setError("Password is required.");
                            return;
                        }

                        progressBar.setVisibility(View.VISIBLE);

                        //authenticate the user here

                        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(LogInActivity.this, "Log in successful.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),MotiveHomeActivity.class));
                                }else {
                                    Toast.makeText(LogInActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);

                                }
                            }
                        });


                    }
                });

                    forgotTextLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            final EditText resetMail = new EditText(view.getContext());

                            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                            passwordResetDialog.setTitle("Reset Password?");
                            passwordResetDialog.setMessage("Enter your email to receive password rest link.");
                            passwordResetDialog.setView(resetMail);

                            passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //extract email and send reset link

                                    String mail = resetMail.getText().toString();
                                    fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(LogInActivity.this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LogInActivity.this, "Error! Rest link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                            passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //close the dialog
                                }
                            });

                            passwordResetDialog.create().show();

                        }
                    });

        }


        public void logout(View view) {
        }




}


