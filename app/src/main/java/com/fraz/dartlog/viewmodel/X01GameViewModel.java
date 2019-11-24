package com.fraz.dartlog.viewmodel;

import android.content.Intent;
import android.os.Bundle;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.x01.X01;
import com.fraz.dartlog.game.x01.X01PlayerData;
import com.fraz.dartlog.game.x01.X01ScoreManager;

import java.util.ArrayList;

public class X01GameViewModel extends GameViewModel<X01> {

    public void initGame(Bundle savedInstanceState, Intent intent) {
        if (!restoreSavedGame(savedInstanceState))
        {
            getGameObservable().setValue(new X01(createPlayerDataList(intent)));
        }
    }

    @Override
    public void newLeg()
    {
        saveGame();
        getGame().newLeg();
        notifyGameUpdated();
    }

    void saveGame()
    {
        DartLogDatabaseHelper.getInstance().addX01Match(getGame());
    }

    private boolean restoreSavedGame(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("game")) {
            getGameObservable().setValue((X01) savedInstanceState.getSerializable("game"));
            return true;
        }
        return false;
    }

    /**
     * Create and return a list of player data from a list of player names.
     */
    private ArrayList<X01PlayerData> createPlayerDataList(Intent intent) {
        ArrayList<String> playerNames = intent.getStringArrayListExtra("playerNames");
        int x = intent.getIntExtra("x", 3);
        int doubleOutAttempts = intent.getIntExtra("double_out", 0);
        ArrayList<X01PlayerData> playerDataList = new ArrayList<>();
        for (String playerName : playerNames) {
            X01ScoreManager scoreManager = new X01ScoreManager(x);
            scoreManager.setDoubleOutAttempts(doubleOutAttempts);
            playerDataList.add(new X01PlayerData(new CheckoutChart(), playerName, scoreManager));
        }
        return playerDataList;
    }
}
