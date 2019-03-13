package com.fraz.dartlog.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.x01.X01GameActivity;

import java.util.ArrayList;

public class GameStatisticsFragment extends Fragment{

    private GamePagerAdapter adapter;
    private ArrayList<GameData> games = new ArrayList<>();
    private ViewPager matchPager;


    public static GameStatisticsFragment newInstance(ArrayList<GameData> games) {
        GameStatisticsFragment fragment = new GameStatisticsFragment();
        Bundle args = new Bundle();
        args.putSerializable("games", games);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("games")) {
            games = (ArrayList<GameData>) savedInstanceState.getSerializable("games");
        }
        else
        {
            games = (ArrayList<GameData>) getArguments().getSerializable("games");
        }
        adapter = new GamePagerAdapter(getFragmentManager(), games);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_game_statistics, container, false);
        matchPager = view.findViewById(R.id.game_pager);
        matchPager.setAdapter(adapter);
        matchPager.setCurrentItem(0);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(matchPager);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("games", games);
    }

    public void addGame(GameData game)
    {
        games.add(game);
        adapter.notifyDataSetChanged();
    }

    public void setPagerItem(int item)
    {
        matchPager.setCurrentItem(item, true);
    }
}