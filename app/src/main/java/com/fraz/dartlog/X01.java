package com.fraz.dartlog;

import android.app.Activity;

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
    private Map<Integer, String> checkouts = new HashMap<>();

    public X01(Activity context, ArrayList<PlayerData> players, int x) {
        super(context, players);

        startingScore = x*100 + 1;
        initPlayerData(startingScore);
        setActivePlayer(0);

        initCheckoutMap();
    }

    public void enterScore(int score) {
        if (!isDone()) {
            PlayerData currentPlayer = players.get(currentPlayerIdx);
            int newScore = currentPlayer.getCurrentScore() - score;
            if (newScore < 0) {
                newScore = currentPlayer.getCurrentScore();
                showBustToast();
            }
            setCurrentScore(newScore);
            updateGameState();
        }
    }

    public void newLeg() {
        initPlayerData(startingScore);
        initGame();
    }

    private void updateGameState() {
        PlayerData currentPlayer = players.get(currentPlayerIdx);
        int currentScore = currentPlayer.getCurrentScore();
        if (currentScore == 0) {
            setWinner(currentPlayer);
            showWinnerToast();
        }
        else {
            nextPlayer();
        }
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

    @Override
    public String getHintText() {
        int currentScore = players.get(currentPlayerIdx).getCurrentScore();
        String checkoutHint = checkouts.get(currentScore);
        if (checkoutHint == null) {
            checkoutHint = checkoutUnavailableText;
        }
        return checkoutHint;
    }

    private void showBustToast() {
        CharSequence text = "Bust!";
        showToast(text);
    }
}
