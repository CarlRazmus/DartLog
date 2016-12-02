package com.fraz.dartlog.game;

import android.text.InputFilter;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fraz.dartlog.R;

public class NumPadHandler implements View.OnTouchListener {

    private final TextView scoreInput;
    private InputEventListener listener;

    public NumPadHandler(ViewGroup numpadView, int maxScore) {
        this.scoreInput = (TextView) numpadView.findViewById(R.id.score_input);
        ViewGroup numpad = (ViewGroup) numpadView.findViewById(R.id.num_pad);
        setOnTouchListenerInView(numpad);
        initInputField(maxScore);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
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
                    addNumber(((Button) view).getText());
                    break;
                case R.id.erase:
                    eraseNumber();
                    break;
                case R.id.enter:
                    enter();
                    break;
            }
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        }
        return false;
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

    private void setOnTouchListenerInView(ViewGroup viewGroup) {
        for (int i=0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof Button || v instanceof ImageButton) {
                v.setOnTouchListener(this);
            } else if (v instanceof ViewGroup) {
                setOnTouchListenerInView((ViewGroup) v);
            }
        }
    }

    private void initInputField(int maxScore) {
        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new ScoreFilter(maxScore);
        scoreInput.setFilters(inputFilters);
    }
}
