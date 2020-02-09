package com.fraz.dartlog.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.fraz.dartlog.Util;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.model.Profile;
import com.fraz.dartlog.model.repository.Repository;
import com.fraz.dartlog.util.Event;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<Profile> profile = new MutableLiveData<>();
    private ArrayList<GameData> matchHistory = new ArrayList<>();

    private MutableLiveData<Event<String>> highScoresLoaded = new MutableLiveData<>();
    private MutableLiveData<Event<Integer>> matchHistoryLoaded = new MutableLiveData<>();

    private final Repository repository;

    private AsyncTaskFetchSummaryData runnerSummary;
    private AsyncTaskFetchHighScores runnerHighScores;
    private AsyncTaskFetchMatchHistory runnerMatchHistory;

    private MutableLiveData<Boolean> finishedLoadingSummary = new MutableLiveData<>();
    private MutableLiveData<Boolean> finishedLoadingHighScores = new MutableLiveData<>();
    private MediatorLiveData<Boolean> showSummaryProgressBar = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> showHighScoresProgressBar = new MediatorLiveData<>();
    private MutableLiveData<Boolean> startupDelayTimePassed = new MutableLiveData<>();

    public ProfileViewModel()
    {
        super();

        finishedLoadingHighScores.setValue(false);
        finishedLoadingSummary.setValue(false);
        showSummaryProgressBar.setValue(false);
        showHighScoresProgressBar.setValue(false);
        startupDelayTimePassed.setValue(false);

        runnerSummary = new AsyncTaskFetchSummaryData();
        runnerHighScores = new AsyncTaskFetchHighScores();
        runnerMatchHistory = new AsyncTaskFetchMatchHistory();

        Observer<Boolean> summaryObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (startupDelayTimePassed.getValue() == null ||
                        finishedLoadingSummary.getValue() == null) {
                    return;
                }
                showSummaryProgressBar.setValue(
                        startupDelayTimePassed.getValue() &&
                                !finishedLoadingSummary.getValue());
            }};

        Observer<Boolean> highScoresObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (startupDelayTimePassed.getValue() == null ||
                        finishedLoadingHighScores.getValue() == null) {
                    return;
                }
                showHighScoresProgressBar.setValue(
                        startupDelayTimePassed.getValue() &&
                                !finishedLoadingHighScores.getValue());
            }};

        showSummaryProgressBar.addSource(finishedLoadingSummary, summaryObserver);
        showSummaryProgressBar.addSource(startupDelayTimePassed, summaryObserver);
        showHighScoresProgressBar.addSource(finishedLoadingHighScores, highScoresObserver);
        showHighScoresProgressBar.addSource(startupDelayTimePassed, highScoresObserver);

        repository = Repository.getInstance();
    }

    public MutableLiveData<Event<String>> getHighScoresLoaded() {
        return highScoresLoaded;
    }
    public MutableLiveData<Event<Integer>> getMatchHistoryLoaded() {
        return matchHistoryLoaded;
    }
    public String getProfileName()
    {
        return profile.getValue().getName();
    }

    public LiveData<Profile> getProfile()
    {
        return profile;
    }

    public LiveData<Boolean> getFinishedLoadingSummary() {
        return finishedLoadingSummary;
    }

    public LiveData<Boolean> getShowSummaryProgressBar(){
        return showSummaryProgressBar;
    }

    public LiveData<Boolean> getShowHighScoresProgressBar(){
        return showHighScoresProgressBar;
    }

    public MutableLiveData<Boolean> getFinishedLoadingHighScores() {
        return finishedLoadingHighScores;
    }

    public ArrayList<GameData> getMatchHistory() {
        return matchHistory;
    }

    public void setProfile(String profileName) {

        if(profile.getValue() != null && getProfileName().equals(profileName))
            return;

        profile.setValue(new Profile());
        profile.getValue().setName(profileName);

        finishedLoadingSummary.setValue(false);
        finishedLoadingHighScores.setValue(false);
        startupDelayTimePassed.setValue(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startupDelayTimePassed.setValue(true);
            }
        }, 500);


        runnerSummary.executeOnExecutor(Util.getExecutorInstance());
        runnerHighScores.executeOnExecutor(Util.getExecutorInstance());
        runnerMatchHistory.executeOnExecutor(Util.getExecutorInstance());
    }

    public void deleteProfile()
    {
        Repository.getInstance().removeProfile(getProfileName());
    }

    private class AsyncTaskFetchSummaryData extends AsyncTask<Void, Void, Profile> {

        @Override
        protected Profile doInBackground(Void... voids) {
            Thread.currentThread().setName(getProfileName() + "Summary");
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance();
            Profile profileData = new Profile();
            profileData.setGamesWon(databaseHelper.getNumberOfGamesWon(getProfileName()));
            profileData.setGamesPlayed(databaseHelper.getNumberOfGamesPlayed(getProfileName()));
            return profileData;
        }

        @Override
        protected void onPostExecute(Profile profileData) {
            finishedLoadingSummary.setValue(true);
            profile.getValue().setGamesWon(profileData.getGamesWon());
            profile.getValue().setGamesPlayed(profileData.getGamesPlayed());
            profile.setValue(profile.getValue());
        }
    }

    private class AsyncTaskFetchHighScores extends AsyncTask<Void, Void, Profile> {

        @Override
        protected Profile doInBackground(Void... voids) {
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance();
            long  profileId = databaseHelper.getPlayerId(getProfileName());
            Thread.currentThread().setName(getProfileName() + "_Highscore");
            Profile profileData = new Profile();
            profileData.setHighestCheckoutGame(databaseHelper.getHighestCheckout(profileId));
            HashMap<String, GameData> fewestTurnsX01Games = databaseHelper.getfewestTurnsX01Games(profileId);
            profileData.setFewestTurns301Game(fewestTurnsX01Games.get("3"));
            profileData.setFewestTurns501Game(fewestTurnsX01Games.get("5"));
            return profileData;
        }

        @Override
        protected void onPostExecute(Profile profileData) {
            profile.getValue().setFewestTurns301Game(profileData.getFewestTurns301Game());
            profile.getValue().setFewestTurns501Game(profileData.getFewestTurns501Game());
            profile.getValue().setHighestCheckoutGame(profileData.getHighestCheckoutGame());
            profile.setValue(profile.getValue());
            finishedLoadingHighScores.setValue(true);
            highScoresLoaded.setValue(new Event<>(""));
        }
    }

    private class AsyncTaskFetchMatchHistory extends AsyncTask<Void, Integer, Void> {
        private boolean allLoaded = false;
        private long lastLoadedMatchId = Long.MAX_VALUE;
        private final int AMOUNT_ITEMS_TO_LOAD = 20;

        @Override
        protected Void doInBackground(Void... voids) {
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance();

            while(!allLoaded) {
                ArrayList<GameData> newData = databaseHelper.getPlayerMatchData(getProfileName(), lastLoadedMatchId, AMOUNT_ITEMS_TO_LOAD);
                Integer sizeNewData = newData.size();
                if (sizeNewData < AMOUNT_ITEMS_TO_LOAD)
                    allLoaded = true;
                lastLoadedMatchId = databaseHelper.getLastLoadedMatchId();
                matchHistory.addAll(newData);

                publishProgress(sizeNewData);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            matchHistoryLoaded.setValue(new Event<>(values[0]));
        }
    }
}
