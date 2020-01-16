package com.fraz.dartlog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.fraz.dartlog.db.DartLogDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Util {

    private static String PROFILE_NAMES = "PROFILE_NAMES_STRING";
    private static Executor executorInstance;

    public static Executor getExecutorInstance(){
        if(executorInstance == null)
            executorInstance = Executors.newFixedThreadPool(8);
        return executorInstance;
    }

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

    public static void setDialogSize(Activity activity, Dialog dialog, float width, float height) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        layoutParams.width = (int) (displayMetrics.widthPixels * width);
        layoutParams.height = (int) (displayMetrics.heightPixels * height);

        dialog.getWindow().setAttributes(layoutParams);
    }

    public void updateDbStatistics(Context context) {
        UpdateStatisticsAsyncTask asyncTask = new UpdateStatisticsAsyncTask(context);
        asyncTask.executeOnExecutor(getExecutorInstance());
    }


    private class UpdateStatisticsAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context context;
        public UpdateStatisticsAsyncTask(Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DartLogDatabaseHelper.getInstance(context).createStatisticViews();
            return null;
        }
    }
}
