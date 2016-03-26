package com.fraz.dartlog;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;

public class X01 {

    private final Context context;
    private final ArrayList<PlayerData> players;
    private Integer currentPlayerIdx;
    private Integer startingScore;
    private PlayerData winner;

    public X01(Context context, ArrayList<PlayerData> players, Integer x) {
        this.context = context;
        this.players = players;

        currentPlayerIdx = 0;
        startingScore = x*100 + 1;
        resetScores();
    }

    public void enterScore(Integer score) {
        if (!isDone()) {
            PlayerData currentPlayer = players.get(currentPlayerIdx);
            Integer newScore = currentPlayer.getScore() - score;
            if (newScore < 0) {
                showBustToast();
            } else {
                currentPlayer.setScore(newScore);
            }

            if (newScore == 0) {
                setWinner(currentPlayer);
                showWinnerToast();
            }
            else
                nextPlayer();
        }
    }

    public Boolean isDone() {
        return winner != null;
    }

    private void setWinner(PlayerData currentPlayer) {
        winner = currentPlayer;
    }

    private void showBustToast() {
        CharSequence text = "Bust!";
        showToast(text);
    }

    private void showWinnerToast() {
        CharSequence text = String.format("Winner: %s!",
                players.get(currentPlayerIdx).getPlayerName());
        showToast(text);
    }

    private void showToast(CharSequence text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void nextPlayer() {
        setActivePlayer((currentPlayerIdx + 1) % players.size());
    }

    private void setActivePlayer(Integer playerIdx) {
        players.get(currentPlayerIdx).setActive(false);
        currentPlayerIdx = playerIdx;
        players.get(currentPlayerIdx).setActive(true);
    }

    public int getCurrentPlayer(){
        return currentPlayerIdx;
    }

    public void newLeg() {
        setActivePlayer(0);
        winner = null;
        resetScores();
    }

    private void resetScores() {
        players.get(currentPlayerIdx).setActive(true);
        for (PlayerData player : players) {
            player.setScore(startingScore);
        }
    }
}
