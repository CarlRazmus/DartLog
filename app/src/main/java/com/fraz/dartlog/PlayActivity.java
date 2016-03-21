package com.fraz.dartlog;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlayActivity extends ActionBarActivity implements NumPadEventListener{

    private X01 game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) findViewById(R.id.numpad_view));
        numPadHandler.setListener(this);

        String[] players = {"Filip"};
        game = new X01(players, 3);
    }

    @Override
    public void enter(Integer score) {
        Log.i("MyTag", "enter: ");
        game.enterScore(score);
        TextView v = (TextView) findViewById(R.id.player_score);
        v.setText(String.valueOf(game.getScores()[0]));
    }
}
