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

    private final Button submitButton;
    private InputEventListener listener;
    private final TextView scoreInput;

    public NumPadHandler(ViewGroup numpadView, int maxScore) {
        submitButton = (Button) numpadView.findViewById(R.id.submit_button);
        ViewGroup numpad = (ViewGroup) numpadView.findViewById(R.id.num_pad);
        scoreInput = (TextView) numpadView.findViewById(R.id.score_view);
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
                case R.id.submit_button:
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
        submitButton.setText(R.string.no_score);
        scoreInput.setText("0");
        scoreInput.setVisibility(View.GONE);
    }

    private int getInput() {
        if (isNoScore(scoreInput.getText()))
            return 0;
        else
            return Integer.parseInt(scoreInput.getText().toString());
    }

    private void eraseNumber() {
        CharSequence scoreText = scoreInput.getText();
        int length = scoreText.length();
        if (!isNoScore(scoreText) && length > 1) {
            scoreInput.setText(scoreText.subSequence(0, length - 1));
        } else {
            scoreInput.setText("0");
            scoreInput.setVisibility(View.GONE);
            submitButton.setText(R.string.no_score);
        }
    }

    private void addNumber(CharSequence number) {
        CharSequence scoreText = scoreInput.getText();
        if (isNoScore(scoreText)) {
            scoreInput.setText(number);
            scoreInput.setVisibility(View.VISIBLE);
            if (!number.equals("0")) {
                submitButton.setText(R.string.submit);
            }
        } else {
            scoreInput.setText(String.format("%s%s", scoreText, number));
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

    private boolean isNoScore(CharSequence scoreText) {
        return (scoreText.length() == 0 || scoreText.equals("0"));
    }
}
