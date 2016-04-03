package com.fraz.dartlog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayersActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> playersNames;
    private SelectedPlayersListAdapter arrayStringAdapter;

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
        arrayStringAdapter = new SelectedPlayersListAdapter(this, playersNames);
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
                clearInputTextField();
                break;
        }
    }

    private void clearInputTextField() {
        EditText editText = (EditText) findViewById(R.id.player_input_textfield);
        editText.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void addPlayerNameToPlayerListView()
    {
        EditText editText = (EditText) findViewById(R.id.player_input_textfield);
        String playerName = editText.getText().toString();

        if(!playerName.equals("")) {
            arrayStringAdapter.add(playerName);
        }
        else{
            showEmptyTextErrorToast();
        }
    }

    private void showEmptyTextErrorToast() {
        showToast("must write something!");
    }

    private void showMustAddPlayersErrorToast() {
        showToast("Its kinda hard to play dart without any players!");
    }

    private void showToast(CharSequence text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void startPlayActivity() {
        if(playersNames.size() == 0){
            showMustAddPlayersErrorToast();
        }

        Intent intent = new Intent(this, GameActivity.class);

        intent.putStringArrayListExtra("playerNames", playersNames);
        startActivity(intent);
    }

}
