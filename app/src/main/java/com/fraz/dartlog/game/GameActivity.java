package com.fraz.dartlog.game;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fraz.dartlog.OnBackPressedDialogFragment;
import com.fraz.dartlog.R;
import com.fraz.dartlog.statistics.MatchPagerAdapter;

public class GameActivity extends AppCompatActivity implements OnBackPressedDialogFragment.OnBackPressedDialogListener{

    private GamePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        adapter = new GamePagerAdapter(getSupportFragmentManager(), getIntent().getExtras());
        ViewPager matchPager = findViewById(R.id.game_pager);
        matchPager.setAdapter(adapter);
        matchPager.setCurrentItem(0);
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
