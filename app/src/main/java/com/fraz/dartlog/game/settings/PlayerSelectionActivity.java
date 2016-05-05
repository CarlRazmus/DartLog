package com.fraz.dartlog.game.settings;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameActivity;
import com.fraz.dartlog.game.PlayerData;

import java.util.ArrayList;

public class PlayerSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> playersNames;
    private ParticipantsListAdapter arrayStringAdapter;
    private ParticipantsListAdapter arrayStringAdapterForFechedPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        Button readyButton = (Button) findViewById(R.id.ready_button);
        assert readyButton != null;
        readyButton.setOnClickListener(this);

        /*Button addPlayerButton = (Button) findViewById(R.id.add_player_button);
        assert addPlayerButton != null;
        addPlayerButton.setOnClickListener(this);
*/

        FloatingActionButton openPlayerSelectionFab = (FloatingActionButton)findViewById(R.id.open_player_selection_fab);
        assert openPlayerSelectionFab != null;
        openPlayerSelectionFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openPlayerSelectionView();
            }
        });

        /* create a listView that contains all players who will join the game */
        ListView myListView = (ListView) findViewById(R.id.players_listView);

        playersNames = new ArrayList<>();
        /* add names for debug purposes, remove when app is finished */
        playersNames.add("Razmus");
        playersNames.add("Filip");
        playersNames.add("Jonathan");
        playersNames.add("Martin");
        arrayStringAdapter = new ParticipantsListAdapter(this, playersNames);
        assert myListView != null;
        myListView.setAdapter(arrayStringAdapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ready_button:
                startPlayActivity();
                break;
        }
    }

    private void openPlayerSelectionView(){
        //set up dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.select_players_dialog);
        dialog.setTitle("Choose players");
        dialog.setCancelable(true);

        /* create a listView that contains all players who can join the game */
        ListView myListView = (ListView) dialog.findViewById(R.id.dialog_players_listView);
        assert myListView != null;

        ArrayList<String> fetchedPlayersNames = new ArrayList<>();
        populatePlayerList(fetchedPlayersNames);

        arrayStringAdapterForFechedPlayers = new ParticipantsListAdapter(this, fetchedPlayersNames);

        myListView.setAdapter(arrayStringAdapterForFechedPlayers);

        //set up button
        Button button = (Button) dialog.findViewById(R.id.done_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        dialog.show();
    }

    private void populatePlayerList(ArrayList<String> playersNames){
        //ArrayList<PlayerData> players = fetchPlayerNamesFromDataBase();

        /* add names for debug purposes, remove when app is finished */
        playersNames.add("Razmus");
        playersNames.add("Filip");
        playersNames.add("Jonathan");
        playersNames.add("Martin");
        playersNames.add("GenericName1");
        playersNames.add("GenericName2");
        playersNames.add("GenericName3");
        playersNames.add("GenericName4");
        playersNames.add("GenericName5");
        playersNames.add("GenericName6");
        playersNames.add("GenericName7");
        playersNames.add("GenericName8");
    }

    private ArrayList<PlayerData> fetchPlayerNamesFromDataBase(){
        return null;
    }
   /* private void clearInputTextField() {
        EditText editText = (EditText) findViewById(R.id.player_input_textfield);
        assert editText != null;
        editText.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void addPlayerNameToPlayerListView()
    {
        EditText editText = (EditText) findViewById(R.id.player_input_textfield);
        assert editText != null;
        String playerName = editText.getText().toString();

        if(!playerName.equals("")) {
            arrayStringAdapter.add(playerName);
        }
        else{
            showEmptyTextErrorToast();
        }
    }*/

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
