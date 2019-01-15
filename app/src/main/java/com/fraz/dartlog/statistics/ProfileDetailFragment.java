package com.fraz.dartlog.statistics;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;

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
    private View rootView;
    private GameData highestCheckoutGame;
    private GameData fewestTurns301Game;
    private GameData fewestTurns501Game;
    private int gamesWon;
    private int gamesPlayed;
    private long startTime;
    private boolean finishedLoadingSummary;
    private boolean finishedLoadingHighscores;


    private class AsyncTaskFetchSummaryData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance(getContext());
            gamesWon = databaseHelper.getNumberOfGamesWon(profileName);
            gamesPlayed = databaseHelper.getNumberOfGamesPlayed(profileName);
            finishedLoadingSummary = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            RelativeLayout layout = rootView.findViewById(R.id.summary_container);
            ProgressBar progressBar = layout.findViewById(R.id.progress_bar);
            if(progressBar != null){
                layout.removeView(progressBar);
            }
            addSummaryData();
        }
    }

    private class AsyncTaskFetchHighScores extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance(getContext());
            highestCheckoutGame = databaseHelper.getHighestCheckoutGame(profileName);
            fewestTurns301Game = databaseHelper.getFewestTurns301Game(profileName);
            fewestTurns501Game = databaseHelper.getFewestTurns501Game(profileName);

            if (highestCheckoutGame == null && fewestTurns301Game  == null && fewestTurns501Game  == null) {
                databaseHelper.refreshStatistics(profileName);
                highestCheckoutGame = databaseHelper.getHighestCheckoutGame(profileName);
                fewestTurns301Game = databaseHelper.getFewestTurns301Game(profileName);
                fewestTurns501Game = databaseHelper.getFewestTurns501Game(profileName);
            }

            finishedLoadingHighscores = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //LinearLayout linearLayout = rootView.findViewById(R.id.profile_detail_linear_layout);
            //initFewestTurnsGames(linearLayout);
            //initHighestOutGame(linearLayout);
        }
    }

    private class AsyncTaskProgressBarHandler extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            while(!finishedLoadingSummary || !finishedLoadingHighscores){
                if (SystemClock.uptimeMillis() - startTime > 1000){
                    publishProgress();
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            if(!finishedLoadingSummary){
                RelativeLayout summaryLayout = rootView.findViewById(R.id.summary_container);
                ProgressBar progressBar =  new ProgressBar(getContext());
                setLayoutParams(progressBar, 40, 40);
                summaryLayout.addView(progressBar);
            }

            //if(finishedLoadingHighscores){
            //    addSummaryProgressBar();
            //}
        }
    }


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.profile_detail, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(profileName);
        }

        startTime = SystemClock.uptimeMillis();
        AsyncTaskFetchSummaryData runnerSummary = new AsyncTaskFetchSummaryData();
        AsyncTaskFetchHighScores runnerHighScores = new AsyncTaskFetchHighScores();
        AsyncTaskProgressBarHandler runnerProgressBar = new AsyncTaskProgressBarHandler();
        runnerHighScores.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        runnerSummary.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        runnerProgressBar.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        return rootView;
    }


    private void setLayoutParams(View view, int height_dp, int width_dp){
        int width =  (int)(width_dp * getContext().getResources().getDisplayMetrics().density);
        int height =   (int)(height_dp * getContext().getResources().getDisplayMetrics().density);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(width, height);
        relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        view.setLayoutParams(relativeParams);
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

    private void initFewestTurnsGames(LinearLayout linearLayout) {
        TextView fewestTurnsHeader =
                (TextView) linearLayout.findViewById(R.id.profile_detail_fewest_turns_container);
        fewestTurnsHeader.setText(R.string.fewest_turns);

        int index = linearLayout.indexOfChild(fewestTurnsHeader) + 1;
        if (fewestTurns301Game != null)
            addFewestTurnsView(fewestTurns301Game, linearLayout, index);
        if (fewestTurns501Game != null)
            addFewestTurnsView(fewestTurns501Game, linearLayout, index);
    }

    private void initHighestOutGame(LinearLayout linearLayout) {
        TextView highestOutHeader =
                (TextView) linearLayout.findViewById(R.id.profile_detail_highest_out_container);
        int index = linearLayout.indexOfChild(highestOutHeader) + 1;
        highestOutHeader.setText(R.string.highest_out);

        if (highestCheckoutGame != null)
            addCheckoutView(highestCheckoutGame, linearLayout, index);
    }

    private void addCheckoutView(final GameData game, LinearLayout linearLayout, final int index) {
        MatchItemView matchItemView = new MatchItemView(getContext());
        matchItemView.setStatToShow(MatchItemView.Stat.CHECKOUT);
        addGameView(matchItemView, game, linearLayout, index);
    }

    private void addFewestTurnsView(final GameData game, LinearLayout linearLayout, final int index) {
        MatchItemView matchItemView = new MatchItemView(getContext());
        matchItemView.setStatToShow(MatchItemView.Stat.TURNS);
        addGameView(matchItemView, game, linearLayout, index);
    }

    private void addGameView(MatchItemView matchItemView, final GameData game, LinearLayout linearLayout, final int index) {
        matchItemView.setVisibility(View.VISIBLE);
        matchItemView.setGame(game, profileName);
        matchItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchFragment.newInstance(game).show(getFragmentManager(),
                        "overview_game_" + Integer.toString(index));
            }
        });

        linearLayout.addView(matchItemView, index);
    }
}
