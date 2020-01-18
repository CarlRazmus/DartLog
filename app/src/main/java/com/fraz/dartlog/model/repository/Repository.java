package com.fraz.dartlog.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.fraz.dartlog.Util;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.model.Profile;

import java.util.ArrayList;

public class Repository {

    private final DartLogDatabaseHelper dbHelper;
    private static Repository repositoryInstance = null;

    private final MutableLiveData<ArrayList<String>> profiles = new MutableLiveData<>();

    public static Repository getInstance() {
        if (repositoryInstance == null) {
            repositoryInstance = new Repository();
        }
        return repositoryInstance;
    }

    private Repository()
    {
        dbHelper = DartLogDatabaseHelper.getInstance();
        profiles.setValue(Util.loadProfileNames());
    }

    public LiveData<ArrayList<String>> getProfiles()
    {
        return profiles;
    }

    public void addPlayer(String name)
    {
        if(!dbHelper.playerExist(name)) {
            if (dbHelper.addPlayer(name) != -1) {
                Util.addPlayer(name);
            }
        }
        else if(!Util.loadProfileNames().contains(name))
            Util.addPlayer(name);
        profiles.setValue(Util.loadProfileNames());
    }

    public void removeProfile(String name)
    {
        Util.removePlayer(name);
        profiles.setValue(Util.loadProfileNames());
    }
}
