package com.fraz.dartlog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.fraz.dartlog.model.Profile;
import com.fraz.dartlog.model.repository.Repository;

public class ProfileViewModel extends AndroidViewModel {

    private final Repository repository;

    public ProfileViewModel(Application application)
    {
        super(application);

        repository = Repository.getInstance(application);

    }

    public Profile getProfile(String profileName) {
        return repository.GetProfile(profileName);
    }
}
