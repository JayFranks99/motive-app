package com.example.motive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter; /***/
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.OnMapReadyCallback;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

     TabLayout tabLayout;
     ViewPager viewPager;
     PagerAdapter pagerAdapter;
     TabItem tabHome;
     TabItem tabEvents;
     TabItem tabMental;
     TabItem tabMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tabLayout = findViewById(R.id.tabLayout);
//        tabEvents = findViewById(R.id.tabEvents);
//        tabMental = findViewById(R.id.tabMental);
//        tabMessages = findViewById(R.id.tabMessages);
        viewPager = findViewById(R.id.viewPager);

        List<Fragment> FragmentList = new ArrayList<Fragment>();
//        FragmentList.add(new HomeFragment(getSupportFragmentManager()));

        SupportMapFragment map = new com.google.android.gms.maps.SupportMapFragment();
        map.getMapAsync(this);

        FragmentList.add(map);

        FragmentList.add(new EventsFragment());
        FragmentList.add(new MentalFragment());
        FragmentList.add(new MessagesFragment());

        pagerAdapter = new com.example.motive.PagerAdapter(getSupportFragmentManager(), FragmentList);
        viewPager.setAdapter(pagerAdapter);

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

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
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Leeds and move the camera
        LatLng Jay = new LatLng(53.8179462, -1.5687024);
        googleMap.addMarker(new MarkerOptions().position(Jay).title("Jay Franks - Football").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Jay,13));

        LatLng George = new LatLng(53.8198745, -1.5677403);
        googleMap.addMarker(new MarkerOptions().position(George).title("George - Football").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(George,13));

    }

}

