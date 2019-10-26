package com.fraz.dartlog.model.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.fraz.dartlog.db.DartLogDatabaseHelper;

import java.util.ArrayList;

public class Repository {

    private final DartLogDatabaseHelper dbHelper;
    private static Repository repositoryInstance = null;

    private final MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();

    public static Repository getInstance(Application application) {
        if (repositoryInstance == null) {
            repositoryInstance = new Repository(application);
        }
        return repositoryInstance;
    }

    private Repository(Application application)
    {
        dbHelper = DartLogDatabaseHelper.getInstance();
        data.setValue(dbHelper.getPlayers());
    }

    public LiveData<ArrayList<String>> getPlayers()
    {
        return data;
    }

    public long addPlayer(String playerName)
    {
        /*
        if(!dbHelper.playerExist(name)) {
            if (dbHelper.addPlayer(name) != -1) {
                Util.addPlayer(name, getContext());
                getProfilesAdapter().updateProfiles();
            }
        }
        else if(!Util.loadProfileNames(getContext()).contains(name))
            Util.addPlayer(name, getContext());
*/
        long id = dbHelper.addPlayer(playerName);
        data.setValue(dbHelper.getPlayers());
        return id;
    }
}
