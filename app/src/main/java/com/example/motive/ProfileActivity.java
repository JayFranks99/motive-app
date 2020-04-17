package com.example.motive;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.squareup.picasso.Picasso;
import java.util.Objects;

public class  ProfileActivity extends AppCompatActivity {

    ImageView backImageView;
    Button resendCodeButton;
    String userId;
    TextView username, halls, bio, motives, degree;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageView profileImage;
    Button changeProfileImage;

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
        motives = findViewById(R.id.profileMotives);
        degree = findViewById(R.id.profileDegree);

        profileImage = findViewById(R.id.displayImageView);
        changeProfileImage = findViewById(R.id.changeProfile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                username.setText(documentSnapshot.getString("username"));
                halls.setText(documentSnapshot.getString("halls"));
                bio.setText(documentSnapshot.getString("user bio"));
                degree.setText(documentSnapshot.getString("degree"));
                motives.setText(documentSnapshot.getString("other motives"));
                //Picasso class
            }
        });

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
                            Log.d(TAG,"Email not send " + e.getMessage());
                        }
                    });
                }
            });

        }

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }
}
