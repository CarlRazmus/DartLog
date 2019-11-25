package com.fraz.dartlog;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.setup.SetupActivity;
import com.fraz.dartlog.statistics.ProfileListActivity;

import java.util.ArrayList;


public class MainActivity extends MenuBackground implements View.OnClickListener {

    public MainActivity(){
        super(R.layout.activity_main);
        setParentActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdateStatisticsAsyncTask asyncTask = new UpdateStatisticsAsyncTask(this);
        asyncTask.executeOnExecutor(Util.getExecutorInstance());

        Button playButton = (Button) findViewById(R.id.play_x01_button);
        Button profilesButton = (Button) findViewById(R.id.profiles_button);
        Button randomButton = (Button) findViewById(R.id.play_random_button);

        assert playButton != null;
        assert profilesButton != null;
        assert randomButton != null;

        playButton.setOnClickListener(this);
        profilesButton.setOnClickListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.x01_preferences, false);
        randomButton.setOnClickListener(this);

        checkIfSharedPlayersPreferencesExists();

        //Initialize checkout chart now since it is expensive to load.
        CheckoutChart.initCheckoutMap(getApplicationContext());
    }

    private void checkIfSharedPlayersPreferencesExists() {
        ArrayList<String> playerNames = Util.loadProfileNames(this);
        /* If no preference file exist -> create an empty list, or get the player names from
         * the database (if the database contains any names) */
        if(playerNames == null || playerNames.isEmpty()) {
            if(DartLogDatabaseHelper.getInstance(this).getPlayers().size() > 0)
                Util.saveProfileNames(DartLogDatabaseHelper.getInstance(this).getPlayers(), this);
            else
                Util.saveProfileNames(new ArrayList<String>(), this);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.play_x01_button:
                startPlayersActivity("x01");
                break;
            case R.id.profiles_button:
                startProfileActivity();
                break;
            case R.id.play_random_button:
                startPlayersActivity("random");
                break;
        }
    }

    private void startPlayersActivity(String gameType) {
        Intent intent = new Intent(this, SetupActivity.class);
        intent.putExtra("gameType", gameType);
        startActivity(intent);
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileListActivity.class);
        startActivity(intent);
    }

    private class UpdateStatisticsAsyncTask extends AsyncTask<Void, Void, Void>{

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
