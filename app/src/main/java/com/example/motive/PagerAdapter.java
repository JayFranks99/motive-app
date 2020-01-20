package com.example.motive;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    List <Fragment> FragmentList;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        FragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentList.get(position);
    }

    @Override
    public int getCount() {return FragmentList.size(); }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String returnValue = "";

        switch (position)
        {
            case 0:
                returnValue = "Home";
                break;
            case 1:
                returnValue = "Events";
                break;
            case 2:
                returnValue = "Mental";
                break;
            case 3:
                returnValue = "Messages";
                break;
        }

        return returnValue;

    }
}
