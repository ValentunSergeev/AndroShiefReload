package com.valentun.androshief;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatButton;

/**
 * Created by Valentun on 14.03.2017.
 */

public class Support {
    public static void colorizeButton (AppCompatButton btn, int color) {
        btn.setTextColor(Color.WHITE);
        btn.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }
}
