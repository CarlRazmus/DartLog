package com.fraz.dartlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fraz.dartlog.game.setup.SetupActivity;
import com.fraz.dartlog.statistics.ProfileListActivity;
import com.fraz.dartlog.statistics.StatisticsActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button playButton = (Button) findViewById(R.id.play_x01_button);
        Button profilesButton = (Button) findViewById(R.id.profiles_button);
        Button statButton = (Button) findViewById(R.id.stat_button);
        Button randomButton = (Button) findViewById(R.id.play_random_button);

        assert playButton != null;
        assert profilesButton != null;
        assert statButton != null;
        assert randomButton != null;

        playButton.setOnClickListener(this);
        profilesButton.setOnClickListener(this);
        statButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            case R.id.stat_button:
                startStatActivity();
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

    private void startStatActivity() {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }
}
