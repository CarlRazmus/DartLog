package com.fraz.dartlog.statistics;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;

/**
 * Created by Filip on 2017-02-05.
 */

public class MatchPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<GameData> games;

    MatchPagerAdapter(FragmentManager fm, ArrayList<GameData> games) {
        super(fm);
        this.games = games;
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
