package com.jlouistechnology.Jazzro.Widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextView_Bold extends TextView {
    public TextView_Bold(Context context) {
        super(context);
        setFont();
    }

    public TextView_Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public TextView_Bold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/SFUIDisplay_Semibold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
