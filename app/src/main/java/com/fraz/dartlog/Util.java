package com.fraz.dartlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class Util {

    private static String PROFILE_NAMES = "PROFILE_NAMES_STRING";

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static void saveProfileNames(ArrayList<String> profileNames, Context context) {
        JSONArray jsonArray = new JSONArray(profileNames);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putString(PROFILE_NAMES, jsonArray.toString());
        mEdit1.commit();
    }

    public static ArrayList<String> loadProfileNames(Context context) {
        ArrayList<String> profileNames = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            profileNames = convertToArrayList( prefs.getString(PROFILE_NAMES, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileNames;
    }

    private static ArrayList<String> convertToArrayList(String listString) throws JSONException {
        ArrayList<String> listData = null;

        if (listString != null) {
            JSONArray jsonArray = new JSONArray(listString);
            listData = new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++){
                try {
                    listData.add(jsonArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return listData;
    }

    public static void removePlayer(String name, Context context){
        ArrayList<String> playerNames = loadProfileNames(context);
        Log.d("RemovePlayers", "found players : " + playerNames.toString());
        if(playerNames.contains(name)){
            playerNames.remove(name);
            saveProfileNames(playerNames, context);
        }
    }

    public static void addPlayer(String name, Context context) {
        ArrayList<String> profiles = loadProfileNames(context);
        profiles.add(name);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEdit1 = sp.edit();
        JSONArray jsonArray = new JSONArray(profiles);
        mEdit1.putString(PROFILE_NAMES, jsonArray.toString());
        mEdit1.commit();
    }
}
