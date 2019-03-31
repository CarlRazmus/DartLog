package com.fraz.dartlog;

import android.os.Bundle;

public class AboutActivity extends MenuBackground {

    public AboutActivity(){
        super(R.layout.activity_app_settings);
        setParentActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
