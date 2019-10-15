package com.fraz.dartlog.game.x01;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.MainActivity;
import com.fraz.dartlog.OnBackPressedDialogFragment;
import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.InputEventListener;
import com.fraz.dartlog.game.NumPadHandler;
import com.fraz.dartlog.game.PlayerData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class X01GameActivity extends AppCompatActivity implements View.OnClickListener,
        InputEventListener, OnBackPressedDialogFragment.OnBackPressedDialogListener {

    private X01 game;
    private X01GameListAdapter gameListAdapter;
    private ViewAnimator viewAnimator;
    private DartLogDatabaseHelper dbHelper;
    private TextView roundTextView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x01_game);

        setSupportActionBar((Toolbar) findViewById(R.id.game_toolbar));
        viewAnimator = findViewById(R.id.game_input);
        dbHelper = DartLogDatabaseHelper.getInstance(this);
        roundTextView = findViewById(R.id.game_header_round);

        game = GetX01GameInstance(savedInstanceState);
        gameListAdapter = new X01GameListAdapter(game);
        requestQueue = Volley.newRequestQueue(this);

        initListView();
        initNumPadView();
        initGameDoneView();
        updateView();
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
                dbHelper.addX01Match(game);
                game.newLeg();
                updateView();
                break;
            case R.id.complete_match:
                dbHelper.addX01Match(game);
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

    private void sendGameDataToServer()
    {
        final String url = "http://192.168.28.139:5000/matches";
        final String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //json={"device_id" : 1,  "match" : {"player1" : {"name" : "pelle", "matchdata" : [301, 281, 200]}, "player2" : {"name" : "hakon", "matchdata" : [301, 181, 0]}}})
        String winner = "None";
        if (game.isGameOver())
            winner = game.getWinner().getPlayerName();

        PlayerData player1 = game.getPlayer(0);
        PlayerData player2 = game.getPlayer(1);
        Map player1data = new HashMap();
        Map player2data = new HashMap();
        player1data.put("name", player1.getPlayerName());
        player2data.put("name", player2.getPlayerName());
        LinkedList player1list = (LinkedList)player1.getTotalScoreHistory().clone();
        LinkedList player2list = (LinkedList)player2.getTotalScoreHistory().clone();

        Log.d("dartserver", "p1 list " + player1list.toString());
        Log.d("dartserver", "p2 list " + player2list.toString());

        //if (player1list.size() > 1)
        player1list.add(player1.getScore());
        //if (player2list.size() > 1)
        player2list.add(player2.getScore());
        player1data.put("matchdata", player1list);
        player2data.put("matchdata", player2list);

        Map match = new HashMap();
        match.put("winner", winner);
        match.put("player1", player1data);
        match.put("player2", player2data);

        Log.d("dartserver", android_id);
        Map data = new HashMap();
        data.put("device_id", android_id);
        data.put("match", match);

        Log.d("hej", data.toString());
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("dartserver", "Got a response!");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("dartserver", "Got an error :(\n" + error.getMessage() );
                    }
                }
        );

        requestQueue.add(jsonobj);
    }
    private void updateView() {
        gameListAdapter.notifyDataSetChanged();

        sendGameDataToServer();

        scrollToPlayerInList();
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
        RecyclerView myListView = findViewById(R.id.play_players_listView);
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
        RecyclerView playersListView = findViewById(R.id.play_players_listView);
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing.
    }
}
