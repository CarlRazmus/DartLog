package com.fraz.dartlog.statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

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


        LinearLayout linearLayout =
                (LinearLayout) rootView.findViewById(R.id.profile_detail_linear_layout);

        initSummary(rootView);
        initBestGames(linearLayout);
        initHighestOutGame(linearLayout);
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

    private void initBestGames(LinearLayout linearLayout) {
        TextView bestGameHeader =
                (TextView) linearLayout.findViewById(R.id.profile_detail_best_game_label);
        int index = linearLayout.indexOfChild(bestGameHeader) + 1;
        bestGameHeader.setText(R.string.best_game);
        final HashMap<String, GameData> bestGames = getBestGame(playerGameData);
        for (GameData gameData : bestGames.values()) {
            addGameView(gameData, linearLayout, index);
        }
    }

    private void initHighestOutGame(LinearLayout linearLayout) {
        TextView highestOutHeader =
                (TextView) linearLayout.findViewById(R.id.profile_detail_highest_out_label);
        int index = linearLayout.indexOfChild(highestOutHeader) + 1;
        highestOutHeader.setText(R.string.highest_out);
        final GameData highestOutGame = getHighestOut(playerGameData);
        if (highestOutGame != null)
            addGameView(highestOutGame, linearLayout, index);
    }

    private HashMap<String, GameData> getBestGame(ArrayList<GameData> playerGameData) {
        HashMap<String, Integer> leastTurns = new HashMap<>();
        HashMap<String, GameData> bestGames = new HashMap<>();
        for (GameData game : playerGameData) {
            if(game.getGameType().equals("x01") &&
               game.getWinner().getPlayerName().equals(profileName))
            {
                String detailedGameType = game.getDetailedGameType();
                int turns = game.getTurns();
                if (bestGames.containsKey(detailedGameType))
                {
                    if (turns < leastTurns.get(detailedGameType)) {
                        bestGames.put(detailedGameType, game);
                        leastTurns.put(detailedGameType, turns);
                    }
                } else {
                    bestGames.put(detailedGameType, game);
                    leastTurns.put(detailedGameType, turns);
                }
            }
        }
        return bestGames;
    }

    private GameData getHighestOut(ArrayList<GameData> playerGameData) {
        int highestOut = Integer.MIN_VALUE;
        GameData highestOutGame = null;
        for (GameData game : playerGameData) {
            if(game.getGameType().equals("x01") &&
                    game.getWinner().getPlayerName().equals(profileName))
            {
                int out = game.getWinner().getScoreHistory().getLast();
                if (out > highestOut)
                {
                    highestOutGame = game;
                    highestOut = out;
                }
            }
        }
        return highestOutGame;
    }

    private void addGameView(final GameData game, LinearLayout linearLayout, final int index) {
        MatchItemView matchItemView = new MatchItemView(getContext());
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
