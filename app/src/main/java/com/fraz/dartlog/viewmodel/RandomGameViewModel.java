package com.fraz.dartlog.viewmodel;

import android.content.Intent;
import android.os.Bundle;

import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.AdditionScoreManager;
import com.fraz.dartlog.game.PlayerData;
import com.fraz.dartlog.game.random.Random;

import java.util.ArrayList;

public class RandomGameViewModel extends GameViewModel<Random>
{
    public void initGame(Bundle savedInstanceState, Intent intent) {
        if (!restoreSavedGame(savedInstanceState))
        {
            int nrOfTurns = intent.getIntExtra("turns", 10);
            getGameObservable().setValue(new Random(createPlayerDataList(intent), nrOfTurns));
        }
    }

    @Override
    public void newLeg()
    {
        saveGame();
        getGame().newLeg();
        notifyGameUpdated();
    }

    @Override
    void saveGame() {
        DartLogDatabaseHelper.getInstance().addRandomMatch(getGame());
    }

    /**
     * Create and return a list of player data from a list of player names.
     */
    private ArrayList<PlayerData> createPlayerDataList(Intent intent) {
        ArrayList<String> playerNames = intent.getStringArrayListExtra("playerNames");
        ArrayList<PlayerData> playerDataList = new ArrayList<>();

        for (String playerName : playerNames) {
            playerDataList.add(new PlayerData(playerName, new AdditionScoreManager()));
        }
        return playerDataList;
    }

    private boolean restoreSavedGame(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("randomGame")) {
            getGameObservable().setValue((Random) savedInstanceState.getSerializable("randomGame"));
            return true;
        }
        return false;
    }
}
