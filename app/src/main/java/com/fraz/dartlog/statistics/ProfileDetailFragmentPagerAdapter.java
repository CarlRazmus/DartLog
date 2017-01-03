package com.fraz.dartlog.statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Filip on 2017-01-03.
 */

public class ProfileDetailFragmentPagerAdapter extends FragmentPagerAdapter {

    private String profileName;

    public ProfileDetailFragmentPagerAdapter(FragmentManager fm, String profileName) {
        super(fm);
        this.profileName = profileName;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putString(ProfileDetailFragment.ARG_ITEM_NAME, profileName);
        ProfileDetailFragment fragment = new ProfileDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
