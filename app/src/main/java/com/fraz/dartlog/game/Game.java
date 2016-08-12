package com.fraz.dartlog.game;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import com.fraz.dartlog.game.settings.GameData;

import java.util.Calendar;
import java.util.LinkedList;

public abstract class Game {

    protected GameData gameData;
    protected Activity context;
    protected int currentPlayerIdx;
    private LinkedList<Integer> playOrder;

    /** Needed for serialization of subclasses. */
    protected Game() {
    }

    public Game(Activity context, GameData gameData) {
        this.context = context;
        this.gameData = gameData;
    }

    public boolean submitScore(int newScore) {
        playOrder.add(currentPlayerIdx);
        return getPlayer(currentPlayerIdx).submitScore(newScore);
    }

    public void undo() {
        if (!playOrder.isEmpty()) {
            int lastPlayerIdx = playOrder.removeLast();
            getPlayer(lastPlayerIdx).undoScore();
            currentPlayerIdx = lastPlayerIdx;
            gameData.setWinner(null);
        }
    }

    public boolean isGameOver() {
        return gameData.getWinner() != null;
    }

    public int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    public PlayerData getPlayer(int index) {
        return gameData.getPlayer(index);
    }

    protected void newGame() {
        playOrder = new LinkedList<>();
        int startingPlayer = getNextStartingPlayer(gameData);
        gameData.newGame(startingPlayer);
        currentPlayerIdx = startingPlayer;
    }

    protected void showWinnerToast() {
        CharSequence text = String.format("Winner: %s!",
                getPlayer(currentPlayerIdx).getPlayerName());
        showToast(text);
    }

    protected void showToast(CharSequence text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    protected void nextPlayer() {
        currentPlayerIdx = (currentPlayerIdx + 1) % gameData.getNumberOfPlayers();
    }

    protected int getNextStartingPlayer(GameData lastGameData) {
        return (lastGameData.getStartingPlayerIdx() + 1) % gameData.getNumberOfPlayers();
    }

    public int getNumberOfPlayers() {
        return gameData.getNumberOfPlayers();
    }

    public Calendar getDate() {
        return gameData.getDate();
    }

    public PlayerData getStartingPlayer() {
        return getPlayer(gameData.getStartingPlayerIdx());
    }

    public PlayerData getWinner()
    {
        return gameData.getWinner();
    }
}
