package com.fraz.dartlog.statistics;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MatchRecyclerViewAdapter extends RecyclerView.Adapter<MatchRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GameData> gameData;
    private String playerName;

    public MatchRecyclerViewAdapter(Context context, ArrayList<GameData> gameData, String playerName) {
        this.context = context;
        this.gameData = gameData;
        this.playerName = playerName;
        Collections.reverse(this.gameData);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GameData game = gameData.get(position);

        holder.gameType.setText(game.getGameType().toUpperCase());
        bindPlayers(holder, game);
        bindResult(holder, game);
        bindDate(holder, game);
    }

    private void bindPlayers(ViewHolder holder, GameData game) {
        int numberOfPlayers = game.getNumberOfPlayers();
        String text;
        if (numberOfPlayers == 1)
            text = String.format(Locale.getDefault(), "%d player", numberOfPlayers);
        else
            text = String.format(Locale.getDefault(), "%d players", numberOfPlayers);
        holder.players.setText(text);
    }

    private void bindResult(ViewHolder holder, GameData game) {
        if (game.getPlayer(playerName).equals(game.getWinner())) {
            holder.result.setText(R.string.win);
            holder.result.setTextColor(context.getResources().getColor(R.color.green_win));
        }
        else {
            holder.result.setText(R.string.loss);
            holder.result.setTextColor(context.getResources().getColor(R.color.red_loss));
        }
    }

    private void bindDate(ViewHolder holder, GameData game) {
        Date date = game.getDate().getTime();
        if (DateUtils.isToday(date.getTime()))
            holder.date.setText(DateUtils.getRelativeTimeSpanString(date.getTime(),
                    Calendar.getInstance().getTimeInMillis(), DateUtils.HOUR_IN_MILLIS));
        else
            holder.date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).
                    format(date));
    }

    @Override
    public int getItemCount() {
        return gameData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView gameType;
        public final TextView players;
        public final TextView result;
        public final TextView date;

        public ViewHolder(View view) {
            super(view);
            gameType = (TextView) view.findViewById(R.id.match_history_game_type);
            players = (TextView) view.findViewById(R.id.match_history_players);
            result = (TextView) view.findViewById(R.id.match_history_result);
            date = (TextView) view.findViewById(R.id.match_history_date);
        }
    }
}
