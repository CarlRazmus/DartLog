package com.fraz.dartlog;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayActivity extends ActionBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initButtons();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.zero:
            case R.id.one:
            case R.id.two:
            case R.id.three:
            case R.id.four:
            case R.id.five:
            case R.id.six:
            case R.id.seven:
            case R.id.eight:
            case R.id.nine:
                addNumber(((Button) v).getText());
                break;
            case R.id.erase:
                eraseNumber();
                break;
        }
    }

    private void eraseNumber() {
        TextView scoreInput = (TextView) findViewById(R.id.score_input);
        CharSequence currentText = scoreInput.getText();
        int length = currentText.length();
        if (length > 1) {
            scoreInput.setText(currentText.subSequence(0, length - 1));
        } else {
            scoreInput.setText("0");
        }
    }

    private void addNumber(CharSequence number) {
        TextView scoreInput = (TextView) findViewById(R.id.score_input);
        Log.i("MyTag", "addNumber: " + scoreInput.getText());
        if (scoreInput.getText().toString().equals("0")) {
            Log.i("MyTag", "addNumber: setting text");
            scoreInput.setText(number);
        } else {
            Log.i("MyTag", "addNumber: appending text");
            scoreInput.append(number);
        }
    }

    private void initButtons() {
        RelativeLayout numpad = (RelativeLayout) findViewById(R.id.numpad);
        int childCount = numpad.getChildCount();
        for (int i=0; i < childCount; i++) {
            View v = numpad.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(this);
            }
        }
    }
}
