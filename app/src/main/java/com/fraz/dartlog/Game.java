package com.fraz.dartlog;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;

public class Game {
    protected final Activity context;
    protected final ArrayList<PlayerData> players;
    protected int currentPlayerIdx;
    protected PlayerData winner;

    public Game(Activity context, ArrayList<PlayerData> players) {
        this.context = context;
        this.players = players;
        currentPlayerIdx = 0;
    }

    public boolean isDone() {
        return winner != null;
    }

    public int getCurrentPlayer(){
        return currentPlayerIdx;
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
        setActivePlayer((currentPlayerIdx + 1) % players.size());
    }

    protected void setActivePlayer(int playerIdx) {
        players.get(currentPlayerIdx).setActive(false);
        currentPlayerIdx = playerIdx;
        players.get(currentPlayerIdx).setActive(true);
    }
}
