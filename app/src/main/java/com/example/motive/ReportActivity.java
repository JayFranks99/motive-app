package com.example.motive;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static com.example.motive.RegisterActivity.TAG1;

public class ReportActivity extends AppCompatActivity {

    EditText email, username, issue;
    Button submit;
    ImageView back;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    DocumentReference documentReference;

    private static final String TAG = "Report Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Log.d(TAG, "onCreate: started.");

        email = findViewById(R.id.emailEditText);
        username = findViewById(R.id.usernameET);
        issue = findViewById(R.id.issueDescriptionEt);
        submit = findViewById(R.id.submitButton);
        back = findViewById(R.id.backImg);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               final String userEmail = email.getText().toString().trim();
               final String userUsername = username.getText().toString().trim();
               final String userIssue = issue.getText().toString();

               userID = fAuth.getCurrentUser().getUid();
               documentReference = fStore.collection("Reports").document(userID);
               //user HashMap called user
               HashMap<String, Object> user = new HashMap<>();
               user.put("email", userEmail);
               user.put("Reported user username", userUsername);
               user.put("Issue", userIssue);


               documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Log.d(TAG1, "Report sent to database " + userID);

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.d(TAG1, "onFailure: " + e.toString());
                   }
               });

               Toast.makeText(ReportActivity.this, "Thanks for letting us know, your report has been sent and will be reviewed", Toast.LENGTH_LONG).show();
           }
       });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent;
                myintent = new Intent(getBaseContext(),
                        SettingsActivity.class);
                        startActivity(myintent);
            }
        });
            }

}
