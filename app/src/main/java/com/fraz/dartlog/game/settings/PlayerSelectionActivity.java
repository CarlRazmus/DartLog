package com.fraz.dartlog.game.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDbHelper;
import com.fraz.dartlog.game.GameActivity;
import com.fraz.dartlog.game.PlayerData;

import java.util.ArrayList;

public class PlayerSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> participantNames;
    private AvailablePlayersListAdapter availablePlayersListAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DartLogDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        dbHelper = new DartLogDbHelper(this);

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

        participantNames = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new ParticipantsListRecyclerAdapter(participantNames);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ParticipantSwipeCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, participantNames, recyclerViewAdapter));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

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

    private void initializeDialog(Dialog dialog){
        dialog.setContentView(R.layout.select_players_dialog);
        dialog.setTitle("Select players");
        dialog.setCancelable(true);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("Dialog", "adding stuff");
                participantNames.clear();
                participantNames.addAll(availablePlayersListAdapter.getSelectedPlayers());
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void openPlayerSelectionView(){
        final Dialog dialog = new Dialog(this);

        initializeDialog(dialog);

        final ListView availablePlayersListView = (ListView) dialog.findViewById(R.id.dialog_players_listView);
        assert availablePlayersListView != null;

        ArrayList<String> playerNames = new ArrayList<>();
        //populatePlayerList(playerNames);
        fetchPlayerNamesFromDataBase(playerNames);

        availablePlayersListAdapter = new AvailablePlayersListAdapter(this, playerNames);
        availablePlayersListView.setAdapter(availablePlayersListAdapter);

        availablePlayersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                availablePlayersListAdapter.toggleSelected(position);

                if(availablePlayersListAdapter.isMarked(position))
                    view.setBackgroundResource(R.color.game_player_winner);
                else
                    view.setBackgroundResource(R.color.background_dark_transparent);
            }
        });

        Button button = (Button) dialog.findViewById(R.id.done_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void fetchPlayerNamesFromDataBase(ArrayList<String> list){
        list.addAll(dbHelper.getPlayers());
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
        if(participantNames.size() == 0){
            showMustAddPlayersErrorToast();
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putStringArrayListExtra("playerNames", participantNames);
        startActivity(intent);
    }
}
