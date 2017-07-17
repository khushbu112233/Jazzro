package com.jlouistechnology.Jazzro.Jazzro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.jlouistechnology.Jazzro.R;

/**
 * Created by aipxperts on 9/2/17.
 */

public class BaseActivity extends Activity {

    public ProgressDialog mProgressDialog;
    public Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        mContext = BaseActivity.this;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgressDialog.show();
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setContentView(R.layout.progressdialog);
    }
}
