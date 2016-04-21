package com.fraz.dartlog;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class X01 extends Game implements Serializable {

    private int startingScore;
    private Map<Integer, String> checkouts = new HashMap<>();

    public X01(Activity context, ArrayList<X01PlayerData> players, int x) {
        super(context, players);

        startingScore = x*100 + 1;
        initPlayerData(startingScore);
        currentPlayerIdx = 0;

        initCheckoutMap();
    }

    public boolean submitScore(int score) {
        if (!isDone()) {
            if (!super.submitScore(score)) {
                showBustToast();
            }
            updateGameState();
        }
        return true;
    }

    public void newLeg() {
        initPlayerData(startingScore);
        initGame();
    }

    private void updateGameState() {
        X01PlayerData currentPlayer = players.get(currentPlayerIdx);
        if (currentPlayer.getScore() == 0) {
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
        int score = players.get(currentPlayerIdx).getScore();
        String checkoutHint = checkouts.get(score);
        if (checkoutHint == null) {
            checkoutHint = "Checkout Unavailable";
        }
        return checkoutHint;
    }

    private void showBustToast() {
        CharSequence text = "Bust!";
        showToast(text);
    }
}
