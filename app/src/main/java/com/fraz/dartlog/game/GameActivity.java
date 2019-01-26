package com.fraz.dartlog.game;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.fraz.dartlog.OnBackPressedDialogFragment;
import com.fraz.dartlog.R;
import com.fraz.dartlog.statistics.MatchPagerAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements OnBackPressedDialogFragment.OnBackPressedDialogListener{

    private GamePagerAdapter adapter;
    private ArrayList<GameData> games = new ArrayList<>();
    private ViewPager matchPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (savedInstanceState != null && savedInstanceState.containsKey("games"))
        {
            games = (ArrayList<GameData>) savedInstanceState.getSerializable("games");
        }
        adapter = new GamePagerAdapter(getSupportFragmentManager(), getIntent().getExtras(), games);
        matchPager = findViewById(R.id.game_pager);
        matchPager.setAdapter(adapter);
        matchPager.setCurrentItem(0);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("games", games);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        String gameType = extras.getString("gameType");
        if (gameType != null && gameType.equals("x01")) {
            int x = extras.getInt("x");
            getSupportActionBar().setTitle(String.format(Locale.getDefault(), "%d01", x));
            int numPlayers = extras.getStringArrayList("playerNames").size();
            String checkoutText;
            if (extras.containsKey("double_out")) {
                int double_out = extras.getInt("double_out");
                if (double_out == -1) {
                    checkoutText = "Double out";
                }
                else
                {
                    checkoutText = String.format(Locale.getDefault(), "Single out after %d attempts", double_out);
                }
            }
            else
            {
                checkoutText = "Single out";
            }
            String numPlayerText;
            if (numPlayers > 1)
                numPlayerText = "Players";
            else
                numPlayerText = "Player";

            getSupportActionBar().setSubtitle(
                    String.format(Locale.getDefault(), "%d %s | %s", numPlayers, numPlayerText, checkoutText));
        }
    }

    public void addGame(GameData game)
    {
        games.add(game);
        adapter.notifyDataSetChanged();
    }

    public void setPagerItem(int item)
    {
        matchPager.setCurrentItem(item, true);
    }

    @Override
    public void onBackPressed() {
        DialogFragment onBackPressedDialogFragment = new OnBackPressedDialogFragment();
        onBackPressedDialogFragment.show(getFragmentManager(), "OnBackPressedDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing.
    }
}