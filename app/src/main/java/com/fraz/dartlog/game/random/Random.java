package com.fraz.dartlog.game.random;

import com.fraz.dartlog.game.Game;
import com.fraz.dartlog.game.PlayerData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Random extends Game implements Serializable{

    private final int nrOfTurns;
    private ArrayList<Integer> fields = new ArrayList<>();
    java.util.Random rand;


    public Random(RandomGameActivity context, ArrayList<? extends PlayerData> playerData, int nrOfTurns) {
        super(context, playerData);
        this.nrOfTurns = nrOfTurns;
        rand = new java.util.Random();

        addFields(nrOfTurns);
    }

    public int getNextStartingPlayer() {
        return 0;
    }

    public int getCurrentField(){return fields.get(getTurn() - 1);}

    public int getNextField() {
        int field = 0;

        try {
            field = fields.get(getTurn());
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return field;
    }
    private void addFields(int nrOfTurns) {
        for(int i=0; i<nrOfTurns; i++)
            generateRandomFieldNr();
    }

    private void generateRandomFieldNr() {
        if(isNetworkAvailable())
            fields.add(generateRandomNrFromSpace());
        else
            fields.add(rand.nextInt(20) + 1);
    }

    private Integer generateRandomNrFromSpace() {
        return rand.nextInt(20) + 1;
    }

    private boolean isNetworkAvailable() {
    /*    boolean haveConnectedWifi = false;
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
        */
        return false;
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
        if (getTurn() == nrOfTurns + 1) {
            setWinner();
            showWinnerToast();
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
        fields = new ArrayList<>();
        addFields(nrOfTurns);
        newGame();
    }
}
