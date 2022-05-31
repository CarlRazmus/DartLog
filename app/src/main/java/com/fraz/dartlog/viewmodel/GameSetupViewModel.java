package com.fraz.dartlog.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import com.fraz.dartlog.model.repository.Repository;

import java.util.ArrayList;

public class GameSetupViewModel extends AndroidViewModel {

    private final Observer<ArrayList<String>> observer;
    private ArrayList<String> profiles;
    private final MutableLiveData<ArrayList<String>> participants = new MutableLiveData<>();
    private  ArrayList<Participant> newParticipants = new ArrayList<>();
    private final Repository repository;


    public GameSetupViewModel(Application application)
    {
        super(application);

        repository = Repository.getInstance();
        profiles = repository.getProfiles().getValue();
        participants.setValue(new ArrayList<String>());
        initNewParticipants();


        observer = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable ArrayList<String> names) {
                profiles = names;
                initNewParticipants();
            }
        };
        repository.getProfiles().observeForever(observer);
    }


    private void initNewParticipants() {
        newParticipants.clear();
        for (String profileName : profiles)
        {
            newParticipants.add(new Participant(profileName, participants.getValue().contains(profileName)));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.getProfiles().removeObserver(observer);
    }

    public LiveData<ArrayList<String>> getParticipants()
    {
        return participants;
    }

    public void setParticipantsToNewParticipants(){
        participants.getValue().clear();
        for (Participant participant : newParticipants){
            if (participant.isParticipating.getValue())
                participants.getValue().add(participant.name);
        }
    }

    public ArrayList<Participant> getNewParticipants() {
        return newParticipants;
    }

    public void setNewParticipantsToParticipants() {
        for (Participant participant : newParticipants){
            if (participants.getValue().contains(participant.name))
                participant.isParticipating.setValue(true);
            else
                participant.isParticipating.setValue(false);
        }
    }

    public class Participant {
        public String name;
        private MutableLiveData<Boolean> isParticipating = new MutableLiveData<>();

        public Participant(String name, boolean isParticipating){
            this.name = name;
            this.isParticipating.setValue(isParticipating);
        }

        public MutableLiveData<Boolean> getIsParticipating() {
            return isParticipating;
        }
    }
}
