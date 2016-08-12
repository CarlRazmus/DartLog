package com.fraz.dartlog.game.settings;

import com.fraz.dartlog.game.PlayerData;

import java.util.ArrayList;
import java.util.Calendar;

public class GameData {

    private PlayerData winner = null;
    private int startingPlayerIdx = 0;
    private Calendar date;
    protected ArrayList<? extends PlayerData> players;

    public GameData(ArrayList<? extends PlayerData> players, Calendar date) {
        this.players = players;
        this.date = date;
    }

    public void setWinner(PlayerData playerIdx) {
        winner = playerIdx;
    }

    public PlayerData getWinner() {
        return winner;
    }

    public int getStartingPlayerIdx() {
        return startingPlayerIdx;
    }

    public PlayerData getPlayer(int index) {
        return players.get(index);
    }

    public PlayerData getPlayer(String playerName) {
        for(PlayerData player : players )
        {
            if (player.getPlayerName().equals(playerName))
                return player;
        }
        throw new IllegalArgumentException();
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public Calendar getDate()
    {
        return date;
    }

    public void newGame(int startingPlayerIdx)
    {
        this.startingPlayerIdx = startingPlayerIdx;
        this.date = Calendar.getInstance();
        for(PlayerData player : players)
        {
            player.resetScore();
        }
    }
}
