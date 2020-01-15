package com.fraz.dartlog.statistics;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.fraz.dartlog.R;
import com.fraz.dartlog.Util;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * A fragment representing a single Profile detail screen.
 * This fragment is either contained in a {@link ProfileListActivity}
 * in two-pane mode (on tablets) or a {@link ProfileDetailActivity}
 * on handsets.
 */
public class ProfileDetailFragment extends Fragment {

    public static final String ARG_ITEM_NAME = "item_name";

    /**
     * Name of the profile this fragment is presenting.
     */
    private String profileName;
    private long profileId;
    private View rootView;
    private GameData highestCheckoutGame;
    private GameData fewestTurns301Game;
    private GameData fewestTurns501Game;
    private int gamesWon;
    private int gamesPlayed;
    private long startTime;
    private boolean finishedLoadingSummary;
    private boolean finishedLoadingHighscores;
    private int unique_id = 0;
    private AsyncTaskFetchSummaryData runnerSummary;
    private AsyncTaskFetchHighScores runnerHighScores;
    private AsyncTaskProgressBarHandler runnerProgressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProfileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileName = getArguments().getString(ARG_ITEM_NAME);
    }

    @Override
    public void onDestroyView()
    {
        if (runnerSummary != null)
        runnerSummary.cancel(true);

        if (runnerHighScores != null)
            runnerHighScores.cancel(true);

        if (runnerProgressBar != null)
            runnerProgressBar.cancel(true);

        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.profile_detail, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null)
            toolbar.setTitle(profileName);

        LinearLayout fewestTurnsLayout = rootView.findViewById(R.id.profile_detail_fewest_turns_container);
        TextView fewestTurnHeader = fewestTurnsLayout.findViewById(R.id.header);
        fewestTurnHeader.setText(R.string.fewest_turns);

        LinearLayout highestCheckoutLayout = rootView.findViewById(R.id.profile_detail_highest_out_container);
        TextView highestCheckoutHeader = highestCheckoutLayout.findViewById(R.id.header);
        highestCheckoutHeader.setText(R.string.highest_out);
        return rootView;
    }

    @Override
    public void onDestroy(){
        Log.d("hej", "destroying activity!");
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        runnerSummary = new AsyncTaskFetchSummaryData();
        runnerHighScores = new AsyncTaskFetchHighScores();
        runnerProgressBar = new AsyncTaskProgressBarHandler();

        startTime = SystemClock.uptimeMillis();

        //Executor exec = Executors.newFixedThreadPool(3);

        runnerSummary.executeOnExecutor(Util.getExecutorInstance());
        runnerHighScores.executeOnExecutor(Util.getExecutorInstance());
        runnerProgressBar.executeOnExecutor(Util.getExecutorInstance());
    }

    private void removeProgressBar(int id_){
        View layout = rootView.findViewById(id_);
        ProgressBar progressBar = layout.findViewById(R.id.progress_bar);

        if(layout.getClass().equals(RelativeLayout.class)){
            if(progressBar != null){
                ((RelativeLayout)layout).removeView(progressBar);
            }
        }
        if(layout.getClass().equals(LinearLayout.class)){
            if(progressBar != null){
                ((LinearLayout)layout).removeView(progressBar);
            }
        }
    }

    private void addSummaryData() {
        LinearLayout profileLayout = rootView.findViewById(R.id.profile_detail_linear_layout);
        profileLayout.removeViewAt(0);
        View summaryView = getLayoutInflater().inflate(R.layout.profile_match_summary, null);
        ((TextView) summaryView.findViewById(R.id.profile_detail_games_played))
                .setText(String.format(Locale.getDefault(), "%d", gamesPlayed));

        ((TextView) summaryView.findViewById(R.id.profile_detail_games_won))
                .setText(String.format(Locale.getDefault(), "%d", gamesWon));
        profileLayout.addView(summaryView, 0);
    }

    private void addFewestTurnsGames() {
        LinearLayout fewestTurnsLayout = rootView.findViewById(R.id.profile_detail_fewest_turns_container);
        if (fewestTurns301Game != null)
            addFewestTurnsView(fewestTurns301Game, fewestTurnsLayout);
        if (fewestTurns501Game != null)
            addFewestTurnsView(fewestTurns501Game, fewestTurnsLayout);
    }

    private void addHighestOutGame( ) {
        LinearLayout linearLayout= rootView.findViewById(R.id.profile_detail_highest_out_container);
        if (highestCheckoutGame != null){
            MatchItemView matchItemView = new MatchItemView(getContext());
            matchItemView.setStatToShow(MatchItemView.Stat.CHECKOUT);
            addGameView(matchItemView, highestCheckoutGame, linearLayout);
        }
    }

    private void addFewestTurnsView(final GameData game, LinearLayout linearLayout) {
        MatchItemView matchItemView = new MatchItemView(getContext());
        matchItemView.setStatToShow(MatchItemView.Stat.TURNS);
        addGameView(matchItemView, game, linearLayout);
    }

    private void addGameView(MatchItemView matchItemView, final GameData game, LinearLayout linearLayout) {
        matchItemView.setVisibility(View.VISIBLE);
        matchItemView.setGame(game, profileName);
        matchItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchFragment.newInstance(game).show(getFragmentManager(),
                        "game_overview_" + String.valueOf(unique_id));
                unique_id += 1;
            }
        });
        linearLayout.addView(matchItemView);
    }


    private class AsyncTaskFetchSummaryData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Thread.currentThread().setName(profileName + "Summary");
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance(getContext());
            gamesWon = databaseHelper.getNumberOfGamesWon(profileName);
            gamesPlayed = databaseHelper.getNumberOfGamesPlayed(profileName);
            finishedLoadingSummary = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            removeProgressBar(R.id.summary_container);
            addSummaryData();
        }
    }

    private class AsyncTaskFetchHighScores extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance(getContext());
            profileId = databaseHelper.getPlayerId(profileName);
            Thread.currentThread().setName(profileName + "_Highscore");

            ArrayList<Pair<String, GameData>> highestCheckoutGames = databaseHelper.getHighestCheckouts(profileId);
            //fewestTurns301Game = databaseHelper.getFewestTurns301Game(profileName);
            //fewestTurns501Game = databaseHelper.getFewestTurns501Game(profileName);
            finishedLoadingHighscores = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            removeProgressBar(R.id.profile_detail_fewest_turns_container);
            removeProgressBar(R.id.profile_detail_highest_out_container);
            addFewestTurnsGames();
            addHighestOutGame();
        }
    }

    private class AsyncTaskProgressBarHandler extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Thread.currentThread().setName(profileName + "_Progressbar");
            while(!finishedLoadingSummary || !finishedLoadingHighscores)
                if (SystemClock.uptimeMillis() - startTime > 1000)
                    break;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(!finishedLoadingSummary){
                RelativeLayout summaryLayout = rootView.findViewById(R.id.summary_container);
                getLayoutInflater().inflate(R.layout.circular_progress_bar, summaryLayout);
            }
            if(!finishedLoadingHighscores){
                LinearLayout fewestTurnLayout = rootView.findViewById(R.id.profile_detail_fewest_turns_container);
                LinearLayout highestOutLayout = rootView.findViewById(R.id.profile_detail_highest_out_container);
                getLayoutInflater().inflate(R.layout.linear_progress_bar, fewestTurnLayout);
                getLayoutInflater().inflate(R.layout.linear_progress_bar, highestOutLayout);
            }
        }
    }
}
