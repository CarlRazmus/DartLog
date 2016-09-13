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
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.x01.X01GameActivity;

import java.util.ArrayList;

public class SetupActivity extends AppCompatActivity
        implements ParticipantsListRecyclerAdapter.OnDragStartListener, View.OnClickListener  {

    private AvailablePlayersRecyclerAdapter availablePlayersListAdapter;
    private ParticipantsListRecyclerAdapter participantsRecyclerAdapter;
    private ArrayList<String> participantNames;
    private DartLogDatabaseHelper dbHelper;
    private Dialog selectPlayerDialog;
    private ItemTouchHelper itemTouchHelper;
    private ViewAnimator viewAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup);

        RecyclerView.LayoutManager participantsLayoutManager = new LinearLayoutManager(this);
        dbHelper = new DartLogDatabaseHelper(this);
        participantNames = new ArrayList<>();
        participantsRecyclerAdapter = new ParticipantsListRecyclerAdapter(this, participantNames);

        InitializeToolbar();
        InitializeButton();
        InitializeFAB();
        InitializeRules();
        initializeSettingsView();

        RecyclerView participantsRecyclerView =
                (RecyclerView) findViewById(R.id.participants_recycler_view);
        assert participantsRecyclerView != null;
        participantsRecyclerView.setHasFixedSize(true);
        participantsRecyclerView.setLayoutManager(participantsLayoutManager);
        participantsRecyclerView.setAdapter(participantsRecyclerAdapter);

        itemTouchHelper = new ItemTouchHelper(new ParticipantSwipeCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, participantNames, participantsRecyclerAdapter));
        itemTouchHelper.attachToRecyclerView(participantsRecyclerView);

        initializeSelectPlayersDialog();
    }

    private void initializeSettingsView(){
        String gameType = getIntent().getStringExtra("gameType");
        viewAnimator = (ViewAnimator) findViewById(R.id.game_setup);

        switch (gameType) {
            case ("x01"):
                viewAnimator.setDisplayedChild(0);
                break;
            case ("random"):
                viewAnimator.setDisplayedChild(1);
                break;
        }

    }

    private void InitializeRules() {
        Spinner scoreSpinner = (Spinner) findViewById(R.id.score_spinner);
        assert scoreSpinner != null;
        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.x01_entries));
        scoreSpinner.setAdapter(spinnerAdapter);
    }

    private void InitializeFAB() {
        FloatingActionButton openPlayerSelectionFab = (FloatingActionButton) findViewById(R.id.open_player_selection_fab);
        assert openPlayerSelectionFab != null;
        openPlayerSelectionFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openPlayerSelectionDialog();
            }
        });
    }

    private void InitializeButton() {
        Button readyButton = (Button) findViewById(R.id.start_game_button);
        assert readyButton != null;
        readyButton.setOnClickListener(this);
    }

    private void InitializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
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
        RecyclerView.LayoutManager availablePlayersLayoutManager = new LinearLayoutManager(this);
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
        availablePlayersListAdapter = new AvailablePlayersRecyclerAdapter(playerNames);
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
            Intent intent = new Intent(this, X01GameActivity.class);
            intent.putStringArrayListExtra("playerNames", participantNames);
            intent.putExtra("x", getSelectedX());
            startActivity(intent);
        }
    }

    private int getSelectedX() {
        Spinner scoreSpinner = (Spinner) findViewById(R.id.score_spinner);
        return Character.getNumericValue(((String) scoreSpinner.getSelectedItem()).charAt(0));
    }

    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
