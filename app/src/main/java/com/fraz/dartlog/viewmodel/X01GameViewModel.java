package com.fraz.dartlog.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.x01.X01;
import com.fraz.dartlog.game.x01.X01PlayerData;
import com.fraz.dartlog.game.x01.X01ScoreManager;
import com.fraz.dartlog.util.Event;

import java.util.ArrayList;

public class X01GameViewModel extends ViewModel {

    private MutableLiveData<X01> game = new MutableLiveData<>();

    private MutableLiveData<Event<String>> completeMatchEvent = new MutableLiveData<>();

    public MutableLiveData<Event<String>> getCompleteMatchEvent()
    {
        return completeMatchEvent;
    }
    public void onCompleteMatchEvent()
    {
        completeMatchEvent.setValue(new Event<>(""));
    }

    public void Init(Bundle savedInstanceState, Intent intent) {
        if (savedInstanceState != null && savedInstanceState.containsKey("game")) {
            game.setValue((X01) savedInstanceState.getSerializable("game"));
        }
        else
        {
            game.setValue(new X01(createPlayerDataList(intent)));
        }
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

    public X01 getGame() {
        return game.getValue();
    }

    public MutableLiveData<X01> getGameObservable() {
        return game;
    }

    public void submitScore(int score)
    {
        getGame().submitScore(score);
        notifyGameUpdated();
    }

    public void undo()
    {
        getGame().undo();
        notifyGameUpdated();
    }

    public void newLeg()
    {
        saveGame();
        getGame().newLeg();
        notifyGameUpdated();
    }

    public void completeMatch()
    {
        saveGame();
        onCompleteMatchEvent();
    }

    private void saveGame()
    {
        DartLogDatabaseHelper.getInstance().addX01Match(getGame());
    }

    private void notifyGameUpdated()
    {
        game.setValue(game.getValue());
    }
}
