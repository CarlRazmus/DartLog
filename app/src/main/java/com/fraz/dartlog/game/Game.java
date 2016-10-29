package com.fraz.dartlog.game;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public abstract class Game {

    private LinkedList<Integer> playOrder = new LinkedList<>();
    protected Activity context;
    protected int currentPlayerIdx;

    private PlayerData winner = null;
    private int startingPlayerIdx;
    private Calendar date;
    private ArrayList<? extends PlayerData> players;

    /** Needed for serialization of subclasses. */
    protected Game() {
    }

    public Game(Activity context, ArrayList<? extends PlayerData> players) {
        this.context = context;
        this.players = players;
        this.startingPlayerIdx = 0;
        this.date = Calendar.getInstance();
        this.currentPlayerIdx = getStartingPlayerIdx();
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
            setWinner(null);
        }
    }

    public boolean isGameOver() {
        return getWinner() != null;
    }

    public int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    public PlayerData getPlayer(int index) {
        return players.get(index);
    }

    protected void newGame() {
        playOrder = new LinkedList<>();
        resetPlayers(players);
        currentPlayerIdx = startingPlayerIdx = getNextStartingPlayer();
        setWinner(null);
        date = Calendar.getInstance();
    }

    private void resetPlayers(ArrayList<? extends PlayerData> players) {
        for (PlayerData player : players) {
            player.resetScore();
        }
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
        currentPlayerIdx = (currentPlayerIdx + 1) % getNumberOfPlayers();
    }

    public int getNextStartingPlayer() {
        return (getStartingPlayerIdx() + 1) % getNumberOfPlayers();
    }

    public void setStartingPlayerIdx(int startingPlayerIdx) {
        this.startingPlayerIdx = startingPlayerIdx;
    }

    public void setWinner(PlayerData winner) {
        this.winner = winner;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public Calendar getDate() {
        return date;
    }

    public int getStartingPlayerIdx() {
        return startingPlayerIdx;
    }

    public PlayerData getWinner()
    {
        return winner;
    }

    public ArrayList<? extends PlayerData> getPlayers() {
        return players;
    }

    public int getRound() {
        return playOrder.size() / players.size() + 1;
    }
}
