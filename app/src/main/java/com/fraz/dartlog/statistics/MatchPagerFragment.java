package com.fraz.dartlog.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;
import java.util.Locale;

public class MatchPagerFragment extends Fragment {

    public static final String ARG_ITEM_POSITION = "ARG_POSITION";
    public static final String ARG_ITEM_NAME = "ARG_NAME";
    public static final String ARG_MATCHES = "ARG_MATCHES";
    private ArrayList<GameData> playerGameData;
    private int position;

    public static MatchPagerFragment newInstance(String playerName, int position, int numMatches) {
        MatchPagerFragment matchPagerFragment = new MatchPagerFragment();
        Bundle args = new Bundle();
        args.putString(MatchPagerFragment.ARG_ITEM_NAME, playerName);
        args.putInt(MatchPagerFragment.ARG_ITEM_POSITION, position);
        args.putInt(MatchPagerFragment.ARG_MATCHES, numMatches);
        matchPagerFragment.setArguments(args);
        return matchPagerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null)
        {
            position = arguments.getInt(ARG_ITEM_POSITION, 0);
            int nrOfMatches = arguments.getInt(ARG_MATCHES, 0);
            String profileName = arguments.getString(ARG_ITEM_NAME);

            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance();
            playerGameData = databaseHelper.getPlayerMatchData(profileName, Long.MAX_VALUE, nrOfMatches);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewPager view = (ViewPager) inflater.inflate(R.layout.activity_match_pager, container, false);

        MatchPagerAdapter adapter =
                new MatchPagerAdapter(getFragmentManager(), playerGameData);
        view.setAdapter(adapter);
        view.setCurrentItem(position);
        view.addOnPageChangeListener(new OnPageChangeListener());
        UpdateToolbar(position, view);

        return view;
    }

    private void UpdateToolbar(int position, View view) {
        if (view != null)
        {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle(
                        String.format(Locale.getDefault(), "Match %d", position + 1));
            }
        }
    }

    class OnPageChangeListener extends ViewPager.SimpleOnPageChangeListener
    {
        @Override
        public void onPageSelected(int position) {
            UpdateToolbar(position, getView());
        }
    }
}
