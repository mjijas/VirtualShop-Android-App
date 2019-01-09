package com.example.ijasahamed.lezyv2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdopter extends FragmentStatePagerAdapter {

    int noOfTebs;

    public PagerAdopter(FragmentManager fm,int NumberOfTebs){
        super(fm);
        this.noOfTebs = NumberOfTebs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                login log = new login();
                return log;

            case 1:
                register registeration = new register();
                return registeration;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTebs;
    }
}
