package com.fraz.dartlog.viewmodel;

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

    public void deleteProfile()
    {
        Repository.getInstance().removeProfile(profile.getName());
    }
}
