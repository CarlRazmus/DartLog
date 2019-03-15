package com.fraz.dartlog.game.x01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
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
import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameActivity;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.GameStatisticsFragment;
import com.fraz.dartlog.game.InputEventListener;
import com.fraz.dartlog.game.NumPadHandler;

import java.util.ArrayList;

public class X01GameActivity extends Fragment implements View.OnClickListener,
        InputEventListener {

    private X01 game;
    private X01GameListAdapter gameListAdapter;
    private ViewAnimator viewAnimator;
    private DartLogDatabaseHelper dbHelper;
    private boolean currentMatchAdded = false;
    private boolean undoPossible = false;

    public static X01GameActivity newInstance(int x, boolean doubleOutEnabled, int doubleOutAttempts, ArrayList<String> participantNames) {
        X01GameActivity x01GameFragment = new X01GameActivity();
        Bundle args = new Bundle();
        args.putInt("x", x);
        if (doubleOutEnabled) {
            args.putInt("double_out", doubleOutAttempts);
        }
        args.putStringArrayList("playerNames", participantNames);
        x01GameFragment.setArguments(args);
        return x01GameFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            undoPossible = savedInstanceState.getBoolean("undoPossible");
            currentMatchAdded = savedInstanceState.getBoolean("currentMatchAdded");
        }
        dbHelper = DartLogDatabaseHelper.getInstance(getActivity());
        game = GetX01GameInstance(savedInstanceState);
        gameListAdapter = new X01GameListAdapter(game);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_x01_game, container, false);
        viewAnimator = view.findViewById(R.id.game_input);

        initListView(view);
        initNumPadView(view);
        initGameDoneView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateView();
    }

    @NonNull
    private X01 GetX01GameInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            X01 game = (X01) savedInstanceState.getSerializable("game");
            if (game != null)
                return game;
        }
        return new X01(getActivity(), createPlayerDataList());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", game);
        outState.putBoolean("currentMatchAdded", currentMatchAdded);
        outState.putBoolean("undoPossible", undoPossible);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_leg:
                addCurrentLeg(game.getPlayers().get(0).getPlayerName());
                game.newLeg();
                setCurrentMatchAdded(false);
                updateView();
                break;
            case R.id.match_summary:
                addCurrentLeg(game.getPlayers().get(0).getPlayerName());
                showStatistics();
                break;
            case R.id.complete_match:
                addCurrentLeg(game.getPlayers().get(0).getPlayerName());
                completeMatch();
                break;
        }
    }

    private void setCurrentMatchAdded(boolean added) {
        currentMatchAdded = added;
        undoPossible = !added;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.game_menu, menu);
        MenuItem undo_action = menu.findItem(R.id.action_undo);
        MenuItem stats_action = menu.findItem(R.id.action_stats);
        if (undoPossible) {
            undo_action.setEnabled(true);
        }
        else
        {
            undo_action.setEnabled(false);
        }

        if (((GameActivity) getActivity()).hasStatistics() || game.isGameOver())
        {
            stats_action.setEnabled(true);
        }
        else
        {
            stats_action.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_undo:
                game.undo();
                updateView();
                return true;
            case R.id.action_stats:
                if (game.isGameOver())
                    addCurrentLeg(game.getPlayers().get(0).getPlayerName());
                showStatistics();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void showStatistics() {
        ((GameActivity) getActivity()).showStatistics();
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
        }
        if (game.isGameOver()) {
            setGameDoneView();
        } else {
            setNumPadView();
            updateGameRound();
        }
        getActivity().invalidateOptionsMenu();
    }

    private void updateGameRound() {
        ((TextView) getView().findViewById(R.id.game_header_round)).setText(String.valueOf(game.getTurn()));
    }

    private void initListView(View container) {
        RecyclerView myListView = container.findViewById(R.id.play_players_listView);
        assert myListView != null;
        myListView.setAdapter(gameListAdapter);
    }

    /**
     * Create and return a list of player data from a list of player names.
     */
    private ArrayList<X01PlayerData> createPlayerDataList() {
        Bundle args = getArguments();
        ArrayList<String> playerNames = args.getStringArrayList("playerNames");
        int x = args.getInt("x", 3);
        int doubleOutAttempts = args.getInt("double_out", 0);
        ArrayList<X01PlayerData> playerDataList = new ArrayList<>();
        for (String playerName : playerNames) {
            X01ScoreManager scoreManager = new X01ScoreManager(x);
            scoreManager.setDoubleOutAttempts(doubleOutAttempts);
            playerDataList.add(new X01PlayerData(new CheckoutChart(getActivity()), playerName, scoreManager));
        }
        return playerDataList;
    }

    private void scrollToPlayerInList() {
        RecyclerView playersListView = getView().findViewById(R.id.play_players_listView);
        assert playersListView != null;
        playersListView.smoothScrollToPosition(game.getCurrentPlayerIdx());
    }

    private void initNumPadView(View container) {
        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) container.findViewById(R.id.num_pad), 180);
        numPadHandler.setListener(this);
    }

    private void initGameDoneView(View container) {
        Button newLegButton = container.findViewById(R.id.new_leg);
        Button completeMatchButton = container.findViewById(R.id.complete_match);
        Button matchSummaryButton = container.findViewById(R.id.match_summary);

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
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void addCurrentLeg(String profileName) {
        if(!currentMatchAdded) {
            GameActivity activity = (GameActivity) getActivity();
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance(activity);
            dbHelper.addX01Match(game);
            ArrayList<GameData> gameData = databaseHelper.getPlayerMatchData(profileName, Long.MAX_VALUE, 1);
            activity.addGame(gameData.get(0));
            setCurrentMatchAdded(true);
        }
    }
}
