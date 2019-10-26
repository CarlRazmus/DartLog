package com.fraz.dartlog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.fraz.dartlog.model.repository.Repository;

import java.util.ArrayList;

public class ProfileListViewModel extends AndroidViewModel {

    private final LiveData<ArrayList<String>> profiles;
    private final Repository repository;

    public ProfileListViewModel(Application application)
    {
        super(application);

        repository = Repository.getInstance(application);
        profiles = repository.getPlayers();
    }

    public LiveData<ArrayList<String>> getProfilesObservable()
    {
        return profiles;
    }

    public void AddProfile(String name)
    {
        repository.addPlayer(name);
    }
}
