package com.example.motive;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.OnMapReadyCallback;


public class MotiveHome extends AppCompatActivity implements OnMapReadyCallback {

    ImageView myProfileIcon;
    ImageView settingsIcon;
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    List<Fragment> FragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FindViewsById();

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        final Context context = this;


        Toast.makeText(context, "Map Ready Called", Toast.LENGTH_LONG).show();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {



                final String[] items = {"View Profile", "Message", "Close"};
                new AlertDialog.Builder(context).setTitle(marker.getTitle()).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, items[i], Toast.LENGTH_LONG).show();
                        switch (i)
                        {
                            case 0:
                                //Start profile
                                break;
                            case 1:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                    }
                }).show();


                return false;
            }
        });

        // Add a marker in Leeds and move the camera
        LatLng Jay = new LatLng(53.8179462, -1.5687024);
        googleMap.addMarker(new MarkerOptions().position(Jay).title("Jay").snippet("Football").icon(BitmapDescriptorFactory.fromResource(R.drawable.f_icon)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Jay,15));

        LatLng George = new LatLng(53.8198745, -1.5677403);
        googleMap.addMarker(new MarkerOptions().position(George).title("George").snippet("Football").icon(BitmapDescriptorFactory.fromResource(R.drawable.f_icon)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(George,15));

        LatLng Tom = new LatLng(53.8177046, -1.5633514);
        googleMap.addMarker(new MarkerOptions().position(Tom).title("Tom").snippet("Tennis").icon(BitmapDescriptorFactory.fromResource(R.drawable.tennis_icon)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Tom,15));

        LatLng Mick = new LatLng(53.8205374, -1.5660997);
        googleMap.addMarker(new MarkerOptions().position(Mick).title("Millie").snippet("Chess").icon(BitmapDescriptorFactory.fromResource(R.drawable.chess_icon)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Mick,15));


    }

}


