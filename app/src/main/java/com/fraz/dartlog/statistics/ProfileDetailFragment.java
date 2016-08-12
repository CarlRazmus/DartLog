package com.fraz.dartlog.statistics;

import android.app.Activity;
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
    private ArrayList<PlayerData> playerData;

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
            String name = getArguments().getString(ARG_ITEM_NAME);
            DartLogDatabaseHelper databaseHelper = new DartLogDatabaseHelper(getActivity());
            playerData = databaseHelper.getPlayerMatchData(name);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_detail, container, false);

        int playerWins = 0;
        for(PlayerData player : playerData)
        {
            if(player.getScore() == 0)
                playerWins++;
        }

        ((TextView) rootView.findViewById(R.id.profile_detail_games_played))
                .setText(String.format(Locale.getDefault(), "%d", playerData.size()));

        ((TextView) rootView.findViewById(R.id.profile_detail_games_won))
                .setText(String.format(Locale.getDefault(), "%d", playerWins));


        RecyclerView recyclerView =
                (RecyclerView) rootView.findViewById(R.id.recent_games_list);
        assert recyclerView != null;
        RecentGamesRecyclerViewAdapter recyclerViewAdapter = new ProfileDetailFragment.RecentGamesRecyclerViewAdapter(playerData);
        recyclerView.setAdapter(recyclerViewAdapter);

        return rootView;
    }

    public class RecentGamesRecyclerViewAdapter
            extends RecyclerView.Adapter<RecentGamesRecyclerViewAdapter.ViewHolder> {

        private static final int NUMBER_OF_GAMES = 5;
        private final ArrayList<PlayerData> playerData;

        public RecentGamesRecyclerViewAdapter(ArrayList<PlayerData> playerData) {
            this.playerData = playerData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_history_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mPlayer = playerData.get(position);

            TextView result = (TextView) holder.mView.findViewById(R.id.match_history_result);

            if (holder.mPlayer.getScore() == 0) {
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
            date.setText("");
        }

        @Override
        public int getItemCount() {
            return Math.min(playerData.size(), NUMBER_OF_GAMES);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public PlayerData mPlayer;

            public ViewHolder(View view) {
                super(view);
                mView = view;
            }
        }
    }
}
