package com.fraz.dartlog;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;

public class X01 {

    private final Context context;
    private final ArrayList<PlayerData> players;
    private Integer currentPlayerIdx;
    private PlayerData winner;

    public X01(Context context, ArrayList<PlayerData> players, Integer x) {
        this.context = context;
        this.players = players;
        currentPlayerIdx = 0;

        players.get(currentPlayerIdx).setActive(true);
        for (PlayerData player : players) {
            player.setScore(x*100 + 1);
        }
    }

    public void enterScore(Integer score) {
        if (winner == null) {
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
        players.get(currentPlayerIdx).setActive(false);
        currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
        players.get(currentPlayerIdx).setActive(true);
    }
}
