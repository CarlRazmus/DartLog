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

    public GamePagerAdapter(FragmentManager fm, Bundle extras, ArrayList<GameData> games) {
        super(fm);
        this.extras = extras;
        this.games = games;
    }

    @Override
    public Fragment getItem(int position) {
        if (position > 0)
            return MatchFragment.newInstance(games.get(games.size() - position));
        else
        {
            X01GameActivity x01GameActivity = new X01GameActivity();
            x01GameActivity.setArguments(extras);
            return x01GameActivity;
        }
    }

    @Override
    public int getCount() {
        return games.size() + 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.format(Locale.getDefault(), "Leg %d", games.size() + 1 - position);
    }
}
