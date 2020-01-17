package com.fraz.dartlog.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.fraz.dartlog.game.PlayerData;

public class PlayerViewModel extends ViewModel {

    private final LiveData<PlayerData> winner;
    private final LiveData<PlayerData> nextToThrow;
    private PlayerData player;


    private MutableLiveData<String> playerName = new MutableLiveData<>();
    private MutableLiveData<String> currentScore = new MutableLiveData<>();
    private MutableLiveData<String> scoreHistory = new MutableLiveData<>();

    public LiveData<Boolean> isWinner() {
        return Transformations.map(winner, new Function<PlayerData, Boolean>() {
            @Override
            public Boolean apply(PlayerData input) {
                return input == player;
            }
        });
    }

    public LiveData<Boolean> isNextToThrow() {
        return Transformations.map(nextToThrow, new Function<PlayerData, Boolean>() {
            @Override
            public Boolean apply(PlayerData input) {
                return input == player;
            }
        });
    }


    public PlayerViewModel(LiveData<PlayerData> winner, LiveData<PlayerData> nextToThrow) {
        this.winner = winner;
        this.nextToThrow = nextToThrow;
    }

    public LiveData<String> getPlayerName() {
        return playerName;
    }
    public MutableLiveData<String> getCurrentScore() {
        return currentScore;
    }
    public LiveData<String> getScoreHistory() {
        return scoreHistory;
    }
    public PlayerData getPlayer() {
        return player;
    }

    public void updatePlayer(PlayerData player)
    {
        this.player = player;
        this.playerName.setValue(player.getPlayerName());
        this.scoreHistory.setValue(getScoresString());
        this.currentScore.setValue(String.valueOf(player.getScore()));
    }

    private String getScoresString() {
        StringBuilder scoreHistoryText = new StringBuilder();
        if (player != null) {
            for (Integer score : player.getScoreHistory()) {
                scoreHistoryText.append(String.format("%s ", Integer.toString(score)));
            }
        }
        return scoreHistoryText.toString().trim();
    }
}
