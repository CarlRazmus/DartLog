package com.fraz.dartlog.statistics;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;
import java.util.Locale;

public class MatchPagerActivity extends AppCompatActivity {

    public static final String ARG_ITEM_NAME = "ARG_NAME";
    public static final String ARG_ITEM_POSITION = "ARG_POSITION";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String profileName = getIntent().getStringExtra(ARG_ITEM_NAME);
        int position = getIntent().getIntExtra(ARG_ITEM_POSITION, 0);
        DartLogDatabaseHelper databaseHelper = new DartLogDatabaseHelper(this);
        ArrayList<GameData> playerGameData = databaseHelper.getPlayerMatchData(profileName);
        setContentView(R.layout.activity_match_pager);

        MatchPagerAdapter adapter =
                new MatchPagerAdapter(getSupportFragmentManager(), playerGameData);
        ViewPager matchPager = (ViewPager) findViewById(R.id.match_pager);
        matchPager.setAdapter(adapter);
        matchPager.setCurrentItem(position);
        matchPager.addOnPageChangeListener(new OnPageChangeListener());
        UpdateToolbar(position);
    }

    private void UpdateToolbar(int position) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(
                String.format(Locale.getDefault(), "%s #%d", getTitle(), position + 1));
    }

    class OnPageChangeListener extends ViewPager.SimpleOnPageChangeListener
    {
        @Override
        public void onPageSelected(int position) {
            UpdateToolbar(position);
        }
    }
}
