package com.fraz.dartlog.game.settings;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
    private ArrayList<String> participantNames;
    private AvailablePlayersListAdapter arrayStringAdapterForFechedPlayers;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        Button readyButton = (Button) findViewById(R.id.ready_button);
        assert readyButton != null;
        readyButton.setOnClickListener(this);

        FloatingActionButton openPlayerSelectionFab = (FloatingActionButton)findViewById(R.id.open_player_selection_fab);
        assert openPlayerSelectionFab != null;
        openPlayerSelectionFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openPlayerSelectionView();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.participants_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        participantNames = new ArrayList<>();
        recyclerViewAdapter = new ParticipantsListRecyclerAdapter(participantNames);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ParticipantSwipeCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, participantNames, recyclerViewAdapter));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        participantNames.add("Razmus");
        participantNames.add("Filip");
        participantNames.add("Jonathan");
        participantNames.add("Martin");

        itemTouchHelper.attachToRecyclerView(recyclerView);
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
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.select_players_dialog);
        dialog.setTitle("Select players");
        dialog.setCancelable(true);

        ListView availablePlayersListView = (ListView) dialog.findViewById(R.id.dialog_players_listView);
        assert availablePlayersListView != null;

        ArrayList<String> fetchedPlayersNames = new ArrayList<>();
        populatePlayerList(fetchedPlayersNames);

        arrayStringAdapterForFechedPlayers = new AvailablePlayersListAdapter(this, fetchedPlayersNames);
        availablePlayersListView.setAdapter(arrayStringAdapterForFechedPlayers);

        Button button = (Button) dialog.findViewById(R.id.done_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void populatePlayerList(ArrayList<String> participantNames){
        participantNames.add("Razmus");
        participantNames.add("Filip");
        participantNames.add("Jonathan");
        participantNames.add("Martin");
        participantNames.add("GenericName1");
        participantNames.add("GenericName2");
        participantNames.add("GenericName3");
        participantNames.add("GenericName4");
        participantNames.add("GenericName5");
        participantNames.add("GenericName6");
        participantNames.add("GenericName7");
        participantNames.add("GenericName8");
    }

    private ArrayList<PlayerData> fetchPlayerNamesFromDataBase(){
        return null;
    }

/*
        private void clearInputTextField() {
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
    }

    private void showEmptyTextErrorToast() {
        showToast("must write something!");
    }
*/

    private void showMustAddPlayersErrorToast() {
        showToast("Its kinda hard to play dart without any players!");
    }

    private void showToast(CharSequence text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void startPlayActivity() {
        if(participantNames.size() == 0){
            showMustAddPlayersErrorToast();
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putStringArrayListExtra("playerNames", participantNames);
        startActivity(intent);
    }

}
