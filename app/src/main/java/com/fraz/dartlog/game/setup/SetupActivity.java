package com.fraz.dartlog.game.setup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fraz.dartlog.MenuBackground;
import com.fraz.dartlog.PlayerSelectorDialogFragment;
import com.fraz.dartlog.R;
import com.fraz.dartlog.Util;
import com.fraz.dartlog.game.random.RandomGameActivity;
import com.fraz.dartlog.game.random.RandomSettingsFragment;
import com.fraz.dartlog.game.x01.X01GameActivity;
import com.fraz.dartlog.game.x01.X01SettingsFragment;
import com.fraz.dartlog.statistics.ProfileListActivity;

import java.util.ArrayList;

public class SetupActivity extends MenuBackground
        implements ParticipantsListRecyclerAdapter.OnDragStartListener, View.OnClickListener,
                    PlayerSelectorDialogFragment.PlayerSelectorDialogListener {

    private ParticipantsListRecyclerAdapter participantsRecyclerAdapter;
    private ArrayList<String> participantNames;
    private ItemTouchHelper itemTouchHelper;
    private String gameType;
    private String rulesString;
    private String rulesTitle;
    private PlayerSelectorDialogFragment dialogFragment;


    public SetupActivity(){
        super(R.layout.activity_setup);
        setParentActivity(this);
    }

    @Override
    public void onDialogPositiveClick(PlayerSelectorDialogFragment dialog) {
        participantNames.clear();
        participantNames.addAll(dialog.getSelectedPlayers());
        participantsRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView.LayoutManager participantsLayoutManager = new LinearLayoutManager(this);
        dialogFragment = new PlayerSelectorDialogFragment();
        participantNames = new ArrayList<>();
        participantsRecyclerAdapter = new ParticipantsListRecyclerAdapter(this, participantNames);
        gameType = getIntent().getStringExtra("gameType");

        InitializeToolbar();
        InitializeButton();
        InitializeFAB();
        InitializeRules();

        RecyclerView participantsRecyclerView = findViewById(R.id.participants_recycler_view);
        assert participantsRecyclerView != null;
        participantsRecyclerView.setLayoutManager(participantsLayoutManager);
        participantsRecyclerView.setAdapter(participantsRecyclerAdapter);

        itemTouchHelper = new ItemTouchHelper(new ParticipantSwipeCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, participantNames, participantsRecyclerAdapter));
        itemTouchHelper.attachToRecyclerView(participantsRecyclerView);
    }

    private void InitializeRules() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        switch (gameType) {
            case ("x01"):
                ab.setTitle("X01 setup");
                getFragmentManager().beginTransaction()
                        .replace(R.id.game_preferences, new X01SettingsFragment())
                        .commit();
                rulesString = getString(R.string.x01_rules);
                rulesTitle = "X01";
                break;
            case ("random"):
                ab.setTitle("Random setup");
                getFragmentManager().beginTransaction()
                        .replace(R.id.game_preferences, new RandomSettingsFragment())
                        .commit();
                rulesString = getString(R.string.random_rules);
                rulesTitle = "Random";
                break;
            default:
                break;
        }
    }

    private void InitializeFAB() {
        FloatingActionButton openPlayerSelectionFab = findViewById(R.id.open_player_selection_fab);
        openPlayerSelectionFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putStringArrayList("selectedNames", participantNames);
                dialogFragment.setArguments(args);
                dialogFragment.show(getSupportFragmentManager(), "selectPlayers");
            }
        });
    }

    private void InitializeButton() {
        Button readyButton = findViewById(R.id.start_game_button);
        assert readyButton != null;
        readyButton.setOnClickListener(this);
    }

    private void InitializeToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_game_button:
                startPlayActivity();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, ProfileListActivity.class));
            return true;
        }

        if(id ==  R.id.action_rules)
            showRulesDialog(rulesTitle, rulesString);
        return super.onOptionsItemSelected(item);
    }

    private void showRulesDialog(String title, String rules) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title + " rules")
                .setMessage(rules)
                .setIcon(R.drawable.ic_info_outline_white_18dp)
                .setPositiveButton(android.R.string.yes, null);
        AlertDialog dialog = builder.show();

        dialog.getButton(Dialog.BUTTON_NEGATIVE).setVisibility(View.INVISIBLE);
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setActivated(false);
    }

    private void startPlayActivity() {
        if (participantNames.size() == 0) {
            Util.showToast("A game requires 1 or more players to start!");
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
            case "x01":
            default:
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

    public String getGameType(){
        return gameType;
    }
}
