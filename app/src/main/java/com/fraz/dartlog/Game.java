package com.fraz.dartlog;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Game {
    protected final Activity context;
    protected final ArrayList<X01PlayerData> players;
    protected int currentPlayerIdx;
    protected X01PlayerData winner;
    private LinkedList<Integer> playOrder;

    public Game(Activity context, ArrayList<X01PlayerData> players) {
        this.context = context;
        this.players = players;
        initGame();
    }

    public boolean isDone() {
        return winner != null;
    }

    public int getCurrentPlayer() {
        return currentPlayerIdx;
    }

    public X01PlayerData getPlayer(int index) {
        return players.get(index);
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public boolean submitScore(int newScore) {
        playOrder.add(currentPlayerIdx);
        return players.get(currentPlayerIdx).submitScore(newScore);
    }

    public void undo() {
        if (!playOrder.isEmpty()) {
            int lastPlayerIdx = playOrder.removeLast();
            players.get(lastPlayerIdx).undo();
            currentPlayerIdx = lastPlayerIdx;
            winner = null;
        }
    }

    public String getHintText() {
        return null;
    }

    protected void initGame() {
        playOrder = new LinkedList<>();
        winner = null;
        currentPlayerIdx = 0;
    }

    protected void setWinner(X01PlayerData currentPlayer) {
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

    protected void initPlayerData(int score) {
        for (X01PlayerData player : players) {
            player.initPlayerData(score);
        }
    }
}
