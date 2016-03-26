package com.fraz.dartlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewAnimator;

import java.util.ArrayList;

public class PlayActivity extends ActionBarActivity implements View.OnClickListener,
                                                               InputEventListener {

    private X01 game;
    private PlayerListAdapter playerListAdapter;
    private ArrayList<PlayerData> playerDataArrayList;
    private ViewAnimator viewAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        viewAnimator = (ViewAnimator) findViewById(R.id.game_input);
        playerDataArrayList = new ArrayList<>();
        playerListAdapter = new PlayerListAdapter(this, playerDataArrayList);
        addPlayerNamesToListView();
        linkListViewToPlayerListAdapter();

        initNumPadView();
        initGameDoneView();

        game = new X01(this, playerDataArrayList, 3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_leg:
                game.newLeg();
                update();
                break;
            case R.id.complete_match:
                completeMatch();
                break;
        }
    }

    @Override
    public void enter(Integer score) {
        game.enterScore(score);
        update();
    }

    private void update() {
        playerListAdapter.notifyDataSetChanged();
        updateSelectedItemInPlayersListView();
        if (game.isDone()) {
            setGameDoneView();
        } else {
            setNumPadView();
        }
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

    private void updateSelectedItemInPlayersListView() {
        ListView playersListView = (ListView) findViewById(R.id.play_players_listView);
        playersListView.smoothScrollToPosition(game.getCurrentPlayer());
    }

    private void initNumPadView() {
        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) findViewById(R.id.num_pad_view));
        numPadHandler.setListener(this);
    }

    private void initGameDoneView() {
        Button newLegButton = (Button) findViewById(R.id.new_leg);
        Button completeMatchButton = (Button) findViewById(R.id.complete_match);

        newLegButton.setOnClickListener(this);
        completeMatchButton.setOnClickListener(this);
    }

    private void setGameDoneView() {
        viewAnimator.setDisplayedChild(1);
    }

    private void setNumPadView() {
        viewAnimator.setDisplayedChild(0);
    }

    private void completeMatch() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
