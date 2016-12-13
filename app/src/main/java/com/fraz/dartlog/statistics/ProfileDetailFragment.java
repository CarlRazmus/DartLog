package com.fraz.dartlog.statistics;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
                new ProfileDetailFragment.RecentGamesRecyclerViewAdapter(playerGameData,
                        getBestGame(playerGameData));
        recyclerView.setAdapter(recyclerViewAdapter);

        return rootView;
    }

    private GameData getBestGame(ArrayList<GameData> playerGameData) {
        if (playerGameData.isEmpty())
            return null;

        GameData currentBestGame = null;
        int scores = Integer.MAX_VALUE;
        for (GameData gameData : playerGameData) {
            if (gameData.getWinner().getPlayerName().equals(profileName) &&
                gameData.getPlayer(profileName).getScoreHistory().size() < scores) {
                    currentBestGame = gameData;
            }
        }
        return currentBestGame;
    }

    public class RecentGamesRecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int NUMBER_OF_GAMES = 5;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private final ArrayList<GameData> gameData;
        private GameData bestGame;

        RecentGamesRecyclerViewAdapter(ArrayList<GameData> gameData, GameData bestGame) {
            this.gameData = gameData;
            this.bestGame = bestGame;
            Collections.reverse(this.gameData);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == TYPE_HEADER) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.match_statistics_header, parent, false);
                return new HeaderViewHolder(view);
            } else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.match_statistics, parent, false);
                return new GameViewHolder(view);

            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == TYPE_HEADER) {
                bindHeaderViewHolder((HeaderViewHolder) holder, position);
            } else {
                bindGameViewHolder((GameViewHolder) holder, position);
            }
        }

        private void bindHeaderViewHolder(HeaderViewHolder holder, int position) {
            TextView itemView = (TextView) holder.itemView;
            if(isBestGameHeader(position))
                itemView.setText(R.string.best_game);
            else
                itemView.setText(R.string.recent_games);
        }

        private void bindGameViewHolder(GameViewHolder holder, int position) {
            GameData game;
            if (position == 1)
                game = bestGame;
            else if (bestGame == null)
                game = gameData.get(position - 2);
            else
                game = gameData.get(position - 3);


            holder.gameType.setText(R.string.x01);
            holder.date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).
                            format(game.getDate().getTime()));

            addHeaders(holder, game);
            initializeScoreBoard(holder, game);
        }

        private void initializeScoreBoard(GameViewHolder holder, GameData game) {
            holder.scoreboard.setAdapter(new MatchStatisticsRecyclerViewAdapter(getContext(), game));
            holder.scoreboard.setLayoutManager(new GridLayoutManager(getContext(),
                    game.getNumberOfPlayers() + 1, GridLayoutManager.HORIZONTAL, false));
        }

        private void addHeaders(final GameViewHolder holder, GameData game) {
            holder.headerGroup.removeAllViews();
            TextView turnHeader = createHeaderView("Turn", holder.headerGroup);
            turnHeader.setTypeface(Typeface.DEFAULT_BOLD);
            holder.headerGroup.addView(turnHeader);
            for (int i = 0; i < game.getNumberOfPlayers(); i++) {
                PlayerData player = game.getPlayer(i);
                TextView header = createHeaderView(player.getPlayerName(), holder.headerGroup);
                if (game.getWinner() == player)
                    header.setTextColor(getResources().getColor(R.color.accent_color));
                holder.headerGroup.addView(header);
            }
        }

        private TextView createHeaderView(String text, ViewGroup headerGroup)
        {
            TextView header = (TextView) LayoutInflater.from(getContext())
                    .inflate(R.layout.match_statistics_scoreboard_list_item, headerGroup, false);
            header.setText(text);
            header.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            return header;
        }

        @Override
        public int getItemCount() {
            int extraItems = 2;
            if (bestGame != null)
                extraItems++;
            return Math.min(gameData.size(), NUMBER_OF_GAMES) + extraItems;
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position))
                return TYPE_HEADER;

            return TYPE_ITEM;
        }

        private boolean isHeader(int position) {
            return isBestGameHeader(position) || isRecentGameHeader(position);
        }

        private boolean isRecentGameHeader(int position) {
            if (bestGame != null)
                return position == 2;
            else
                return position == 1;
        }

        private boolean isBestGameHeader(int position) {
            return position == 0;
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {

            HeaderViewHolder(View view) {
                super(view);
            }
        }

        class GameViewHolder extends RecyclerView.ViewHolder {
            RecyclerView scoreboard;
            TextView gameType;
            TextView date;
            ViewGroup headerGroup;


            GameViewHolder(View view) {
                super(view);
                gameType = (TextView) view.findViewById(R.id.match_statistics_game_type);
                date = (TextView) view.findViewById(R.id.match_statistics_game_date);
                headerGroup = (ViewGroup) view.findViewById(R.id.match_statistics_scoreboard_header);
                scoreboard = (RecyclerView) view.findViewById(R.id.match_statistics_scoreboard);
            }
        }
    }
}
