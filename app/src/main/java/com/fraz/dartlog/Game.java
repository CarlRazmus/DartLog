package com.fraz.dartlog;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Game {
    protected final Activity context;
    protected final ArrayList<PlayerData> players;
    protected int currentPlayerIdx;
    protected PlayerData winner;
    private LinkedList<Integer> playOrder;

    public Game(Activity context, ArrayList<PlayerData> players) {
        this.context = context;
        this.players = players;
        initGame();
    }

    public abstract void submitScore(int score);

    public boolean isDone() {
        return winner != null;
    }

    public int getCurrentPlayer() {
        return currentPlayerIdx;
    }

    public PlayerData getPlayer(int index) {
        return players.get(index);
    }

    public int getNumberOfPlayers() {
        return players.size();
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

    protected void setCurrentScore(int newScore) {
        players.get(currentPlayerIdx).setScore(newScore);
        playOrder.add(currentPlayerIdx);
    }

    protected void initPlayerData(int score) {
        for (PlayerData player : players) {
            player.initPlayerData(score);
        }
    }
}
