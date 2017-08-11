package com.jlouistechnology.Jazzro.Jazzro;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.FieldsValidator;
import com.jlouistechnology.Jazzro.Helper.FontCustom;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.LoginNewLayoutBinding;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by aipxperts-ubuntu-01 on 21/4/17.
 */

public class LoginInNewScreenActivity extends Activity {
    Constants constants;
    String value, value1;
    FieldsValidator mValidator;
    String token;
    LoginNewLayoutBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.login_new_layout);


        /**
         * initial component
         */
        init();

        /**
         * set component font typeface
         */
        Preview();

        /**
         *set status bar color according app color
         */
        constants.StatusBar();

        /**
         * go to the forgot password
         */
        mBinding.txtForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginInNewScreenActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        /**
         * go to the login button
         */
        mBinding.txtSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginBtnClicked();


            }
        });
    }

    public void Preview() {
         /*edt_email.setText("lgrimsley@gmail.com");
        edt_password.setText("bigbang1");*/
      mBinding.edtEmail.setText("hadelman@jlouis.com");
       mBinding.edtPassword.setText("aipx@1234");
     //   mBinding.edtEmail.setTypeface(FontCustom.setFontOpenSansLight(LoginInNewScreenActivity.this));
      //  mBinding.edtPassword.setTypeface(FontCustom.setFontOpenSansLight(LoginInNewScreenActivity.this));
        mBinding.txtSignin.setTypeface(FontCustom.setFontOpenSansLight(LoginInNewScreenActivity.this));
        mBinding.txtForgotpassword.setTypeface(FontCustom.setFontOpenSansLight(LoginInNewScreenActivity.this));
        mBinding.txt1.setTypeface(FontCustom.setFontOpenSansRegular(LoginInNewScreenActivity.this));

    }

    public void init() {
        constants = new Constants(LoginInNewScreenActivity.this);
        mValidator = new FieldsValidator(LoginInNewScreenActivity.this);

    }

    private void mLoginBtnClicked() {

        boolean isCorrect = true;

        isCorrect = isCorrect
                && mValidator
                .validateNotEmpty(mBinding.edtEmail,
                        getString(R.string.Email_is_required));

        isCorrect = isCorrect
                && mValidator
                .validateEmail(mBinding.edtEmail,
                        getString(R.string.Invalid_email));


        isCorrect = isCorrect
                && mValidator
                .validateNotEmpty(mBinding.edtPassword,
                        getString(R.string.Password_is_required));
        isCorrect = isCorrect
                && mValidator
                .validateLength(mBinding.edtPassword, 6, 15,
                        getString(R.string.Password_must_be_withine_6_to_15));


        if (!isCorrect) {

            return;
        }


        value = mBinding.edtEmail.getText().toString();
        value1 = mBinding.edtPassword.getText().toString();

        if (Utils.checkInternetConnection(LoginInNewScreenActivity.this)) {
            new ExecuteTask_Login().execute();
        } else {
            Toast.makeText(LoginInNewScreenActivity.this, getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
        }

    }

    class ExecuteTask_Login extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(LoginInNewScreenActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = WebService.PostData(value, value1, WebService.Login);
            Log.e("res....login", "" + res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                WebService.dismissProgress();
                JSONObject json1 = new JSONObject(result);
                if(json1.optString("token").equalsIgnoreCase(""))
                {
                    Toast.makeText(LoginInNewScreenActivity.this,json1.optString("detail"),Toast.LENGTH_LONG).show();

                }else {
                    token = json1.optString("token");
                    Pref.setValue(LoginInNewScreenActivity.this, Constants.TOKEN, token);
                    new ExecuteTask_get_data().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    class ExecuteTask_get_data extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(LoginInNewScreenActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            if (!token.equals("") && !token.isEmpty()) {
                try {

                    res = WebService.getResponseUsingHeader(WebService.USER, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginInNewScreenActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            Log.e("res", "Login data" + res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                WebService.dismissProgress();
                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.getJSONObject("data");
                String fname = json2.optString("fname");
                String lname = json2.optString("lname");
                String email = json2.optString("email");
                Log.e("email",""+email);
                Pref.setValue(LoginInNewScreenActivity.this, Constants.PREF_PROFILE_EMAIL, email);
                /**
                 * for set dashboard title display login first last name
                 */
                Pref.setValue(LoginInNewScreenActivity.this, "fname", fname + " " + lname);


                LoginInNewScreenActivity.this.overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
                Pref.setValue(LoginInNewScreenActivity.this,"first_login","1");
               // Pref.setValue(LoginInNewScreenActivity.this,"last_sync_contact_id",0);
                Intent i = new Intent(LoginInNewScreenActivity.this, DashboardNewActivity.class);
                startActivity(i);
                finish();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
