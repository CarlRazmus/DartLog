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

    private final ArrayList<GameData> games = new ArrayList<>();
    private Bundle extras;

    public GamePagerAdapter(FragmentManager fm, Bundle extras) {
        super(fm);
        this.extras = extras;
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
        return String.format(Locale.getDefault(), "Leg %d", games.size() + 1);
    }
}
