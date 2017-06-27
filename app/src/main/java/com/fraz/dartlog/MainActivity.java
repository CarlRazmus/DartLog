package com.fraz.dartlog;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.fraz.dartlog.game.setup.SetupActivity;
import com.fraz.dartlog.statistics.ProfileListActivity;



public class MainActivity extends MenuBackground implements View.OnClickListener {

    DbFileHandler dbFileHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this, R.layout.activity_main);
        Button playButton = (Button) findViewById(R.id.play_x01_button);
        Button profilesButton = (Button) findViewById(R.id.profiles_button);
        Button randomButton = (Button) findViewById(R.id.play_random_button);

        dbFileHandler = new DbFileHandler(this);

        /* Example of how to open UI to create a new db copy */
        //dbFileHandler.createFile("application/x-sqlite3");

        assert playButton != null;
        assert profilesButton != null;
        assert randomButton != null;

        playButton.setOnClickListener(this);
        profilesButton.setOnClickListener(this);

        PreferenceManager.setDefaultValues(this, R.xml.x01_preferences, false);
        randomButton.setOnClickListener(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DbFileHandler.WRITE_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("dbFileHandler", "URI = " + data.getData());
            Log.d("dbFileHandler", "getPath = " + data.getData().getPath());
            dbFileHandler.onFileCreated(data.getData());
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
}
