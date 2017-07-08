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

import static com.fraz.dartlog.statistics.MatchItemView.Stat.CHECKOUT;
import static com.fraz.dartlog.statistics.MatchItemView.Stat.NONE;
import static com.fraz.dartlog.statistics.MatchItemView.Stat.TURNS;

public class MatchItemView extends FrameLayout {

    enum Stat {
        NONE,
        TURNS,
        CHECKOUT,
    }

    private TextView dateView;
    private TextView gameType;
    private TextView players;
    private Stat stat1ToShow;
    private Stat stat2ToShow;

    private GameData game;
    private String playerName;
    private Context context;
    private TextView stat1Label;
    private TextView stat1;
    private TextView stat2Label;
    private TextView stat2;
    private TextView result;

    public MatchItemView(Context context) {
        super(context);
        initView(context);
    }

    public MatchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setStatsToShow(Stat stat1, Stat stat2) {
        stat1ToShow = stat1;
        stat2ToShow = stat2;
        invalidate();
        requestLayout();
    }

    public void setGame(GameData game, String playerName) {
        this.game = game;
        this.playerName = playerName;
        initGame();
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.match_history_item, this);
        gameType = (TextView) findViewById(R.id.match_history_game_type);
        players = (TextView) findViewById(R.id.match_history_players);
        stat1Label = (TextView) findViewById(R.id.match_item_stat_1_label);
        stat1 = (TextView) findViewById(R.id.match_item_stat_1);
        stat2Label = (TextView) findViewById(R.id.match_item_stat_2_label);
        stat2 = (TextView) findViewById(R.id.match_item_stat_2);
        result = (TextView) findViewById(R.id.match_item_result);
        dateView = (TextView) findViewById(R.id.match_history_date);

        stat1ToShow = TURNS;
        stat2ToShow = NONE;
    }

    private void initGame() {
        gameType.setText(game.getDetailedGameType().toUpperCase());
        initPlayers();
        initStatView(stat1Label, stat1, stat1ToShow);
        initStatView(stat2Label, stat2, stat2ToShow);
        initResultView();
        initDate();
        invalidate();
        requestLayout();
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

    private void initStatView(TextView labelView, TextView statView, Stat stat) {
        if (stat == NONE) {
            labelView.setVisibility(GONE);
            statView.setVisibility(GONE);
        } else {
            labelView.setVisibility(VISIBLE);
            statView.setVisibility(VISIBLE);

            if(stat == TURNS) {
                labelView.setText(R.string.turns);
                statView.setText(String.format(Locale.getDefault(), "%d", game.getTurns()));
            } else if(stat == CHECKOUT) {
                labelView.setText(R.string.checkout);
                statView.setText(String.format(Locale.getDefault(), "%d", game.getCheckout()));
            }
        }
    }

    private void initResultView() {
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
            dateView.setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).
                    format(date));
    }
}
