package com.example.motive;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.firestore.GeoPoint;

import android.location.Address;

import static com.example.motive.RegisterActivity.TAG1;


public class MotiveHomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView myProfileIcon;
    ImageView settingsIcon;
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    List<Fragment> FragmentList;

    HashMap<String, Object> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivehome);
        FindViewsById();


        Bundle dbfields = this.getIntent().getExtras();
        if (dbfields != null) {
            user = (HashMap<String,Object>)dbfields.getSerializable("user");
        }

        myProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(),
                        ProfileActivity.class);
                startActivity(myIntent);
            }
        });

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(),
                        SettingsActivity.class);
                startActivity(myIntent);
            }
        });



        FragmentList = new ArrayList<Fragment>();

        SupportMapFragment map = new com.google.android.gms.maps.SupportMapFragment();
        map.getMapAsync(this);

        FragmentList.add(map);
        FragmentList.add(new EventsFragment());
        FragmentList.add(new MentalFragment());
        FragmentList.add(new MessagesFragment());

        tabLayout.setupWithViewPager(viewPager);
        pagerAdapter = new com.example.motive.PagerAdapter(getSupportFragmentManager(), FragmentList);
        viewPager.setAdapter(pagerAdapter);



    }

    private void FindViewsById() {
        myProfileIcon = findViewById(R.id.profileButton);
        settingsIcon = findViewById(R.id.settingsImageView);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Rotation of device
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //Google Maps Method

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Markers and camera zoom
        final Context context = this;

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                 final String[] items = {"View Profile", "Message", "Close"};

                new AlertDialog.Builder(context)
                        .setTitle(marker.getTitle())
                        .setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, items[i], Toast.LENGTH_SHORT).show();
                                switch (i)
                                {
                                    case 0:
                                        //View profile
                                        break;
                                    case 1:
                                        viewPager.setCurrentItem(3);
                                        //Message
                                        break;
                                }
                            }
                        }).show();

                return false;
            }
        });

        LatLng jayLatLng = new LatLng ((double) user.get("lat"),(double) user.get("lng"));
        googleMap.addMarker(new MarkerOptions().position(jayLatLng).title((String)user.get("username")).snippet((String)user.get("main motive")).icon(BitmapDescriptorFactory.fromResource(R.drawable.f_icon)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jayLatLng,15));

/*

        List<HashMap<String,Object>> userList = new ArrayList<HashMap<String,Object>>();

        documentReference.get(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG1, "onSuccess: user profile is created for " + userID);

                //With said ddata .ToList()
                //userList = dataFromDatabase.ToList();
                //userList = aVoid.toList
                //get data back out
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG1, "onFailure: " + e.toString());
            }
        });

        //TODO:
        //Link list to Database
        // for loop is for getiing correct user profile

        for (int i = 0; i < userList.size(); i++) {
            Address address = (Address)user.get("address");
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng).title((String)userList.get(i).get("username")).snippet((String)userList.get(i).get("main motive")).icon(BitmapDescriptorFactory.fromResource(R.drawable.f_icon)));
            if (i == 0){
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            }
        }

        LatLng George = new LatLng(53.8198745, -1.5677403);
        googleMap.addMarker(new MarkerOptions().position(George).title("George99").snippet("Football").icon(BitmapDescriptorFactory.fromResource(R.drawable.f_icon)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(George,15));

*/
    }

}


