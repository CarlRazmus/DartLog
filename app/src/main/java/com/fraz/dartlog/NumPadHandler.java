package com.fraz.dartlog;

import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class NumPadHandler implements View.OnClickListener {

    private final TextView scoreInput;
    private final ViewGroup numpad;
    private InputEventListener listener;

    public NumPadHandler(ViewGroup numpadView) {
        this.numpad = (ViewGroup) numpadView.findViewById(R.id.num_pad);
        this.scoreInput = (TextView) numpadView.findViewById(R.id.score_input);
        initButtons();
        initInputField();
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
            case R.id.enter:
                enter();
                break;
        }
    }

    public void setListener(InputEventListener listener) {
        this.listener = listener;
    }

    private void enter() {
        Integer score = getInput();
        listener.enter(score);
        scoreInput.setText("0");
    }

    private Integer getInput() {
        return Integer.parseInt(scoreInput.getText().toString());

    }

    private void eraseNumber() {
        CharSequence currentText = scoreInput.getText();
        int length = currentText.length();
        if (length > 1) {
            scoreInput.setText(currentText.subSequence(0, length - 1));
        } else {
            scoreInput.setText("0");
        }
    }

    private void addNumber(CharSequence number) {
        if (scoreInput.getText().toString().equals("0")) {
            scoreInput.setText(number);
        } else {
            scoreInput.append(number);
        }
    }

    private void initButtons() {
        int childCount = numpad.getChildCount();
        for (int i=0; i < childCount; i++) {
            View v = numpad.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(this);
            }
        }
    }

    private void initInputField() {
        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new ScoreFilter();
        scoreInput.setFilters(inputFilters);
    }
}
