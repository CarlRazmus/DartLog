package com.fraz.dartlog.game;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Game {

    protected Activity context;
    protected ArrayList<? extends PlayerData> players;
    protected int currentPlayerIdx;
    protected PlayerData winner;
    private int startingPlayer;

    private LinkedList<Integer> playOrder;

    /** Needed for serialization of subclasses. */
    protected Game() {
    }

    public Game(Activity context, ArrayList<? extends PlayerData> players) {
        this.context = context;
        this.players = players;
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
            winner = null;
        }
    }

    public boolean isGameOver() {
        return winner != null;
    }

    public int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    public PlayerData getPlayer(int index) {
        return players.get(index);
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public PlayerData getStartingPlayer() {
        return players.get(startingPlayer);
    }

    public PlayerData getWinner() {
        return winner;
    }

    protected void newGame() {
        playOrder = new LinkedList<>();
        winner = null;
        currentPlayerIdx = startingPlayer;
        initPlayerData();
    }

    protected void setWinner(PlayerData currentPlayer) {
        winner = currentPlayer;
    }

    protected void showWinnerToast() {
        CharSequence text = String.format("Winner: %s!",
                players.get(currentPlayerIdx).getPlayerName());
        showToast(text);
    }

    protected void showToast(CharSequence text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    protected void nextPlayer() {
        currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
    }

    protected void cycleStartingPlayer() {
        startingPlayer = (startingPlayer + 1) % players.size();
    }

    protected abstract void initPlayerData();
}
