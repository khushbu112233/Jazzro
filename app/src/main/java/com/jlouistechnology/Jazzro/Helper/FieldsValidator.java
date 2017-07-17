package com.jlouistechnology.Jazzro.Helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class FieldsValidator {

    private static Context context;

    public FieldsValidator(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean validateEmail(EditText view, String message) {
        if (message == null || message.isEmpty())
            message = "Please enter a valid email";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        if (!Patterns.EMAIL_ADDRESS.matcher(
                view.getText().toString()).matches()) {
            view.setError(ssbuilder);
            //view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
            // view.setPadding(10,10,10,10);
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean validateNotEmpty(EditText view, String message) {
        if (message == null || message.isEmpty())
            message = "This field should not be empty";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        if (view.getText().toString().isEmpty()) {
            view.setError(ssbuilder);
            // view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
            //view.setPadding(10,10,10,10);

            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean validateurl(EditText view, String url_value, String message) {
        if (message == null || message.isEmpty())
            message = "This field should not be empty";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        Log.v("url_value", url_value + "");
        if (!Patterns.WEB_URL.matcher(url_value).matches()) {
            view.setError(ssbuilder);
            // view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
            //view.setPadding(10,10,10,10);

            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean validatevalue(EditText view, String message) {
        if (message == null || message.isEmpty())
            message = "This field should not be empty";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        double value = Double.parseDouble(view.getText().toString());
        if (value < 10) {
            view.setError(ssbuilder);
            // view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
            //view.setPadding(10,10,10,10);

            return false;
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean validatevalue_50(EditText view, String message) {
        if (message == null || message.isEmpty())
            message = "This field should not be empty";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        double value = Double.parseDouble(view.getText().toString());
        if (value < 50) {
            view.setError(ssbuilder);
            // view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
            //view.setPadding(10,10,10,10);

            return false;
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean validatevalue_55(EditText view, String message) {
        if (message == null || message.isEmpty())
            message = "This field should not be empty";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        double value = Double.parseDouble(view.getText().toString());
        if (value < 55) {
            view.setError(ssbuilder);
            // view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
            //view.setPadding(10,10,10,10);

            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean validateNotEmptytxt(TextView view, String message) {
        if (message == null || message.isEmpty())
            message = "This field should not be empty";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        if (view.getText().toString().isEmpty()) {
            view.setError(ssbuilder);
            //view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
            //view.setPadding(10,10,10,10);
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean compare(EditText view, EditText view1, String message) {
        if (message == null || message.isEmpty())
            message = "This field should not be empty";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        if (!view.getText().toString().equalsIgnoreCase(view1.getText().toString())) {
            view.setError(ssbuilder);
            //view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
            //view.setPadding(10,10,10,10);
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean validateNotEmptytext(TextView view, String message, int temp) {
        if (message == null || message.isEmpty())
            message = "This field should not be empty";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        if (temp == 0) {
            if (view.getText().toString().isEmpty()) {

                view.setError(ssbuilder);
                //view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
                view.setPadding(10, 10, 10, 10);
                return false;
            }
        } else {
            view.setError(null);
        }
        return true;
    }

    public boolean validateLength(EditText view, int minLength, int maxLength,
                                  String message) {
        if (view.getText().toString().equals("")
                || view.getText().toString().length() < minLength) {
            if (message == null || message.isEmpty())
                message = "Input should be more than " + minLength
                        + " characters.";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(
                    message);
            ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
            view.setError(ssbuilder);
            return false;
        }
        if (view.getText().toString().length() > maxLength) {
            if (message == null || message.isEmpty())
                message = "Input should be less than " + maxLength
                        + " characters.";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(
                    message);
            ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
            view.setError(ssbuilder);
            return false;
        }
        return true;
    }


    public boolean validateMinLength(EditText view, int minLength,
                                     String message) {
        if (view.getText().toString().equals("")
                || view.getText().toString().length() < minLength) {
            if (message == null || message.isEmpty())
                message = "Input should be more than " + minLength
                        + " characters.";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(
                    message);
            ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
            view.setError(ssbuilder);
            return false;
        }

        return true;
    }

    public boolean validateLength(TextView view, int minLength, int maxLength,
                                  String message) {
        if (view.getText().toString().equals("")
                || view.getText().toString().length() < minLength) {
            if (message == null || message.isEmpty())
                message = "Input should be more than " + minLength
                        + " characters.";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(
                    message);
            ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
            view.setError(ssbuilder);
            return false;
        }
        if (view.getText().toString().length() > maxLength) {
            if (message == null || message.isEmpty())
                message = "Input should be less than " + maxLength
                        + " characters.";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(
                    message);
            ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
            view.setError(ssbuilder);
            return false;
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean validateValidCharacters(EditText view, Pattern pattern,
                                           String message) {
        if (!pattern.matcher(view.getText().toString()).matches()) {
            if (message == null || message.isEmpty())
                message = "This input does not fit the requiered pattern.";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(
                    message);
            ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
            view.setError(ssbuilder);
            // view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
            view.setPadding(10, 10, 10, 10);
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean validateBothPasswordtxt(EditText view, EditText view1, String message) {
        if (message == null || message.isEmpty())
            message = "This field should not be empty";
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        if (!view.getText().toString().equals(view1.getText().toString())) {
            view.setError(ssbuilder);
//view.setBackground(context.getResources().getDrawable(R.drawable.edittext_field_background_red));
//view.setPadding(10,10,10,10);
            return false;
        }
        return true;
    }
}
