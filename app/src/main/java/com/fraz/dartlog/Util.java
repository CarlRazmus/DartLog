package com.fraz.dartlog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Util {

    private static String PROFILE_NAMES = "PROFILE_NAMES_STRING";

    public static float dpFromPx(final float px) {
        return px / MyApplication.getInstance().getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final float dp) {
        return dp * MyApplication.getInstance().getResources().getDisplayMetrics().density;
    }

    public static void saveProfileNames(ArrayList<String> profileNames) {
        JSONArray jsonArray = new JSONArray(profileNames);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putString(PROFILE_NAMES, jsonArray.toString());
        mEdit1.commit();
    }

    public static ArrayList<String> loadProfileNames() {
        ArrayList<String> profileNames = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
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

    public static void removePlayer(String name){
        ArrayList<String> playerNames = loadProfileNames();
        Log.d("RemovePlayers", "found players : " + playerNames.toString());
        if(playerNames.contains(name)){
            playerNames.remove(name);
            saveProfileNames(playerNames);
        }
    }

    public static void addPlayer(String name) {
        ArrayList<String> profiles = loadProfileNames();
        profiles.add(name);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
        SharedPreferences.Editor mEdit1 = sp.edit();
        JSONArray jsonArray = new JSONArray(profiles);
        mEdit1.putString(PROFILE_NAMES, jsonArray.toString());
        mEdit1.commit();
    }

    public static void setDialogSize(Activity activity, Dialog dialog, float width, float height) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        layoutParams.width = (int) (displayMetrics.widthPixels * width);
        layoutParams.height = (int) (displayMetrics.heightPixels * height);

        dialog.getWindow().setAttributes(layoutParams);
    }


    public static void showToast(CharSequence text, Context context) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
