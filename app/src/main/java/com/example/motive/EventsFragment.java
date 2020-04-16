package com.example.motive;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {


    public EventsFragment() {
        // Required empty public constructor
    }

    //variables
    ImageView golfimage;
    ImageView beerimage;
    ImageView spanishimage;


    private void FindViewById(View view) {

        golfimage = view.findViewById(R.id.golfImageView);
        beerimage = view.findViewById(R.id.beerImageView);
        spanishimage = view.findViewById(R.id.spanishImageView);

        golfimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jayIntent = new Intent(getActivity(),
                        EventOneActivity.class);
                startActivity(jayIntent);
            }
        });

        beerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jayIntent = new Intent(getActivity(),
                        EventOneActivity.class);
                startActivity(jayIntent);
            }
        });

        spanishimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jayIntent = new Intent(getActivity(),
                        EventOneActivity.class);
                startActivity(jayIntent);
            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        FindViewById(view);
        return view;
    }
}

