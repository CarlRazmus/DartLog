package com.fraz.dartlog.game;

import android.text.InputFilter;
import android.text.Spanned;

class ScoreFilter implements InputFilter {

    private int maxScore;

    public ScoreFilter(int maxScore){
        this.maxScore = maxScore;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        try {
            int number = Integer.parseInt(dest.toString() + source);
            if (number > maxScore)
                return source.subSequence(start, end - 1);
        } catch (NumberFormatException e) {
            return null;
        }

        return null;
    }
}
