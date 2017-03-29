package com.fraz.dartlog.statistics;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MatchTableView extends FrameLayout {

    private final View layout;
    private GameData game;

    public MatchTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = inflater.inflate(R.layout.match_statistics, this);
    }

    public MatchTableView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = inflater.inflate(R.layout.match_statistics, this);
    }

    public MatchTableView(Context  context, GameData game){
        this(context);
        setGame(game);
    }

    private void initializeScoreBoard(View layout) {
        TextView gameType = (TextView) layout.findViewById(R.id.match_statistics_game_type);
        RecyclerView scoreboard = (RecyclerView) layout.findViewById(R.id.match_statistics_scoreboard);
        TextView dateView = (TextView) layout.findViewById(R.id.match_statistics_game_date);

        scoreboard.setAdapter(new MatchStatisticsRecyclerViewAdapter(game));
        scoreboard.setLayoutManager(new GridLayoutManager(getContext(),
                game.getNumberOfPlayers() + 1, GridLayoutManager.HORIZONTAL, false));

        gameType.setText(game.getGameType().toUpperCase());

        Date date = game.getDate().getTime();
        if (DateUtils.isToday(date.getTime()))
            dateView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(),
                    Calendar.getInstance().getTimeInMillis(),
                    DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL));
        else
            dateView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).
                    format(date));
    }

    private void addHeaders(View layout) {
        ViewGroup headerGroup = (ViewGroup) layout.findViewById(R.id.match_statistics_scoreboard_header);
        headerGroup.removeAllViews();
        TextView turnHeader = createHeaderView("Turn", headerGroup);
        turnHeader.setTypeface(Typeface.DEFAULT_BOLD);
        headerGroup.addView(turnHeader);
        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
            PlayerData player = game.getPlayer(i);
            TextView header = createHeaderView(player.getPlayerName(), headerGroup);
            if (game.getWinner() == player)
                header.setTextColor(getResources().getColor(R.color.accent));
            headerGroup.addView(header);
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

    public void setGame(GameData game) {
        this.game = game;
        initializeScoreBoard(layout);
        addHeaders(layout);

        invalidate();
        requestLayout();
    }
}
