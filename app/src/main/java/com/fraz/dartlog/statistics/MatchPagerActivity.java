package com.fraz.dartlog.statistics;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;
import java.util.Locale;

public class MatchPagerActivity extends AppCompatActivity {

    public static final String ARG_ITEM_POSITION = "ARG_POSITION";
    public static final String ARG_ITEM_NAME = "ARG_NAME";
    public static final String ARG_MATCHES = "ARG_MATCHES";
    public static final String ARG_IS_MATCH_SUMMARY = "ARG_IS_SUMMARY";
    private ArrayList<GameData> playerGameData;
    private int position;
    private boolean isMatchSummary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getIntent().getIntExtra(ARG_ITEM_POSITION, 0);
        int nrOfMatches = getIntent().getIntExtra(ARG_MATCHES, 0);
        isMatchSummary = getIntent().getBooleanExtra(ARG_IS_MATCH_SUMMARY, false);

        String profileName = getIntent().getStringExtra(ARG_ITEM_NAME);
        DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance(this);
        playerGameData = databaseHelper.getPlayerMatchData(profileName, Long.MAX_VALUE, nrOfMatches);

        setContentView(R.layout.activity_match_pager);
        UpdateToolbar();
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();

    }

    private void UpdateToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());
    }


    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        private MatchPagerAdapter adapter;

        public AsyncTaskRunner() {
            super();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            adapter = new MatchPagerAdapter(getSupportFragmentManager(), playerGameData, isMatchSummary);
            return null;
        }

        protected void onPostExecute(Void result) {
            ViewPager matchPager = (ViewPager) findViewById(R.id.match_pager);
            matchPager.setAdapter(adapter);
            matchPager.setCurrentItem(position);
        }
    }
}
