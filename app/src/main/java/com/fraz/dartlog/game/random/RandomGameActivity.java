package com.fraz.dartlog.game.random;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.fraz.dartlog.MainActivity;
import com.fraz.dartlog.OnBackPressedDialogFragment;
import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.AdditionScoreManager;
import com.fraz.dartlog.game.InputEventListener;
import com.fraz.dartlog.game.NumPadHandler;
import com.fraz.dartlog.game.PlayerData;
import java.util.ArrayList;

public class RandomGameActivity extends AppCompatActivity implements View.OnClickListener,
        InputEventListener, OnBackPressedDialogFragment.OnBackPressedDialogListener {

    private Random game;
    private RandomGameListAdapter gameListAdapter;
    private ViewAnimator viewAnimator;
    private DartLogDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_game);

        setSupportActionBar((Toolbar) findViewById(R.id.game_toolbar));
        viewAnimator = (ViewAnimator) findViewById(R.id.game_input);
        dbHelper = new DartLogDatabaseHelper(this);

        game = GetRandomGameInstance(savedInstanceState);
        gameListAdapter = new RandomGameListAdapter(this, game);

        initListView();
        initNumPadView();
        initGameDoneView();
        updateView();
        updateCurrentFieldTextView(game.getCurrentField());
        updateNextFieldTextView(game.getNextField());
    }

    @Override
    public void onBackPressed() {
        DialogFragment onBackPressedDialogFragment = new OnBackPressedDialogFragment();
        onBackPressedDialogFragment.show(getFragmentManager(), "OnBackPressedDialogFragment");
    }

    @NonNull
    private Random GetRandomGameInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Random game = (Random) savedInstanceState.getSerializable("randomGame");
            if (game != null)
                return game;
        }
        int nrOfTurns = getIntent().getIntExtra("turns", 10);
        return new Random(this, createPlayerDataList(), nrOfTurns);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("randomGame", game);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_leg:
                dbHelper.addRandomMatch(game);
                game.newLeg();
                updateView();
                break;
            case R.id.complete_match:
                dbHelper.addRandomMatch(game);
                completeMatch();
                break;
        }
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
        if (game.isGameOver()) {
            setGameDoneView();
        } else {
            setNumPadView();
            updateCurrentFieldTextView(game.getCurrentField());
            updateNextFieldTextView(game.getNextField());
            updateGameRound();
        }
    }

    public void updateNextFieldTextView(int nextFieldNr){
        TextView myListView = (TextView) findViewById(R.id.next_field_textview);
        assert myListView != null;

        if(nextFieldNr != 0)
            myListView.setText(String.valueOf(nextFieldNr));
        else
            myListView.setText("");
    }

    public void updateCurrentFieldTextView(int newFieldNr) {
        TextView myListView = (TextView) findViewById(R.id.current_field_textview);
        assert myListView != null;
        myListView.setText(String.valueOf(newFieldNr));
    }

    private void initListView() {
        RecyclerView myListView = (RecyclerView) findViewById(R.id.play_players_listView);
        assert myListView != null;
        myListView.setAdapter(gameListAdapter);
    }

    private void updateGameRound() {
        ((TextView) findViewById(R.id.game_header_turn)).setText(String.valueOf(game.getTurn()));
    }

    /**
     * Create and return a list of player data from a list of player names.
     */
    private ArrayList<PlayerData> createPlayerDataList() {
        Intent intent = getIntent();
        ArrayList<String> playerNames = intent.getStringArrayListExtra("playerNames");
        ArrayList<PlayerData> playerDataList = new ArrayList<>();

        for (String playerName : playerNames) {
            playerDataList.add(new PlayerData(playerName, new AdditionScoreManager()));
        }
        return playerDataList;
    }

    private void scrollToPlayerInList() {
        RecyclerView playersListView = (RecyclerView) findViewById(R.id.play_players_listView);
        assert playersListView != null;
        playersListView.smoothScrollToPosition(game.getCurrentPlayerIdx());
    }

    private void initNumPadView() {
        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) findViewById(R.id.num_pad), 9);
        numPadHandler.setListener(this);
    }

    private void initGameDoneView() {
        Button newLegButton = (Button) findViewById(R.id.new_leg);
        assert newLegButton != null;
        newLegButton.setOnClickListener(this);

        Button completeMatchButton = (Button) findViewById(R.id.complete_match);
        assert completeMatchButton != null;
        completeMatchButton.setOnClickListener(this);
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing.
    }
}
