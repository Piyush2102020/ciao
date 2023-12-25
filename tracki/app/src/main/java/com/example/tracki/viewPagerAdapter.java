package com.example.tracki;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class viewPagerAdapter extends FragmentPagerAdapter {
    public viewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position==0){
            return new home();
        } else if (position==1) {
            learn learn=new learn();
            return new learn();
        }
        else {
            return new user();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "Home";
        } else if (position==1) {
            return "Messages";
        }
        else {
            return "Private";
        }
    }
}
