package com.fraz.dartlog.game;

import android.text.InputFilter;
import android.text.Spanned;

class ScoreFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int number = Integer.parseInt(dest.toString() + source);
        if (number > 180)
            return "";
        else
            return source;
    }
}
