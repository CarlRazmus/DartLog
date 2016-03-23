package com.fraz.dartlog;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class PlayActivity extends ActionBarActivity implements InputEventListener {

    private X01 game;
    private PlayerListAdapter playerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        ListView myListView = (ListView) findViewById(R.id.play_players_listView);
        ArrayList<PlayerData> playerDataArrayList = new ArrayList<>();

        playerDataArrayList.add(new PlayerData("Razmus Lindgren", 30));
        playerDataArrayList.add(new PlayerData("Filip Källström", 42));

        playerListAdapter = new PlayerListAdapter(this, playerDataArrayList);
        myListView.setAdapter(playerListAdapter);

        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) findViewById(R.id.numpad_view));
        numPadHandler.setListener(this);

        game = new X01(this, playerDataArrayList, 3);
    }

    @Override
    public void enter(Integer score) {
        game.enterScore(score);
        playerListAdapter.notifyDataSetChanged();
    }
}
