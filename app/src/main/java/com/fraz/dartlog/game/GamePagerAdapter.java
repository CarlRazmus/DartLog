package com.fraz.dartlog.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fraz.dartlog.game.x01.X01GameActivity;
import com.fraz.dartlog.statistics.MatchFragment;

import java.util.ArrayList;
import java.util.Locale;

class GamePagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<GameData> games;
    private Bundle extras;

    public GamePagerAdapter(FragmentManager fm, ArrayList<GameData> games) {
        super(fm);
        this.games = games;
    }

    @Override
    public Fragment getItem(int position) {
        return MatchFragment.newInstance(games.get(games.size() - 1 - position));
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.format(Locale.getDefault(), "Leg %d", games.size() - position);
    }
}
