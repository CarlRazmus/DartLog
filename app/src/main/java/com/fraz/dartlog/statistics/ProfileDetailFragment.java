package com.fraz.dartlog.statistics;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

import java.text.SimpleDateFormat;
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

        CollapsingToolbarLayout appBarLayout =
                (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(profileName);
        }

        int playerWins = 0;
        for(GameData game : playerGameData)
        {
            PlayerData player = game.getPlayer(profileName);
            if(player.getScore() == 0)
                playerWins++;
        }

        ((TextView) rootView.findViewById(R.id.profile_detail_games_played))
                .setText(String.format(Locale.getDefault(), "%d", playerGameData.size()));

        ((TextView) rootView.findViewById(R.id.profile_detail_games_won))
                .setText(String.format(Locale.getDefault(), "%d", playerWins));


        RecyclerView recyclerView =
                (RecyclerView) rootView.findViewById(R.id.recent_games_list);
        assert recyclerView != null;
        RecentGamesRecyclerViewAdapter recyclerViewAdapter =
                new ProfileDetailFragment.RecentGamesRecyclerViewAdapter(playerGameData);
        recyclerView.setAdapter(recyclerViewAdapter);

        return rootView;
    }

    public class RecentGamesRecyclerViewAdapter
            extends RecyclerView.Adapter<RecentGamesRecyclerViewAdapter.ViewHolder> {

        private static final int NUMBER_OF_GAMES = 5;
        private final ArrayList<GameData> gameData;

        public RecentGamesRecyclerViewAdapter(ArrayList<GameData> gameData) {
            this.gameData = gameData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_history_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mGame = gameData.get(position);

            TextView result = (TextView) holder.mView.findViewById(R.id.match_history_result);

            if (holder.mGame.getWinner().getPlayerName()
                    .equals(profileName)) {
                result.setText("WIN");
                result.setTextColor(ContextCompat.getColor(getContext(), R.color.main_green));
            }
            else {
                result.setText("LOSS");
                result.setTextColor(ContextCompat.getColor(getContext(), R.color.main_red));
            }


            TextView gameType = (TextView) holder.mView.findViewById(R.id.match_history_game_type);
            gameType.setText("X01");

            TextView date = (TextView) holder.mView.findViewById(R.id.match_history_date);
            date.setText(
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).
                            format(holder.mGame.getDate().getTime()));
        }

        @Override
        public int getItemCount() {
            return Math.min(gameData.size(), NUMBER_OF_GAMES);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public GameData mGame;


            public ViewHolder(View view) {
                super(view);
                mView = view;
            }
        }
    }
}
