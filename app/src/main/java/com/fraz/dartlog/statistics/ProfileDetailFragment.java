package com.fraz.dartlog.statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

import java.util.ArrayList;
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
     * The content this fragment is presenting.
     */
    private ArrayList<GameData> playerGameData;

    /**
     * Name of the profile this fragment is presenting.
     */
    private String profileName;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProfileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_NAME)) {
            profileName = getArguments().getString(ARG_ITEM_NAME);
            DartLogDatabaseHelper databaseHelper = new DartLogDatabaseHelper(getActivity());
            playerGameData = databaseHelper.getPlayerMatchData(profileName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_detail, container, false);

        Toolbar toolbar =
                (Toolbar) getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(profileName);
        }

        initSummary(rootView);
        initBestGame(rootView);
        return rootView;
    }

    private void initSummary(View rootView) {
        TextView summaryHeader =
                (TextView) rootView.findViewById(R.id.profile_detail_summary_label);
        summaryHeader.setText(R.string.summary);

        int playerWins = 0;
        for(GameData game : playerGameData)
        {
            PlayerData player = game.getPlayer(profileName);
            if(player == game.getWinner())
                playerWins++;
        }

        ((TextView) rootView.findViewById(R.id.profile_detail_games_played))
                .setText(String.format(Locale.getDefault(), "%d", playerGameData.size()));

        ((TextView) rootView.findViewById(R.id.profile_detail_games_won))
                .setText(String.format(Locale.getDefault(), "%d", playerWins));

    }

    private void initBestGame(View rootView) {
        TextView bestGameHeader =
                (TextView) rootView.findViewById(R.id.profile_detail_best_game_label);
        bestGameHeader.setText(R.string.best_game);

        final GameData bestGame = getBestGame(playerGameData);
        MatchItemView bestGameView =
                (MatchItemView) rootView.findViewById(R.id.profile_detail_best_game);
        if (bestGame != null) {
            bestGameView.setVisibility(View.VISIBLE);
            bestGameView.setGame(bestGame, profileName);
            bestGameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MatchFragment.newInstance(bestGame).show(getFragmentManager(), "bestGame");
                }
            });
        } else {
            bestGameView.setVisibility(View.GONE);
        }
    }

    private GameData getBestGame(ArrayList<GameData> playerGameData) {
        if (playerGameData.isEmpty())
            return null;

        GameData currentBestGame = null;
        int least_turns = Integer.MAX_VALUE;
        for (GameData gameData : playerGameData) {
            if(gameData.getGameType().equals("x01") &&
               gameData.getWinner().getPlayerName().equals(profileName))
            {
                int turns = gameData.getTurns();
                if (turns < least_turns) {
                    currentBestGame = gameData;
                    least_turns = turns;
                }
            }
        }
        return currentBestGame;
    }
}
