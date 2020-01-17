package com.fraz.dartlog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.fraz.dartlog.model.repository.Repository;
import com.fraz.dartlog.util.Event;

import java.util.ArrayList;

public class ProfileListViewModel extends AndroidViewModel {

    private final LiveData<ArrayList<String>> profiles;
    private final Repository repository;
    private MutableLiveData<Event<String>> addPlayerClickEvent = new MutableLiveData<>();

    public ProfileListViewModel(Application application)
    {
        super(application);

        repository = Repository.getInstance();
        profiles = repository.getProfiles();
    }

    public LiveData<ArrayList<String>> getProfilesObservable()
    {
        return profiles;
    }

    public MutableLiveData<Event<String>> getAddPlayerClickEvent()
    {
        return addPlayerClickEvent;
    }

    public void onAddPlayerClick()
    {
        addPlayerClickEvent.setValue(new Event<>(""));
    }

    public void AddProfile(String name)
    {
        repository.addPlayer(name);
    }
}
