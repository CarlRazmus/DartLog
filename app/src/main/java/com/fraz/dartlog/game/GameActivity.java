package com.fraz.dartlog.game;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fraz.dartlog.R;

public class GameActivity extends AppCompatActivity {

    private GamePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x01_game);

        adapter = new GamePagerAdapter(getSupportFragmentManager());
        ViewPager matchPager = (ViewPager) findViewById(R.id.match_pager);
        matchPager.setAdapter(adapter);
        matchPager.setCurrentItem(0);
    }
}
