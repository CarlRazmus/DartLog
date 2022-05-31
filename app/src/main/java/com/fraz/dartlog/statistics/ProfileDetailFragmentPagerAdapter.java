package com.fraz.dartlog.statistics;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProfileDetailFragmentPagerAdapter extends FragmentPagerAdapter {

    private String profileName;

    public ProfileDetailFragmentPagerAdapter(FragmentManager fm, String profileName) {
        super(fm);
        this.profileName = profileName;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            default:
            case 0:
                fragment = new ProfileDetailFragment();
                break;
            case 1:
                fragment = new MatchHistoryFragment();
                break;
        }
        Bundle arguments = new Bundle();
        arguments.putString(ProfileDetailFragment.ARG_ITEM_NAME, profileName);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            default:
            case 0:
                return "Overview";
            case 1:
                return "Match history";
        }
    }
}
