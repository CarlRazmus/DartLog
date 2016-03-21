package com.fraz.dartlog;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;

public class PlayActivity extends ActionBarActivity implements NumPadEventListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) findViewById(R.id.numpad_view));
        numPadHandler.setListener(this);
    }

    @Override
    public void enter(Integer score) {

    }
}
