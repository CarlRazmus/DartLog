package com.fraz.dartlog;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class PlayActivity extends ActionBarActivity implements NumPadEventListener{

    private X01 game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initButtons();
        initInputField();


        ListView myListView = (ListView) findViewById(R.id.play_players_listView);
        ArrayList<PlayerData> playerDataArrayList = new ArrayList<>();

        playerDataArrayList.add(new PlayerData("Razmus Lindgren", 30));
        playerDataArrayList.add(new PlayerData("Filip Källström", 42));

        PlayerListAdapter playerListAdapter = new PlayerListAdapter(this, playerDataArrayList);
        myListView.setAdapter(playerListAdapter);
    }
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
