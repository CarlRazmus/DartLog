package com.fraz.dartlog.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class GameData implements Serializable {

    private PlayerData winner = null;
    private Calendar date;
    private ArrayList<? extends PlayerData> players;
    private String gameType;


    public GameData(ArrayList<? extends PlayerData> players, Calendar date,
                    PlayerData winner, String gameType) {

        this.players = players;
        this.date = date;
        this.winner = winner;
        this.gameType = gameType;
    }

    public PlayerData getWinner() {
        return winner;
    }

    public PlayerData getPlayer(int index) {
        return players.get(index);
    }

    public PlayerData getPlayer(String name) {
        for (PlayerData player : players) {
            if (player.getPlayerName().equals(name))
                return player;
        }
        return null;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public Calendar getDate()
    {
        return date;
    }

    public String getGameType() { return gameType; }

    public String[] getPlayerNames() {
        String[] names = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            names[i] = players.get(i).getPlayerName();
        }
        return names;
    }
}
