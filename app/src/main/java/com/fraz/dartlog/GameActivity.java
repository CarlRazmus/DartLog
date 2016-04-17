package com.fraz.dartlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements View.OnClickListener,
                                                               InputEventListener {

    private X01 game;
    private GameListAdapter gameListAdapter;
    private ArrayList<PlayerData> playerDataArrayList;
    private ViewAnimator viewAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setSupportActionBar((Toolbar) findViewById(R.id.game_toolbar));

        viewAnimator = (ViewAnimator) findViewById(R.id.game_input);
        playerDataArrayList = new ArrayList<>();
        gameListAdapter = new GameListAdapter(this, playerDataArrayList);

        addPlayerNamesToListView();
        initListView();

        initNumPadView();
        initGameDoneView();

        game = new X01(this, playerDataArrayList, 3);
        updateView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_leg:
                game.newLeg();
                updateView();
                break;
            case R.id.complete_match:
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
        game.enterScore(score);
        updateView();
    }

    private void updateView() {
        gameListAdapter.notifyDataSetChanged();
        updateHintView();
        scrollToPlayerInList();
        if (game.isDone()) {
            setGameDoneView();
        } else {
            setNumPadView();
        }
    }

    private void updateHintView() {
        TextView hintView = (TextView) findViewById(R.id.game_hint);
        assert hintView != null;
        String hintText = game.getHintText();
        if (hintText != null) {
            hintView.setVisibility(View.VISIBLE);
            hintView.setText(hintText);
        } else {
            hintView.setVisibility(View.GONE);
        }
    }

    private void initListView() {
        ListView myListView = (ListView) findViewById(R.id.play_players_listView);
        assert myListView != null;
        myListView.setAdapter(gameListAdapter);
    }

    /**
     * Fetch players names that are sent from PlayersActivity and store them in
     * play_players_listView
     */
    private void addPlayerNamesToListView(){
        for(String playerName: getIntent().getStringArrayListExtra("playerNames"))
        {
            playerDataArrayList.add(new PlayerData(playerName));
        }
    }

    private void scrollToPlayerInList() {
        ListView playersListView = (ListView) findViewById(R.id.play_players_listView);
        assert playersListView != null;
        playersListView.smoothScrollToPosition(game.getCurrentPlayer());
    }

    private void initNumPadView() {
        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) findViewById(R.id.num_pad_view));
        numPadHandler.setListener(this);
    }

    private void initGameDoneView() {
        Button newLegButton = (Button) findViewById(R.id.new_leg);
        Button completeMatchButton = (Button) findViewById(R.id.complete_match);

        assert newLegButton != null;
        newLegButton.setOnClickListener(this);
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
}
