package com.fraz.dartlog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fraz.dartlog.model.Profile;
import com.fraz.dartlog.model.repository.Repository;

public class ProfileViewModel extends ViewModel {

    private final Repository repository;
    private Profile profile;

    public ProfileViewModel()
    {
        super();

        repository = Repository.getInstance();
    }

    public Profile getProfile()
    {
        return profile;
    }

    public void setProfile(String profileName) {
        profile = repository.GetProfile(profileName);
    }
}
