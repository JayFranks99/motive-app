package com.example.motive;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class  ProfileActivity extends AppCompatActivity {

    ImageView backImageView;
    Button resendCodeButton;
    String userId, userName;
    TextView username, halls, bio, motives, degree, mainUserMotive, profileUsernameNew, userBio;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageView profileImage;
    Button editProfileFields;
    StorageReference storageReference;
    ConstraintLayout jayLayout;

    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");

        backImageView = findViewById(R.id.backImageView);
        fAuth = FirebaseAuth.getInstance();
        resendCodeButton = findViewById(R.id.resendCodeButton);

        username = findViewById(R.id.profileUsername);
        halls = findViewById(R.id.profileHalls);
        bio = findViewById(R.id.profileUserBio);
        mainUserMotive = findViewById(R.id.mainMotive);
        motives = findViewById(R.id.profileMotives);
        degree = findViewById(R.id.profileDegree);
        profileImage = findViewById(R.id.displayImageView);
        editProfileFields = findViewById(R.id.editprofileButton);
        profileUsernameNew = findViewById(R.id.profileTextView);
        userBio = findViewById(R.id.aboutUser);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        jayLayout = findViewById(R.id.jay_constraint);
        storageReference = FirebaseStorage.getInstance().getReference();

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //Getting the correct user profile and updating the UI from my profile to user profile
        //From google map marker username
        //if the name on the marker matches a username in the database, then when view profile is clicked update the ui so it displays that users information

        Bundle userBundleData = this.getIntent().getExtras();
        if (userBundleData != null) {
            userName = userBundleData.getSerializable("userName").toString();

            CollectionReference collectionReferenceUsers = fStore.collection("users");

            collectionReferenceUsers.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    for (int i = 0; i < documents.size(); i++) {
                        String name = documents.get(i).getString("username");

                        // if 'name' (username field in the database) = userName (marker title), then update UI for that users data
                        if (name.equalsIgnoreCase(userName)) {
                            userId = documents.get(i).getId();
                            updateUI();
                            break;
                        }

                    }
                }
            });

            //Hide edit profile button
            editProfileFields.setVisibility(View.GONE);

        } else {
            userId = fAuth.getCurrentUser().getUid();

            userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
            final FirebaseUser user = fAuth.getCurrentUser();

            if (!user.isEmailVerified()) {
                resendCodeButton.setVisibility(View.VISIBLE);
                resendCodeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Email not send " + e.getMessage());
                            }
                        });
                    }
                });

            }
        }


        editProfileFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit;
                edit = new Intent(getBaseContext(),
                        UpdateProfileActivity.class);
                startActivity(edit);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();

    }


    public void updateUI() {
        if (userId != null) {
            /* CODE TO CHANGE MARGIN SIZE FOR UI TO LOOK BETTER

            ConstraintSet constraintSet = new ConstraintSet();

            constraintSet.clone(jayLayout);

            constraintSet.connect(R.id.aboutUser, ConstraintSet.TOP, R.id.blueBackground, ConstraintSet.BOTTOM, 5);

            constraintSet.applyTo(jayLayout);

             */

            DocumentReference documentReference = fStore.collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                    profileUsernameNew.setText(documentSnapshot.getString("username"));
                    username.setText(documentSnapshot.getString("username"));
                    halls.setText(documentSnapshot.getString("halls"));
                    bio.setText(documentSnapshot.getString("user bio"));
                    degree.setText(documentSnapshot.getString("degree"));
                    mainUserMotive.setText(documentSnapshot.getString("main motive"));
                    motives.setText(documentSnapshot.getString("other motives"));

                    StorageReference profileRef = storageReference.child("users/" + userId + "/profile.jpg");
                    profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(profileImage);
                        }
                    });
                }
            });

        }

    }
}
