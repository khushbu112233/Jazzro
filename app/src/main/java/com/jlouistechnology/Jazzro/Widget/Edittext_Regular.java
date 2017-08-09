package com.jlouistechnology.Jazzro.Widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class Edittext_Regular extends EditText {
    public Edittext_Regular(Context context) {
        super(context);
        setFont();
    }

    public Edittext_Regular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public Edittext_Regular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sf_Regular.otf");
        setTypeface(font, Typeface.NORMAL);
    }
}
