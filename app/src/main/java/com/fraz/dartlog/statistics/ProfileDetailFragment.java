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

import java.util.Locale;

/**
 * A fragment representing a single Profile detail screen.
 * This fragment is either contained in a {@link ProfileListActivity}
 * in two-pane mode (on tablets) or a {@link ProfileDetailActivity}
 * on handsets.
 */
public class ProfileDetailFragment extends Fragment {

    public static final String ARG_ITEM_NAME = "item_name";

    private GameData highestCheckoutGame;
    private GameData fewestTurns301Game;
    private GameData fewestTurns501Game;
    private int gamesWon;
    private int gamesPlayed;

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
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance(getActivity());
            gamesWon = databaseHelper.getNumberOfGamesWon(profileName);
            gamesPlayed = databaseHelper.getNumberOfGamesPlayed(profileName);
            highestCheckoutGame = databaseHelper.getHighestCheckoutGame(profileName);
            fewestTurns301Game = databaseHelper.getFewestTurns301Game(profileName);
            fewestTurns501Game = databaseHelper.getFewestTurns501Game(profileName);
            if (highestCheckoutGame == null && fewestTurns301Game  == null && fewestTurns501Game  == null) {
                databaseHelper.refreshStatistics(profileName);
                highestCheckoutGame = databaseHelper.getHighestCheckoutGame(profileName);
                fewestTurns301Game = databaseHelper.getFewestTurns301Game(profileName);
                fewestTurns501Game = databaseHelper.getFewestTurns501Game(profileName);
            }

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
        initFewestTurnsGames(linearLayout);
        initHighestOutGame(linearLayout);
        return rootView;
    }

    private void initSummary(View rootView) {
        ((TextView) rootView.findViewById(R.id.profile_detail_games_played))
                .setText(String.format(Locale.getDefault(), "%d", gamesPlayed));

        ((TextView) rootView.findViewById(R.id.profile_detail_games_won))
                .setText(String.format(Locale.getDefault(), "%d", gamesWon));
    }

    private void initFewestTurnsGames(LinearLayout linearLayout) {
        TextView fewestTurnsHeader =
                (TextView) linearLayout.findViewById(R.id.profile_detail_fewest_turns_label);
        int index = linearLayout.indexOfChild(fewestTurnsHeader) + 1;
        fewestTurnsHeader.setText(R.string.fewest_turns);
        if (fewestTurns301Game != null)
            addFewestTurnsView(fewestTurns301Game, linearLayout, index);
        if (fewestTurns501Game != null)
            addFewestTurnsView(fewestTurns501Game, linearLayout, index);
    }

    private void initHighestOutGame(LinearLayout linearLayout) {
        TextView highestOutHeader =
                (TextView) linearLayout.findViewById(R.id.profile_detail_highest_out_label);
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
