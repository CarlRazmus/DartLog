package com.fraz.dartlog.statistics;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fraz.dartlog.MyApplication;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.fraz.dartlog.statistics.MatchItemView.Stat.CHECKOUT;
import static com.fraz.dartlog.statistics.MatchItemView.Stat.NONE;
import static com.fraz.dartlog.statistics.MatchItemView.Stat.TURNS;

public class MatchItemView extends FrameLayout {

    enum Stat {
        NONE,
        TURNS,
        CHECKOUT
    }

    private TextView dateView;
    private TextView gameType;
    private TextView players;
    private Stat statToShow;

    private GameData game;
    private String playerName;
    private TextView stat_1_label;
    private TextView stat_1;
    private TextView stat_2;

    public MatchItemView(Context context) {
        super(context);
        initView(context);
    }

    public MatchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setStatToShow(Stat stat) {
        statToShow = stat;
    }

    public void setGame(GameData game, String playerName) {
        this.game = game;
        this.playerName = playerName;
        initGame();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.match_history_item, this);
        gameType = findViewById(R.id.match_history_game_type);
        players = findViewById(R.id.match_history_players);
        stat_1_label = findViewById(R.id.match_item_stat_1_label);
        stat_1 = findViewById(R.id.match_item_stat_1);
        stat_2 = findViewById(R.id.match_item_stat_2);
        dateView = findViewById(R.id.match_history_date);

        statToShow = TURNS;
    }

    private void initGame() {
        gameType.setText(game.getDetailedGameType().toUpperCase());
        initPlayers();
        initFirstStatView();
        initSecondStatView();
        initDate();
        invalidate();
    }


    private void initPlayers() {
        int numberOfPlayers = game.getNumberOfPlayers();
        String text;
        if (numberOfPlayers == 1)
            text = String.format(Locale.getDefault(), "%d player", numberOfPlayers);
        else
            text = String.format(Locale.getDefault(), "%d players", numberOfPlayers);
        players.setText(text);
    }

    private void initSecondStatView() {
        if (game.getPlayer(playerName).equals(game.getWinner())) {
            stat_2.setText(R.string.win);
            stat_2.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.accent));
        }
        else {
            stat_2.setText(R.string.loss);
            stat_2.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.red_loss));
        }
    }

    private void initFirstStatView() {
        if (statToShow == NONE) {
            stat_1_label.setVisibility(GONE);
            stat_1.setVisibility(GONE);
        } else {
            stat_1_label.setVisibility(VISIBLE);
            stat_1.setVisibility(VISIBLE);

            if(statToShow == TURNS) {
                stat_1_label.setText(R.string.turns);
                stat_1.setText(String.format(Locale.getDefault(), "%d", game.getTurns()));
            } else if(statToShow == CHECKOUT) {
                stat_1_label.setText(R.string.checkout);
                stat_1.setText(String.format(Locale.getDefault(), "%d", game.getCheckout()));
            }
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
