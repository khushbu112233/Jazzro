package com.jlouistechnology.Jazzro.Jazzro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.FontCustom;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Service.GetallPhoneContact;

import io.fabric.sdk.android.Fabric;
import java.util.Locale;

/**
 * Created by aipxperts on 7/12/16.
 */
public class SplashActivity extends Activity {

    boolean isFinish = false;
    int _splashTime = 3000;
    LinearLayout splash_layout;
    Handler mSplashHandler;
    TextView txt_1, txt_2;

    private Runnable mSplashRunnable = new Runnable() {
        @Override
        public void run() {
            isFinish = true;
            // AddContactGroupFragment.isCountExecute = true;

            /**
             * check login is done or not if login directly go to the dashboard activity
             */

            if (Pref.getValue(SplashActivity.this, Constants.TOKEN, "").equalsIgnoreCase("")) {


                Intent i = new Intent(SplashActivity.this, LoginInNewScreenActivity.class);
                startActivity(i);
               // startService(new Intent(SplashActivity.this, GetallPhoneContact.class));
                finish();

            } else {

                Intent i = new Intent(SplashActivity.this, DashboardNewActivity.class);
                startActivity(i);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /**
         * fabric crashlytics initialize
         */
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_layout);
        /**
         * initial component
         */
        init();
        /**
         * set component font typeface
         */
        Preview();
        /**
         * start animation of component
         */
        StartAnimations();


    }

    @Override
    protected void onResume() {
        super.onResume();

        mSplashHandler = new Handler();
        mSplashHandler.postDelayed(mSplashRunnable, _splashTime);


    }

    @Override
    protected void onPause() {
        super.onPause();
        // mScannerView.stopCamera();
        //  mScannerView.stopCameraPreview();
        if (!isFinish) {
            if (mSplashRunnable != null) {
                if (mSplashHandler != null) {
                    mSplashHandler.removeCallbacks(mSplashRunnable);
                }
            }
        }

    }

    public void init() {
        splash_layout = (LinearLayout) findViewById(R.id.splash_layout);
        txt_1 = (TextView) findViewById(R.id.txt_1);
        txt_2 = (TextView) findViewById(R.id.txt_2);

    }

    public void Preview() {
        txt_1.setTypeface(FontCustom.setFontOpenSansLight(SplashActivity.this));
        txt_2.setTypeface(FontCustom.setFontOpenSansLight(SplashActivity.this));

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        splash_layout.clearAnimation();
        splash_layout.startAnimation(anim);

    }

}
