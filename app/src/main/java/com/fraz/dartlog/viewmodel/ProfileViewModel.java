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

import java.util.HashMap;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<Profile> profile = new MutableLiveData<>();
    private final Repository repository;
    private AsyncTaskFetchSummaryData runnerSummary;
    private AsyncTaskFetchHighScores runnerHighScores;

    private MutableLiveData<Boolean> finishedLoadingSummary = new MutableLiveData<>();
    private MutableLiveData<Boolean> finishedLoadingHighScores = new MutableLiveData<>();
    private MediatorLiveData<Boolean> showSummaryProgressBar = new MediatorLiveData<>();
    private MutableLiveData<Boolean> startupDelayTimePassed = new MutableLiveData<>();


    public ProfileViewModel()
    {
        super();

        finishedLoadingHighScores.setValue(false);
        finishedLoadingSummary.setValue(false);
        showSummaryProgressBar.setValue(false);
        startupDelayTimePassed.setValue(false);

        runnerSummary = new AsyncTaskFetchSummaryData();
        runnerHighScores = new AsyncTaskFetchHighScores();

        showSummaryProgressBar.addSource(finishedLoadingSummary, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (startupDelayTimePassed.getValue() == null ||
                        finishedLoadingSummary.getValue() == null) {
                    return;
                }
                showSummaryProgressBar.setValue(
                        startupDelayTimePassed.getValue() &&
                                !finishedLoadingSummary.getValue());
            }});
        showSummaryProgressBar.addSource(startupDelayTimePassed, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (startupDelayTimePassed.getValue() == null ||
                        finishedLoadingSummary.getValue() == null) {
                    return;
                }
                showSummaryProgressBar.setValue(
                        startupDelayTimePassed.getValue() &&
                                !finishedLoadingSummary.getValue());
            }});
        repository = Repository.getInstance();
    }

    public Profile getProfile()
    {
        return profile.getValue();
    }

    public LiveData<Boolean> getFinishedLoadingSummary() {
        return finishedLoadingSummary;
    }

    public LiveData<Boolean> getFinishedLoadingHighScores() {
        return finishedLoadingHighScores;
    }

    public LiveData<Boolean> getShowSummaryProgressBar(){
        return showSummaryProgressBar;
    }

    public void setProfile(String profileName) {

        if(getProfile() != null && getProfile().getName().equals(profileName))
            return;

        profile.setValue(new Profile());
        getProfile().setName(profileName);

        finishedLoadingSummary.setValue(false);
        finishedLoadingHighScores.setValue(false);
        startupDelayTimePassed.setValue(false);
        startupDelayTimePassed.setValue(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Boolean value = getFinishedLoadingSummary().getValue();
                if(value != null && !value)
                {
                    startupDelayTimePassed.setValue(true);
                }
            }
        }, 1000);

        runnerSummary.executeOnExecutor(Util.getExecutorInstance());
        runnerHighScores.executeOnExecutor(Util.getExecutorInstance());
    }

    public void deleteProfile()
    {
        Repository.getInstance().removeProfile(profile.getValue().getName());
    }

    private class AsyncTaskFetchSummaryData extends AsyncTask<Void, Void, Profile> {

        @Override
        protected Profile doInBackground(Void... voids) {
            Thread.currentThread().setName(getProfile().getName() + "Summary");
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance();
            Profile profileData = new Profile();
            profileData.setGamesWon(databaseHelper.getNumberOfGamesWon(getProfile().getName()));
            profileData.setGamesPlayed(databaseHelper.getNumberOfGamesPlayed(getProfile().getName()));
            return profileData;
        }

        @Override
        protected void onPostExecute(Profile profileData) {
            finishedLoadingSummary.setValue(true);
            getProfile().setGamesWon(profileData.getGamesWon());
            getProfile().setGamesPlayed(profileData.getGamesPlayed());
            profile.setValue(getProfile());
        }
    }

    private class AsyncTaskFetchHighScores extends AsyncTask<Void, Void, Profile> {

        @Override
        protected Profile doInBackground(Void... voids) {
            DartLogDatabaseHelper databaseHelper = DartLogDatabaseHelper.getInstance();
            long  profileId = databaseHelper.getPlayerId(getProfile().getName());
            Thread.currentThread().setName(getProfile().getName() + "_Highscore");

            Profile profileData = new Profile();
            profileData.setHighestCheckoutGame(databaseHelper.getHighestCheckout(profileId));
            HashMap<String, GameData> fewestTurnsX01Games = databaseHelper.getfewestTurnsX01Games(profileId);
            profileData.setFewestTurns301Game(fewestTurnsX01Games.get("3"));
            profileData.setFewestTurns501Game(fewestTurnsX01Games.get("5"));
            return profileData;
        }

        @Override
        protected void onPostExecute(Profile profileData) {
            getProfile().setFewestTurns501Game(profileData.getFewestTurns501Game());
            getProfile().setFewestTurns301Game(profileData.getFewestTurns301Game());
            getProfile().setHighestCheckoutGame(profileData.getHighestCheckoutGame());
            profile.setValue(getProfile());
            finishedLoadingHighScores.setValue(true);
        }
    }

}
