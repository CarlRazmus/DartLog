package com.fraz.dartlog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class X01 {

    private final Activity context;
    private final ArrayList<PlayerData> players;
    private final String checkoutUnavailableText = "Checkout Unavailable";
    private Integer currentPlayerIdx;
    private Integer startingScore;
    private PlayerData winner;
    private TextView checkoutView;
    private Map<Integer, String> checkouts = new HashMap<>();

    public X01(Activity context, ArrayList<PlayerData> players, Integer x) {
        this.context = context;
        this.players = players;

        currentPlayerIdx = 0;
        startingScore = x*100 + 1;
        resetScores();

        initCheckoutView();
        initCheckoutMap();
    }

    private void initCheckoutMap() {
        InputStream inputStream = context.getResources().openRawResource(R.raw.checkout_chart);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line = reader.readLine();
            while (line != null) {
                String[] checkout = line.split(",");
                checkouts.put(Integer.parseInt(checkout[0]), checkout[1]);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            updateGameState();
        }
    }

    public Boolean isDone() {
        return winner != null;
    }

    public void newLeg() {
        setActivePlayer(0);
        winner = null;
        resetScores();
        updateCheckoutHint();
    }

    private void updateGameState() {
        PlayerData currentPlayer = players.get(currentPlayerIdx);
        Integer currentScore = currentPlayer.getScore();
        if (currentScore == 0) {
            setWinner(currentPlayer);
            showWinnerToast();
        }
        else {
            nextPlayer();
            updateCheckoutHint();
        }
    }

    private void updateCheckoutHint() {
        Integer currentScore = players.get(currentPlayerIdx).getScore();
        String checkoutHint = checkouts.get(currentScore);
        if (checkoutHint == null) {
            checkoutHint = checkoutUnavailableText;
        }

        checkoutView.setText(checkoutHint);
    }

    private void initCheckoutView() {
        checkoutView = (TextView) context.findViewById(R.id.game_hint);
        checkoutView.setText(checkoutUnavailableText);
        checkoutView.setVisibility(View.VISIBLE);
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

    private void resetScores() {
        players.get(currentPlayerIdx).setActive(true);
        for (PlayerData player : players) {
            player.setScore(startingScore);
        }
    }
}
