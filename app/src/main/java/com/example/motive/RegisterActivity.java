package com.example.motive;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG1 = "TAG";
    public static final String TAG2 = "TAG";
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    EditText mUsername;
    EditText mEmail;
    EditText mPassword;
    EditText cPassword;
    AutoCompleteTextView mHalls;
    EditText mPostcode;
    EditText mUserBio;
    AutoCompleteTextView mMainMotive;
    MultiAutoCompleteTextView mOtherMotives;
    AutoCompleteTextView mDegree;
    Button mRegisterButton;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    ImageView selectedImage;
    Button cameraBtn, galleryBtn;
    String currentPhotoPath;
    StorageReference storageReference;
    ConstraintLayout registerBackground;


    private static final String TAG = "Register Activity";

    //On Create Method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.usernameEditText);
        mEmail = findViewById(R.id.emailEditText);
        mPassword = findViewById(R.id.passwordEditText);
        mPostcode = findViewById(R.id.postcodeEditText);
        mUserBio = findViewById(R.id.descriptionEditText);
        mMainMotive = findViewById(R.id.mainMotiveAutoCompleteTextView);
        mOtherMotives = findViewById(R.id.motivesMultiAutoCompleteTextView);
        mDegree = findViewById(R.id.degreeAutoCompleteTextView);
        mRegisterButton = findViewById(R.id.registerButton2);
        progressBar = findViewById(R.id.progressBar);
        cPassword = findViewById(R.id.confirmPasswordEditText);
        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        registerBackground = findViewById(R.id.registerBackground);
        registerBackground.setOnClickListener(this);
        mHalls = findViewById(R.id.hallsAutoCompleteTextView);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MotiveHomeActivity.class));
            finish();
        }

        //auto-complete arrays

        String[] halls = getResources().getStringArray(R.array.halls);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, halls);

        mHalls.setAdapter(adapter);
        mHalls.setDropDownBackgroundResource(R.color.autocomplete_background_color);

        String[] motive = getResources().getStringArray(R.array.motives);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, motive);

        mMainMotive.setAdapter(adapter2);
        mMainMotive.setDropDownBackgroundResource(R.color.autocomplete_background_color);

        String[] motives = getResources().getStringArray(R.array.motives);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, motives);

        mOtherMotives.setAdapter(adapter3);
        mOtherMotives.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mOtherMotives.setDropDownBackgroundResource(R.color.autocomplete_background_color);

        String[] degree = getResources().getStringArray(R.array.degrees);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, degree);

        mDegree.setAdapter(adapter4);
        mDegree.setDropDownBackgroundResource(R.color.autocomplete_background_color);

        //Register button click

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String confirmPassword = cPassword.getText().toString();
                final String halls = mHalls.getText().toString();
                final String postcode = mPostcode.getText().toString();
                final String username = mUsername.getText().toString();
                final String userBio = mUserBio.getText().toString();
                final String mainMotive = mMainMotive.getText().toString();
                final String otherMotives = mOtherMotives.getText().toString();
                final String degree = mDegree.getText().toString();
                // final String imageUrl = storageReference.getDownloadUrl().toString();



                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required.");
                    return;
                }

                if (email.contains("@leeds.ac.uk")) {
                    Toast.makeText(RegisterActivity.this, "Uniersity of Leeds email", Toast.LENGTH_SHORT);
                } else {
                    mEmail.setError("1st Year Univerisity of Leeds Students Only");
                    return;
                }
                if (email.contains("20")) {
                    Toast.makeText(RegisterActivity.this, "Uniersity of Leeds email", Toast.LENGTH_SHORT);
                }else {
                    mEmail.setError(" Univerisity of Leeds Freshers Only");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be at least 6 characters long");
                    return;
                }

                if (confirmPassword.equals(password)) {
                    Toast.makeText(RegisterActivity.this, "Password matches", Toast.LENGTH_SHORT);
                }else {
                    cPassword.setError("Passwords do not match");
                    return;
                }


                if (TextUtils.isEmpty(halls)) {
                    mHalls.setError("Halls name is required.");
                    return;
                }

                if (TextUtils.isEmpty(postcode)) {
                    mPostcode.setError("Postcode is required.");
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("Username is required.");
                    return;
                }

                if (TextUtils.isEmpty(userBio)) {
                    mUserBio.setError("A user bio must be entered");
                    return;
                }

                if (userBio.length() < 20) {
                    mUserBio.setError("User bio must be at least 20 characters long");
                    return;
                }

                if (TextUtils.isEmpty(mainMotive)) {
                    mMainMotive.setError("A main motive is required");
                    return;
                }


                if (mainMotive.length() > 30) {
                    mMainMotive.setError("Only your main motive required");
                    return;
                }

                if (TextUtils.isEmpty(otherMotives)) {
                    mOtherMotives.setError("Other motives are required");
                    return;
                }

                if (otherMotives.length() > 150) {
                    mOtherMotives.setError("Less than 150 characters please");
                    return;
                }


                if (TextUtils.isEmpty(degree)) {
                    mDegree.setError("Degree is required");
                    return;
                }

                if (degree.length() > 40) {
                    mDegree.setError("Less than 40 characters please");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

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
                                    Log.d(TAG, "Email not send " + e.getMessage());
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();

                            //Enter register data to database
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            //user HashMap called user
                            HashMap<String, Object> user = new HashMap<>();

                            user.put("email", email);
                            user.put("halls", halls);
                            user.put("postcode", postcode);
                            user.put("user bio", userBio);
                            user.put("main motive", mainMotive);
                            user.put("other motives", otherMotives);
                            user.put("degree", degree);
                            user.put("username", username);
                            user.put("address",geoLocate().toString());
                            user.put("lat",geoLocate().getLatitude());
                            user.put("lng",geoLocate().getLongitude());

                           documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG1, "onSuccess: user profile is created for " + userID);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG1, "onFailure: " + e.toString());
                                }
                            });

                           Intent i = new Intent(getApplicationContext(), MotiveHomeActivity.class);

                            Bundle extras = new Bundle();
                            extras.putSerializable("user", user);
                            i.putExtras(extras);

                            startActivity(i);
                            //execute out method for geolocating

                        } else {

                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }

            private Address geoLocate() {
                   Log.d(TAG, "geoLocate:geolocating");

                   String postcode = mPostcode.getText().toString();

                   Geocoder geocoder = new Geocoder(RegisterActivity.this);
                   List <Address> list = new ArrayList<>();
                   try {
                       list = geocoder.getFromLocationName(postcode, 1);

                   }catch (IOException e) {
                       Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
                   }

                   if (list.size() > 0) {
                       Address address = list.get(0);
                       Log.d(TAG, "geoLocate: found a location:  " + address.toString());
                       return address;
                       // Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
                      // moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM);
                   }
                   return null;
               }
            });


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);

            }
        });

    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permisision is required to use the camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //URL of image
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                //selectedImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                uploadImageToFirebase(f.getName(),contentUri);
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentURI = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp +"."+getFileExt(contentURI);
                Log.d("tag", "onActivityResult: Gallery Image uri: " + imageFileName);
               // selectedImage.setImageURI(contentURI);
                uploadImageToFirebase(imageFileName, contentURI);
            }
        }
    }

    //upload on if register is success
    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("users/"+fAuth.getCurrentUser()+"/profile.jpg");
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(selectedImage);
                    }
                });
                Toast.makeText(RegisterActivity.this, "Image is uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // images/image.jpg

    //img extension
    private String getFileExt(Uri contentURI) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentURI));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registerBackground) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
    }
}
