package com.fraz.dartlog.statistics;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Filip on 2017-02-05.
 */

public class MatchPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<GameData> games;

    public MatchPagerAdapter(FragmentManager fm, ArrayList<GameData> games) {
        super(fm);
        this.games = games;
        Collections.reverse(this.games);
    }

    @Override
    public Fragment getItem(int position) {
        return MatchFragment.newInstance(games.get(position));
    }

    @Override
    public int getCount() {
        return games.size();
    }
}
