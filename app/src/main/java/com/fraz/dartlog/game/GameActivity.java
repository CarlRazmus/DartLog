package com.fraz.dartlog.game;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fraz.dartlog.OnBackPressedDialogFragment;
import com.fraz.dartlog.R;
import com.fraz.dartlog.game.x01.X01GameActivity;

import java.util.ArrayList;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements OnBackPressedDialogFragment.OnBackPressedDialogListener{

    private GamePagerAdapter adapter;
    private ArrayList<GameData> games = new ArrayList<>();
    private ViewPager matchPager;
    private GameStatisticsFragment gameStatisticsFragment;
    private X01GameActivity x01GameActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (savedInstanceState != null && savedInstanceState.containsKey("games"))
        {
            games = (ArrayList<GameData>) savedInstanceState.getSerializable("games");
        }

        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction transaction = f.beginTransaction();
        x01GameActivity = new X01GameActivity();
        x01GameActivity.setArguments(getIntent().getExtras());
        transaction.add(R.id.game_fragment, x01GameActivity);
        transaction.commit();

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
                checkoutText = "Double out";
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

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            DialogFragment onBackPressedDialogFragment = new OnBackPressedDialogFragment();
            onBackPressedDialogFragment.show(getFragmentManager(), "OnBackPressedDialogFragment");
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing.
    }

    public void addGame(GameData game)
    {
        games.add(game);
    }

    public void showStatistics()
    {
        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction transaction = f.beginTransaction();
        gameStatisticsFragment = GameStatisticsFragment.newInstance(games);
        transaction.replace(R.id.game_fragment, gameStatisticsFragment);
        transaction.addToBackStack("statistics");
        transaction.commit();
    }
}
