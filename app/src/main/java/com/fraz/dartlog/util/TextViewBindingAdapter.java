package com.fraz.dartlog.util;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.widget.TextView;

public class TextViewBindingAdapter {
    @BindingAdapter("isBold")
    public static void setBold(TextView view, boolean isBold) {
        if (isBold) {
            view.setTypeface(null, Typeface.BOLD);
        } else {
            view.setTypeface(null, Typeface.NORMAL);
        }
    }
}
