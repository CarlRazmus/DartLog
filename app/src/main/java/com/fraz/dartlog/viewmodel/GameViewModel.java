package com.fraz.dartlog.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;

import com.fraz.dartlog.game.Game;
import com.fraz.dartlog.game.PlayerData;
import com.fraz.dartlog.util.Event;

public abstract class GameViewModel<T extends Game> extends ViewModel {

    private MutableLiveData<T> game = new MutableLiveData<>();
    private MutableLiveData<PlayerData> winner = new MutableLiveData<>();
    private MutableLiveData<Integer> currentPlayerIdx = new MutableLiveData<>();

    public MutableLiveData<PlayerData> getWinner() {
        return winner;
    }
    public MutableLiveData<Integer> getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    private MutableLiveData<Event<String>> completeMatchEvent = new MutableLiveData<>();
    public MutableLiveData<Event<String>> getCompleteMatchEvent()
    {
        return completeMatchEvent;
    }

    public T getGame() {
        return game.getValue();
    }

    public MutableLiveData<T> getGameObservable() {
        return game;
    }

    /**
     * Initiates the game data.
     *
     * If a game exists in savedInstanceState it is restored otherwise a new game is created.
     *
     * @param savedInstanceState Bundle with save
     * @param intent
     */
    abstract public void initGame(Bundle savedInstanceState, Intent intent);

    /**
     * Save the current game to database.
     */
    abstract void saveGame();

    abstract public void newLeg();

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

    public void completeMatch()
    {
        saveGame();
        onCompleteMatchEvent();
    }

    void notifyGameUpdated()
    {
        winner.setValue(getGame().getWinner());
        currentPlayerIdx.setValue(getGame().getCurrentPlayerIdx());
        getGameObservable().setValue(getGame());
    }

    private  void onCompleteMatchEvent()
    {
        completeMatchEvent.setValue(new Event<>(""));
    }

    public PlayerViewModel getPlayerViewModel(int position) {
        PlayerViewModel playerViewModel = new PlayerViewModel();
        playerViewModel.updatePlayer(getGame().getPlayer(position));
        return playerViewModel;
    }
}
