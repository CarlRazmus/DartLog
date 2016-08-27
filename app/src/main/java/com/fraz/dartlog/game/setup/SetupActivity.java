package com.fraz.dartlog.game.setup;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameActivity;

import java.util.ArrayList;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> participantNames;
    private AvailablePlayersRecyclerAdapter availablePlayersListAdapter;
    private ParticipantsListRecyclerAdapter participantsRecyclerAdapter;
    private RecyclerView participantsRecyclerView;
    private RecyclerView.LayoutManager participantsLayoutManager;
    private RecyclerView.LayoutManager availablePlayersLayoutManager;
    private DartLogDatabaseHelper dbHelper;
    private Dialog selectPlayerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        dbHelper = new DartLogDatabaseHelper(this);

        Button readyButton = (Button) findViewById(R.id.start_game_button);
        assert readyButton != null;
        readyButton.setOnClickListener(this);

        FloatingActionButton openPlayerSelectionFab = (FloatingActionButton) findViewById(R.id.open_player_selection_fab);
        assert openPlayerSelectionFab != null;
        openPlayerSelectionFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openPlayerSelectionDialog();
            }
        });

        participantsRecyclerView = (RecyclerView) findViewById(R.id.participants_recycler_view);

        participantsLayoutManager = new LinearLayoutManager(this);
        availablePlayersLayoutManager = new LinearLayoutManager(this);
        participantNames = new ArrayList<>();
        participantsRecyclerAdapter = new ParticipantsListRecyclerAdapter(participantNames);

        participantsRecyclerView.setHasFixedSize(true);
        participantsRecyclerView.setLayoutManager(participantsLayoutManager);
        participantsRecyclerView.setAdapter(participantsRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ParticipantSwipeCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, participantNames, participantsRecyclerAdapter));
        itemTouchHelper.attachToRecyclerView(participantsRecyclerView);

        initializeSelectPlayersDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_game_button:
                startPlayActivity();
                break;
        }
    }


    private void initializeSelectPlayersDialog(){
        selectPlayerDialog = new Dialog(this);
        selectPlayerDialog.setTitle("Select players");
        selectPlayerDialog.setContentView(R.layout.setup_players_dialog);
        selectPlayerDialog.setCancelable(true);

        selectPlayerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                participantNames.clear();
                participantNames.addAll(availablePlayersListAdapter.getSelectedPlayers());
                participantsRecyclerAdapter.notifyDataSetChanged();
            }
        });

        final RecyclerView availablePlayersRecyclerView = (RecyclerView) selectPlayerDialog.findViewById(R.id.setup_dialog_available_players);
        assert availablePlayersRecyclerView != null;

        ArrayList<String> playerNames = fetchPlayerNamesFromDataBase();
        availablePlayersListAdapter = new AvailablePlayersRecyclerAdapter(this, playerNames);
        availablePlayersRecyclerView.setAdapter(availablePlayersListAdapter);
        availablePlayersRecyclerView.setLayoutManager(availablePlayersLayoutManager);

        Button button = (Button) selectPlayerDialog.findViewById(R.id.done_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlayerDialog.dismiss();
            }
        });
    }

    private void openPlayerSelectionDialog() {


        /*availablePlayersRecyclerView.setOnClickListener((new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                availablePlayersListAdapter.toggleSelected(position);

                if (availablePlayersListAdapter.isMarked(position))
                    view.setBackgroundResource(R.color.game_player_winner);
                else
                    view.setBackgroundResource(R.color.background_dark_transparent);
            }
        }));
*/

        selectPlayerDialog.show();
    }

    private ArrayList<String> fetchPlayerNamesFromDataBase() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(dbHelper.getPlayers());
        return list;
    }

    private void showMustAddPlayersErrorToast() {
        showToast("A game requires 1 or more players to start!");
    }

    private void showToast(CharSequence text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void startPlayActivity() {
        if (participantNames.size() == 0) {
            showMustAddPlayersErrorToast();
        } else {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putStringArrayListExtra("playerNames", participantNames);
            startActivity(intent);
        }
    }
}
