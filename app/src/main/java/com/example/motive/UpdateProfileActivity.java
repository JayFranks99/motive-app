package com.example.motive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.HashMap;
import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText newUsername, newHalls, newBio, newDegree, newMotives;
    private Button saveButton;
    private  FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String userId;
    public static final String TAG1 = "TAG";
    HashMap<String, Object> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        newUsername = findViewById(R.id.usernameEditText);
        newHalls = findViewById(R.id.hallsEditText);
        newBio = findViewById(R.id.userBioEditText);
        newDegree = findViewById(R.id.degreePlainText);
        newMotives = findViewById(R.id.motivesEditText);
        saveButton = findViewById(R.id.buttonSave);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();


        Bundle dbfields = this.getIntent().getExtras();
        if (dbfields != null) {
            user = (HashMap<String,Object>)dbfields.getSerializable("user");
        }

        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                newUsername.setText(documentSnapshot.getString("username"));
                newHalls.setText(documentSnapshot.getString("halls"));
                newBio.setText(documentSnapshot.getString("user bio"));
                newDegree.setText(documentSnapshot.getString("degree"));
                newMotives.setText(documentSnapshot.getString("other motives"));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = newUsername.getText().toString();
                String halls = newHalls.getText().toString();
                String bio = newBio.getText().toString();
                String degree = newDegree.getText().toString();
                String motives = newMotives.getText().toString();

                userId = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userId);
                //user HashMap called user
                HashMap<String, Object> user = new HashMap<>();
                user.put("halls", halls);
                user.put("user bio", bio);
                user.put("other motives", motives);
                user.put("degree", degree);
                user.put("username",name);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG1, "onSuccess: user profile is created for " + userId);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG1, "onFailure: " + e.toString());
                    }
                });

                Intent i = new Intent(getApplicationContext(), UpdateProfileActivity.class);

                Bundle extras = new Bundle();
                extras.putSerializable("user", user);
                i.putExtras(extras);
            }
        });


    }
}

