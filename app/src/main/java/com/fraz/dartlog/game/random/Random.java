package com.fraz.dartlog.game.random;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fraz.dartlog.game.Game;
import com.fraz.dartlog.game.PlayerData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Random extends Game implements Serializable{

    private final int nrOfFields;
    private int currentField = 0;
    private ArrayList<Integer> fields = new ArrayList<>();
    java.util.Random rand;
    private RandomGameActivity context;


    public Random(RandomGameActivity context, ArrayList<? extends PlayerData> playerData, int nrOfFields) {
        super(context, playerData);
        this.context = context;
        this.nrOfFields = nrOfFields;
        rand = new java.util.Random();

        addFields(nrOfFields);
        updateFieldNrTextView();
    }

    private void addFields(int nrOfFields) {
        for(int i=0; i<nrOfFields; i++)
            generateRandomFieldNr();
    }

    private void generateRandomFieldNr() {
        if(isNetworkAvailable())
            fields.add(generateRandomNrFromSpace());
        else
            fields.add(rand.nextInt(20)+1);
    }

    private Integer generateRandomNrFromSpace() {
        return rand.nextInt(20) + 1;
    }

    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        Log.d("Internet connection", String.valueOf(haveConnectedWifi || haveConnectedMobile));
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void updateFieldNrTextView(){
        context.updateCurrentFieldTextView(fields.get(currentField));
    }

    public boolean submitScore(int score) {
        if (!isGameOver()) {
            super.submitScore(score);
            updateGameState();
        }
        return true;
    }

    private void updateGameState() {
        nextPlayer();
        if (currentPlayerIdx == 0) {
            currentField += 1;

            if (currentField == nrOfFields) {
                setWinner();
                showWinnerToast();
            }
            else
                updateFieldNrTextView();
        }
    }

    private void setWinner() {
        setWinner(getPlayerWithHighestScore());
    }

    private PlayerData getPlayerWithHighestScore() {
        return Collections.max(getPlayers(), new Comparator<PlayerData>() {
            @Override
            public int compare(PlayerData playerData, PlayerData t1) {
                return playerData.getScore() - t1.getScore();
            }
        });
    }

    public void newLeg() {
        newGame();
    }
}
