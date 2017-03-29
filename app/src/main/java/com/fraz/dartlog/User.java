package com.fraz.dartlog;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by CarlR on 29/03/2017.
 */

public class User {
    private String displayName;
    private String email;
    private int id = -1;
    private static User singletonUser;
    private GoogleApiClient mGoogleApiClient;

    private void User(){

    }

    private static User getInstance(){
        if(singletonUser == null)
            singletonUser = new User();
        return singletonUser;
    }

    public static String getFullName() {
        return getInstance().displayName;
    }

    public static void setFullName(String fullName) {
        getInstance().displayName = fullName;
    }

    public static String getEmail() {
        return getInstance().email;
    }

    public static void setEmail(String email) {
        getInstance().email = email;
    }

    public static int getId() {
        return getInstance().id;
    }

    public static void setId(int id) {
        getInstance().id = id;
    }

    public static GoogleApiClient getmGoogleApiClient() {
        return getInstance().mGoogleApiClient;
    }

    public static void setGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        getInstance().mGoogleApiClient = mGoogleApiClient;
    }

    public static void clearAllData() {
        getInstance().displayName = "";
        getInstance().email = "";
        getInstance().id = -1;

    }
}
