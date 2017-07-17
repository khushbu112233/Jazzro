package com.jlouistechnology.Jazzro.Jazzro;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Model.ForgotpasswordModel;
import com.jlouistechnology.Jazzro.Model.PeticularGroupListServiceModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.ActivityForgotPasswordBinding;
import com.jlouistechnology.Jazzro.network.ApiClient;
import com.jlouistechnology.Jazzro.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    private ActivityForgotPasswordBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        mBinding.txtSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     JSONObject jsonObject = new JSONObject();
                RequestBody body = null;
                try {
                    jsonObject.put("email", mBinding.edForgotPass.getText().toString());

                    body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), String.valueOf((new JSONObject((jsonObject.toString())))));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<ForgotpasswordModel> call = apiService.forgotPassword(jsonObject.toString());
                //  mProgressDialog.show();
                call.enqueue(new Callback<ForgotpasswordModel>() {
                    @Override
                    public void onResponse(Call<ForgotpasswordModel> call, Response<ForgotpasswordModel> response) {

                        if (response.body() != null) {
                            if (response.body().status != 400) {
                                finish();
                            }
                            Toast.makeText(ForgotPasswordActivity.this, response.body().detail, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ForgotpasswordModel> call, Throwable t) {

                    }
                });*/

                if (TextUtils.isEmpty(mBinding.edForgotPass.getText().toString())) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please provide email address.", Toast.LENGTH_LONG).show();
                } else if (!emailValidator(mBinding.edForgotPass.getText().toString())) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please provide valid email address.", Toast.LENGTH_LONG).show();
                } else {
                    new forgotpasswordTask().execute();
                }

            }
        });


    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public class forgotpasswordTask extends AsyncTask<String, Integer, String> {

        private String res;
        private String getEmail = mBinding.edForgotPass.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(ForgotPasswordActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            res = WebService.forgotpassword(getEmail, WebService.BASE_URL + "user/forgot-password", Pref.getValue(ForgotPasswordActivity.this, Constants.TOKEN, ""));

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            WebService.dismissProgress();
            try {
                JSONObject json1 = new JSONObject(res);
                int status = json1.optInt("status");
                String message = json1.optString("detail");
                if (status != 400) {
                    finish();
                    Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
