package com.fraz.dartlog;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class PlayActivity extends ActionBarActivity implements InputEventListener {

    private X01 game;
    private PlayerListAdapter playerListAdapter;
    private ArrayList<PlayerData> playerDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        playerDataArrayList = new ArrayList<>();
        playerListAdapter = new PlayerListAdapter(this, playerDataArrayList);

        addPlayerNamesToListView();

        linkListViewToPlayerListAdapter();

        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) findViewById(R.id.numpad_view));
        numPadHandler.setListener(this);

        game = new X01(this, playerDataArrayList, 3);
    }


    private void linkListViewToPlayerListAdapter() {
        ListView myListView = (ListView) findViewById(R.id.play_players_listView);
        myListView.setAdapter(playerListAdapter);
    }

    /**
     * Fetch players names that are sent from PlayersActivity and store them in
     * play_players_listView
     */
    private void addPlayerNamesToListView(){
        for(String playerName: getIntent().getStringArrayListExtra("playerNames"))
        {
            playerDataArrayList.add(new PlayerData(playerName));
        }
    }

    @Override
    public void enter(Integer score) {
        game.enterScore(score);
        playerListAdapter.notifyDataSetChanged();
    }
}
