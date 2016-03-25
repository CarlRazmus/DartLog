package com.fraz.dartlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class PlayersActivity extends ActionBarActivity implements View.OnClickListener {
    private ArrayList<String> playersNames;
    private ArrayAdapter<String> arrayStringAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        Button readyButton = (Button) findViewById(R.id.ready_button);
        readyButton.setOnClickListener(this);

        Button addPlayerButton = (Button) findViewById(R.id.add_player_button);
        addPlayerButton.setOnClickListener(this);

        /* create a listView that contains all players who will join the game */
        ListView myListView = (ListView) findViewById(R.id.players_listView);
        playersNames = new ArrayList<>();
        /* add names for debug purposes, remove when app is finished */
        playersNames.add("Razmus Lindgren");
        playersNames.add("Filip Källström");
        arrayStringAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playersNames);
        myListView.setAdapter(arrayStringAdapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ready_button:
                startPlayActivity();
                break;
            case R.id.add_player_button:
                addPlayerNameToPlayerListView();
                break;
        }
    }

    private void addPlayerNameToPlayerListView()
    {
        EditText editText = (EditText) findViewById(R.id.player_input_textfield);
        String playerName = editText.getText().toString();
        Log.d("PlayerName", playerName);

        arrayStringAdapter.add(playerName);
    }

    private void startPlayActivity() {
        Intent intent = new Intent(this, PlayActivity.class);

        intent.putStringArrayListExtra("playerNames", playersNames);
        startActivity(intent);
    }
}
