package com.jlouistechnology.Jazzro.Helper;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by aipxperts on 14/2/17.
 */


class MyTextView extends EditText {
    public MyTextView(Context context) {
        super(context);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Just ignore the [Enter] key
            return true;
        }
        // Handle all other keys in the default way
        return super.onKeyDown(keyCode, event);
    }
}
