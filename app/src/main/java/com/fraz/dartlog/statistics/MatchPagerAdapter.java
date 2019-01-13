package com.fraz.dartlog.statistics;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Filip on 2017-02-05.
 */

public class MatchPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<GameData> games;
    private boolean isMatchSummary;

    public MatchPagerAdapter(FragmentManager fm, ArrayList<GameData> games) {
        super(fm);
        this.games = games;
        this.isMatchSummary = false;
    }

    public MatchPagerAdapter(FragmentManager fm, ArrayList<GameData> games, boolean isMatchSummary) {
        super(fm);
        this.games = games;
        this.isMatchSummary = isMatchSummary;
    }

    @Override
    public Fragment getItem(int position) {
        return MatchFragment.newInstance(games.get(position));
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String tabLabelPrefix;
        if (isMatchSummary)
            tabLabelPrefix = "Leg";
        else
            tabLabelPrefix = "Match";
        return String.format(Locale.getDefault(), "%s %d", tabLabelPrefix, position + 1);
    }
}
