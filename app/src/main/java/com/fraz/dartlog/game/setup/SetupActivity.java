package com.fraz.dartlog.game.setup;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.fraz.dartlog.MenuBackground;
import com.fraz.dartlog.R;
import com.fraz.dartlog.Util;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.random.RandomGameActivity;
import com.fraz.dartlog.game.random.RandomSettingsFragment;
import com.fraz.dartlog.game.x01.X01GameActivity;
import com.fraz.dartlog.game.x01.X01SettingsFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class SetupActivity extends MenuBackground
        implements ParticipantsListRecyclerAdapter.OnDragStartListener, View.OnClickListener  {

    private AvailablePlayersRecyclerAdapter availablePlayersListAdapter;
    private ParticipantsListRecyclerAdapter participantsRecyclerAdapter;
    private ArrayList<String> participantNames;
    private DartLogDatabaseHelper dbHelper;
    private Dialog selectPlayerDialog;
    private ItemTouchHelper itemTouchHelper;
    private ViewAnimator viewAnimator;
    private String gameType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this, R.layout.activity_setup);

        RecyclerView.LayoutManager participantsLayoutManager = new LinearLayoutManager(this);
        dbHelper = new DartLogDatabaseHelper(this);
        participantNames = new ArrayList<>();
        participantsRecyclerAdapter = new ParticipantsListRecyclerAdapter(this, participantNames);

        gameType = getIntent().getStringExtra("gameType");

        InitializeToolbar();
        InitializeButton();
        InitializeFAB();
        InitializeRules();

        RecyclerView participantsRecyclerView =
                (RecyclerView) findViewById(R.id.participants_recycler_view);
        assert participantsRecyclerView != null;
        participantsRecyclerView.setLayoutManager(participantsLayoutManager);
        participantsRecyclerView.setAdapter(participantsRecyclerAdapter);

        itemTouchHelper = new ItemTouchHelper(new ParticipantSwipeCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, participantNames, participantsRecyclerAdapter));
        itemTouchHelper.attachToRecyclerView(participantsRecyclerView);

        initializeSelectPlayersDialog();
        populateAvailablePlayers();
    }

    private void InitializeRules() {
        switch (gameType) {
            case ("x01"):
                getFragmentManager().beginTransaction()
                        .replace(R.id.game_preferences, new X01SettingsFragment())
                        .commit();
                break;
            case ("random"):
                getFragmentManager().beginTransaction()
                        .replace(R.id.game_preferences, new RandomSettingsFragment())
                        .commit();
                break;
            default:
                break;
        }
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
        final RecyclerView.LayoutManager availablePlayersLayoutManager = new LinearLayoutManager(this);
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

        selectPlayerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                availablePlayersListAdapter.setSelectedPlayers(participantNames);
            }
        });


        final RecyclerView availablePlayersRecyclerView = (RecyclerView) selectPlayerDialog.findViewById(R.id.setup_dialog_available_players);
        assert availablePlayersRecyclerView != null;

        availablePlayersListAdapter = new AvailablePlayersRecyclerAdapter();
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

    private void populateAvailablePlayers(){
        ArrayList<String> playerNames = Util.loadProfileNames(this);
        availablePlayersListAdapter.setAvailablePlayers(playerNames);
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
            startActivity(createGameIntent());
        }
    }

    @NonNull
    private Intent createGameIntent() {
        Intent gameIntent;
        switch (gameType) {
            case "random":
                gameIntent = createRandomGameIntent();
                break;
            default:
            case "x01":
                gameIntent = createX01GameIntent();
                break;
        }
        gameIntent.putStringArrayListExtra("playerNames", participantNames);
        return gameIntent;
    }

    private Intent createRandomGameIntent() {
        Intent intent = new Intent(this, RandomGameActivity.class);
        intent.putExtra("turns", getSelectedNrTurns());
        return intent;
    }

    private Intent createX01GameIntent() {
        Intent intent = new Intent(this, X01GameActivity.class);
        intent.putExtra("x", getSelectedX());
        if (isDoubleOutEnabled())
            intent.putExtra("double_out", getDoubleOutAttemptsSetting());
        return intent;
    }

    private int getSelectedX() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return Integer.parseInt(sharedPref.getString(
                getResources().getString(R.string.pref_key_x01_starting_score), "3"));
    }

    private int getSelectedNrTurns() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return Integer.parseInt(sharedPref.getString(
                getResources().getString(R.string.pref_key_random_nr_of_rounds), "10"));
    }
    private boolean isDoubleOutEnabled() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPref.getBoolean(
                getResources().getString(R.string.pref_key_x01_double_out), false);
    }

    private int getDoubleOutAttemptsSetting() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return Integer.parseInt(sharedPref.getString(
                getResources().getString(R.string.pref_key_x01_double_out_attempts), "5"));
    }

    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
