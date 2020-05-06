package com.example.motive;


import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */

public class MentalFragment extends Fragment {


    public MentalFragment() {
        // Required empty public constructor
    }

    TextView wellbeing;


    private void FindViewById (View view) {
        wellbeing = view.findViewById(R.id.mentalTextView);
        wellbeing.setPaintFlags(wellbeing.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mental, container, false);
        FindViewById(view);
        return view;
    }
}
