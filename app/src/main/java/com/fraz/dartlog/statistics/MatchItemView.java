package com.fraz.dartlog.statistics;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MatchItemView extends FrameLayout {

    private TextView dateView;
    private TextView gameType;
    private TextView result;
    private TextView players;
    private GameData game;
    private String playerName;
    private Context context;

    public MatchItemView(Context context) {
        super(context);
        initView(context);
    }

    public MatchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.match_history_item, this);
        gameType = (TextView) findViewById(R.id.match_history_game_type);
        players = (TextView) findViewById(R.id.match_history_players);
        result = (TextView) findViewById(R.id.match_history_result);
        dateView = (TextView) findViewById(R.id.match_history_date);
    }

    public void setGame(GameData game, String playerName) {
        this.game = game;
        this.playerName = playerName;
        initGame();
    }

    private void initGame() {
        gameType.setText(game.getDetailedGameType().toUpperCase());
        initPlayers();
        initResult();
        initDate();
        invalidate();
    }


    private void initPlayers() {
        int numberOfPlayers = game.getNumberOfPlayers();
        String text;
        if (numberOfPlayers == 1)
            text = String.format(Locale.getDefault(), "%d playerName", numberOfPlayers);
        else
            text = String.format(Locale.getDefault(), "%d players", numberOfPlayers);
        players.setText(text);
    }

    private void initResult() {
        if (game.getPlayer(playerName).equals(game.getWinner())) {
            result.setText(R.string.win);
            result.setTextColor(context.getResources().getColor(R.color.green_win));
        }
        else {
            result.setText(R.string.loss);
            result.setTextColor(context.getResources().getColor(R.color.red_loss));
        }
    }

    private void initDate() {
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
}
