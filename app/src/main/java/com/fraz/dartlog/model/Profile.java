package com.fraz.dartlog.model;

import com.fraz.dartlog.game.GameData;

public class Profile {
    private String name;
    private GameData highestCheckoutGame;
    private GameData fewestTurns301Game;
    private GameData fewestTurns501Game;
    private int gamesWon;
    private int gamesPlayed;

    public GameData getHighestCheckoutGame() {
        return highestCheckoutGame;
    }

    public void setHighestCheckoutGame(GameData highestCheckoutGame) {
        this.highestCheckoutGame = highestCheckoutGame;
    }

    public GameData getFewestTurns301Game() {
        return fewestTurns301Game;
    }

    public void setFewestTurns301Game(GameData fewestTurns301Game) {
        this.fewestTurns301Game = fewestTurns301Game;
    }

    public GameData getFewestTurns501Game() {
        return fewestTurns501Game;
    }

    public void setFewestTurns501Game(GameData fewestTurns501Game) {
        this.fewestTurns501Game = fewestTurns501Game;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
