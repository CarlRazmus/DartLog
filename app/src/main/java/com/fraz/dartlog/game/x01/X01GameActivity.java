package com.fraz.dartlog.game.x01;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.MainActivity;
import com.fraz.dartlog.OnBackPressedDialogFragment;
import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.InputEventListener;
import com.fraz.dartlog.game.NumPadHandler;
import com.fraz.dartlog.statistics.MatchPagerActivity;
import com.fraz.dartlog.statistics.MatchRecyclerViewAdapter;

import java.util.ArrayList;

public class X01GameActivity extends Fragment implements View.OnClickListener,
        InputEventListener, OnBackPressedDialogFragment.OnBackPressedDialogListener {

    private X01 game;
    private X01GameListAdapter gameListAdapter;
    private ViewAnimator viewAnimator;
    private DartLogDatabaseHelper dbHelper;
    private TextView roundTextView;
    private int gamesPlayed;
    private boolean currentMatchAdded = false;
    private boolean undoPossible = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar((Toolbar) findViewById(R.id.game_toolbar));
        viewAnimator = (ViewAnimator) findViewById(R.id.game_input);
        dbHelper = DartLogDatabaseHelper.getInstance(this);
        roundTextView = (TextView) findViewById(R.id.game_header_round);

        game = GetX01GameInstance(savedInstanceState);
        gameListAdapter = new X01GameListAdapter(game);

        gamesPlayed = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_x01_game, container, false);

        initListView();
        initNumPadView();
        initGameDoneView();
        updateView();
        return view;
    }

    @Override
    public void onBackPressed() {
        DialogFragment onBackPressedDialogFragment = new OnBackPressedDialogFragment();
        onBackPressedDialogFragment.show(getFragmentManager(), "OnBackPressedDialogFragment");
    }

    @NonNull
    private X01 GetX01GameInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            X01 game = (X01) savedInstanceState.getSerializable("game");
            if (game != null)
                return game;
        }
        return new X01(this, createPlayerDataList());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", game);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_leg:
                if(!currentMatchAdded) {
                    dbHelper.addX01Match(game);
                }
                gamesPlayed += 1;
                game.newLeg();
                setCurrentMatchAdded(false);
                updateView();
                break;
            case R.id.match_summary:
                if(!currentMatchAdded)
                {
                    dbHelper.addX01Match(game);
                    setCurrentMatchAdded(true);
                }
                showSummary();
                break;
            case R.id.complete_match:
                if(!currentMatchAdded)
                    dbHelper.addX01Match(game);
                completeMatch();
                break;
        }
    }

    private void setCurrentMatchAdded(boolean added) {
        currentMatchAdded = added;
        undoPossible = !added;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_undo:
                game.undo();
                updateView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        MenuItem undo_action = menu.findItem(R.id.action_undo);
        if (undoPossible) {
            undo_action.setEnabled(true);
        }
        else
        {
            undo_action.setEnabled(false);
        }

        return true;
    }

    @Override
    public void enter(int score) {
        game.submitScore(score);
        updateView();
    }

    private void updateView() {
        gameListAdapter.notifyDataSetChanged();
        scrollToPlayerInList();
        if (!currentMatchAdded && undoPossible != game.isUndoPossible())
        {
            undoPossible = !undoPossible;
            invalidateOptionsMenu();
        }
        if (game.isGameOver()) {
            setGameDoneView();
        } else {
            setNumPadView();
            updateGameRound();
        }
    }

    private void updateGameRound() {
        ((TextView) findViewById(R.id.game_header_round)).setText(String.valueOf(game.getTurn()));
    }

    private void initListView() {
        RecyclerView myListView = (RecyclerView) findViewById(R.id.play_players_listView);
        assert myListView != null;
        myListView.setAdapter(gameListAdapter);
    }

    /**
     * Create and return a list of player data from a list of player names.
     */
    private ArrayList<X01PlayerData> createPlayerDataList() {
        Intent intent = getIntent();
        ArrayList<String> playerNames = intent.getStringArrayListExtra("playerNames");
        int x = intent.getIntExtra("x", 3);
        int doubleOutAttempts = intent.getIntExtra("double_out", 0);
        ArrayList<X01PlayerData> playerDataList = new ArrayList<>();
        for (String playerName : playerNames) {
            X01ScoreManager scoreManager = new X01ScoreManager(x);
            scoreManager.setDoubleOutAttempts(doubleOutAttempts);
            playerDataList.add(new X01PlayerData(new CheckoutChart(this), playerName, scoreManager));
        }
        return playerDataList;
    }

    private void scrollToPlayerInList() {
        RecyclerView playersListView = (RecyclerView) findViewById(R.id.play_players_listView);
        assert playersListView != null;
        playersListView.smoothScrollToPosition(game.getCurrentPlayerIdx());
    }

    private void initNumPadView() {
        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) findViewById(R.id.num_pad), 180);
        numPadHandler.setListener(this);
    }

    private void initGameDoneView() {
        Button newLegButton = findViewById(R.id.new_leg);
        Button completeMatchButton = findViewById(R.id.complete_match);
        Button matchSummaryButton = findViewById(R.id.match_summary);

        assert newLegButton != null;
        newLegButton.setOnClickListener(this);
        assert completeMatchButton != null;
        completeMatchButton.setOnClickListener(this);
        assert matchSummaryButton != null;
        matchSummaryButton.setOnClickListener(this);
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

    private void showSummary() {
        Intent intent = new Intent(this, MatchPagerActivity.class);
        intent.putExtra(MatchPagerActivity.ARG_ITEM_NAME, game.getPlayers().get(0).getPlayerName());
        intent.putExtra(MatchPagerActivity.ARG_ITEM_POSITION, 0);
        intent.putExtra(MatchPagerActivity.ARG_MATCHES, gamesPlayed);
        intent.putExtra(MatchPagerActivity.ARG_IS_MATCH_SUMMARY, true);
        startActivity(intent);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing.
    }
}
