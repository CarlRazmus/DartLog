package com.fraz.dartlog.game;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class GameActivity extends AppCompatActivity implements OnBackPressedDialogFragment.OnBackPressedDialogListener{

    private GamePagerAdapter adapter;
    private final ArrayList<GameData> games = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        adapter = new GamePagerAdapter(getSupportFragmentManager(), getIntent().getExtras(), games);
        ViewPager matchPager = findViewById(R.id.game_pager);
        matchPager.setAdapter(adapter);
        matchPager.setCurrentItem(0);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    public void addGame(GameData game)
    {
        games.add(game);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        MenuItem undo_action = menu.findItem(R.id.action_undo);
        undo_action.setEnabled(true);
        return true;
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
