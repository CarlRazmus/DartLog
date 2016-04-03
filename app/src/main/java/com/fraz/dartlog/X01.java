package com.fraz.dartlog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class X01 extends Game {

    private final String checkoutUnavailableText = "Checkout Unavailable";
    private int startingScore;
    private TextView checkoutView;
    private Map<Integer, String> checkouts = new HashMap<>();

    public X01(Activity context, ArrayList<PlayerData> players, int x) {
        super(context, players);

        setActivePlayer(0);
        startingScore = x*100 + 1;
        resetScores();

        initCheckoutView();
        initCheckoutMap();
    }

    public void enterScore(int score) {
        if (!isDone()) {
            PlayerData currentPlayer = players.get(currentPlayerIdx);
            int newScore = currentPlayer.getScore() - score;
            if (newScore < 0) {
                showBustToast();
            } else {
                currentPlayer.setScore(newScore);
            }
            updateGameState();
        }
    }

    public void newLeg() {
        setActivePlayer(0);
        winner = null;
        resetScores();
        updateCheckoutHint();
    }

    private void updateGameState() {
        PlayerData currentPlayer = players.get(currentPlayerIdx);
        int currentScore = currentPlayer.getScore();
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
        int currentScore = players.get(currentPlayerIdx).getScore();
        String checkoutHint = checkouts.get(currentScore);
        if (checkoutHint == null) {
            checkoutHint = checkoutUnavailableText;
        }

        checkoutView.setText(checkoutHint);
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

    private void initCheckoutView() {
        checkoutView = (TextView) context.findViewById(R.id.game_hint);
        checkoutView.setText(checkoutUnavailableText);
        checkoutView.setVisibility(View.VISIBLE);
    }

    private void showBustToast() {
        CharSequence text = "Bust!";
        showToast(text);
    }

    private void resetScores() {
        for (PlayerData player : players) {
            player.setScore(startingScore);
        }
    }
}
