package com.fraz.dartlog.game;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fraz.dartlog.game.x01.X01GameActivity;
import com.fraz.dartlog.statistics.MatchFragment;

import java.util.ArrayList;
import java.util.Locale;

class GamePagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<GameData> games = new ArrayList<>();

    public GamePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addGame(GameData game)
    {
        games.add(game);
    }

    @Override
    public Fragment getItem(int position) {
        if (position > 0)
            return MatchFragment.newInstance(games.get(position));
        else
            return new X01GameFragment();
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.format(Locale.getDefault(), "Leg %d", position + 2);
    }
}
