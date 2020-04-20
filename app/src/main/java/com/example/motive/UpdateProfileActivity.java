package com.example.motive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText newUsername, newBio;
    AutoCompleteTextView newDegree, newHalls, newMainMotive;
    MultiAutoCompleteTextView newMotives;
    Button saveButton;
    TextView editProfilePicture;
    ImageView backImage, profileImage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    public static final String TAG1 = "TAG";
    HashMap<String, Object> user;
    StorageReference storageReference;
    ConstraintLayout updateProfileBackground;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        newUsername = findViewById(R.id.usernameEditText);
        newHalls = findViewById(R.id.hallsEditText);
        newBio = findViewById(R.id.userBioEditText);
        newDegree = findViewById(R.id.degreePlainText);
        newMotives = findViewById(R.id.motivesEditText);
        newMainMotive = findViewById(R.id.mainMotiveEditText2);
        saveButton = findViewById(R.id.buttonSave);
        backImage = findViewById(R.id.backButtonEditProfile);
        profileImage = findViewById(R.id.displayImageView);
        editProfilePicture = findViewById(R.id.editProfilePictureTextView);
        updateProfileBackground = findViewById(R.id.updateProfileBackground);
        updateProfileBackground.setOnClickListener(this);


        //Underline Text View
        editProfilePicture.setPaintFlags(editProfilePicture.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);

        //auto-complete arrays

        String[] halls = getResources().getStringArray(R.array.halls);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, halls);

       newHalls.setAdapter(adapter);
        newHalls.setDropDownBackgroundResource(R.color.autocomplete_background_color);

        String[] motive = getResources().getStringArray(R.array.motives);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, motive);

        newMainMotive.setAdapter(adapter2);
        newMainMotive.setDropDownBackgroundResource(R.color.autocomplete_background_color);

        String[] motives = getResources().getStringArray(R.array.motives);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, motives);

        newMotives.setAdapter(adapter3);
        newMotives.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        newMotives.setDropDownBackgroundResource(R.color.autocomplete_background_color);

        String[] degree = getResources().getStringArray(R.array.degrees);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, degree);

        newDegree.setAdapter(adapter4);
        newDegree.setDropDownBackgroundResource(R.color.autocomplete_background_color);

        //

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        Bundle dbfields = this.getIntent().getExtras();
        if (dbfields != null) {
            user = (HashMap<String, Object>) dbfields.getSerializable("user");
        }


//Profile image reference for each user registered, seperate directory
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });



        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                newUsername.setText(documentSnapshot.getString("username"));
                newHalls.setText(documentSnapshot.getString("halls"));
                newBio.setText(documentSnapshot.getString("user bio"));
                newDegree.setText(documentSnapshot.getString("degree"));
                newMainMotive.setText(documentSnapshot.getString("main motive"));
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
                String mainM = newMainMotive.getText().toString();

                
                if (TextUtils.isEmpty(name)) {
                    newUsername.setError("Username is required.");
                    return;
                }

                if (TextUtils.isEmpty(halls)) {
                    newHalls.setError("Halls name is required.");
                    return;
                }


                if (TextUtils.isEmpty(name)) {
                  newUsername.setError("Username is required.");
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    newUsername.setError("A user bio must be entered");
                    return;
                }

                if (bio.length() < 20) {
                    newBio.setError("User bio must be at least 20 characters long");
                    return;
                }

                if (TextUtils.isEmpty(mainM)) {
                    newMainMotive.setError("A main motive is required");
                    return;
                }


                if (mainM.length() > 30) {
                    newMainMotive.setError("Only your main motive required");
                    return;
                }

                if (TextUtils.isEmpty(motives)) {
                    newMotives.setError("Other motives are required");
                    return;
                }

                if (motives.length() > 150) {
                    newMotives.setError("Less than 150 characters please");
                    return;
                }


                if (TextUtils.isEmpty(degree)) {
                    newDegree.setError("Degree is required");
                    return;
                }

                if (degree.length() > 40) {
                    newDegree.setError("Less than 40 characters please");
                    return;
                }



                userId = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userId);
                //user HashMap called user
                HashMap<String, Object> user = new HashMap<>();
                user.put("halls", halls);
                user.put("user bio", bio);
                user.put("other motives", motives);
                user.put("degree", degree);
                user.put("username", name);
                user.put("main motive", mainM);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(UpdateProfileActivity.this, "Changes saved.", Toast.LENGTH_SHORT).show();

                        Log.d(TAG1, "onSuccess: user profile is updated " + userId);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG1, "onFailure: " + e.toString());
                    }
                });

                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);

                Bundle extras = new Bundle();
                extras.putSerializable("user", user);
                i.putExtras(extras);
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editProfilePicture.setOnClickListener(new View.OnClickListener() {
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
                uploadImagetoFirebase(imageUri);
            }
        }
    }

    private void uploadImagetoFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Hide keyboard when clicking on the background view of the activity
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.updateProfileBackground) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
    }
}



