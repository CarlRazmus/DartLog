package com.fraz.dartlog;

import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class NumPadHandler implements View.OnClickListener {

    private final TextView scoreInput;
    private InputEventListener listener;

    public NumPadHandler(ViewGroup numpadView) {
        this.scoreInput = (TextView) numpadView.findViewById(R.id.score_input);
        ViewGroup numpad = (ViewGroup) numpadView.findViewById(R.id.num_pad);
        setClickListenerInView(numpad);
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
        int score = getInput();
        listener.enter(score);
        scoreInput.setText("0");
    }

    private int getInput() {
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

    private void setClickListenerInView(ViewGroup viewGroup) {
        for (int i=0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof Button || v instanceof ImageButton) {
                v.setOnClickListener(this);
            } else if (v instanceof ViewGroup) {
                setClickListenerInView((ViewGroup) v);
            }
        }
    }

    private void initInputField() {
        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new ScoreFilter();
        scoreInput.setFilters(inputFilters);
    }
}
