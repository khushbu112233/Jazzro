package com.jlouistechnology.Jazzro.Helper;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.R;

/**
 * Created by aipxperts on 9/12/16.
 */
public class Constants {
    public static Context _context;
    public static final String TOKEN = "TOKEN";

    public Constants(Context context) {

        this._context = context;
    }


    public static final String[] Login_param =
            {
                    "email",
                    "password"
            };
    public static final String[] add_contact =
            {

                    "id",
                    "fname",
                    "lname",
                    "company_name",
                    "phone1",
                    "email1"
            };
    public static final String[] Contact_Delete =
            {
                    "id"
            };

    public static void StatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((Activity) _context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            ((Activity) _context).getWindow().setStatusBarColor(_context.getResources().getColor(R.color.colorPrimaryDark));

        }

    }


    //for constant pref value..........
    public static String PREF_ID = "PREF_ID";
    public static String PREF_FIRSTNAME = "PREF_FIRSTNAME";
    public static String PREF_LASTNAME = "PREF_LASTNAME";
    public static String PREF_DISPLAY_NAME = "PREF_DISPLAY_NAME";
    public static String PREF_STREET_ADDRESS = "PREF_STREET_ADDRESS";
    public static String PREF_STREET_ADDRESS1 = "PREF_STREET_ADDRESS1";
    public static String PREF_STREET_ADDRESS2 = "PREF_STREET_ADDRESS2";
    public static String PREF_CITY = "PREF_CITY";
    public static String PREF_STATE = "PREF_STATE";
    public static String PREF_ZIPCODE = "PREF_ZIPCODE";
    public static String PREF_NOTE = "PREF_NOTE";
    public static String PREF_PHONE = "PREF_PHONE";
    public static String PREF_PHONE1 = "PREF_PHONE1";
    public static String PREF_PHONE2 = "PREF_PHONE2";
    public static String PREF_PHONE3 = "PREF_PHONE3";
    public static String PREF_EMAIL = "PREF_EMAIL";
    public static String PREF_EMAIL1 = "PREF_EMAIL1";
    public static String PREF_EMAIL2 = "PREF_EMAIL2";
    public static String PREF_EMAIL3 = "PREF_EMAIL3";
    public static String PREF_COMPANY_NAME = "PREF_COMPANY_NAME";
    public static String PREF_COMPANY_TITLE = "PREF_COMPANY_TITLE";
    public static String PREF_UNIQUEID = "PREF_UNIQUEID";
    public static String PREF_BIRTHDAY = "PREF_BIRTHDAY";
    public static String PREF_WORKANNIVERSARY = "PREF_WORKANNIVERSARY";
    public static String PREF_IMAGE_URL = "PREF_IMAGE_URL";


    //for user profile screen static data----

    public static String PREF_PROFILE_FIRSTNAME = "PREF_PROFILE_FIRSTNAME";
    public static String PREF_PROFILE_LASTNAME = "PREF_PROFILE_LASTNAME";
    public static String PREF_PROFILE_DISPLAY_NAME = "PREF_PROFILE_DISPLAY_NAME";
    public static String PREF_PROFILE_STREET_ADDRESS = "PREF_PROFILE_STREET_ADDRESS";
    public static String PREF_PROFILE_STREET_ADDRESS1 = "PREF_PROFILE_STREET_ADDRESS1";
    public static String PREF_PROFILE_STREET_ADDRESS2 = "PREF_PROFILE_STREET_ADDRESS2";
    public static String PREF_PROFILE_CITY = "PREF_PROFILE_CITY";
    public static String PREF_PROFILE_STATE = "PREF_PROFILE_STATE";
    public static String PREF_PROFILE_ZIPCODE = "PREF_PROFILE_ZIPCODE";
    public static String PREF_PROFILE_NOTE = "PREF_PROFILE_NOTE";
    public static String PREF_PROFILE_PHONE = "PREF_PROFILE_PHONE";
    public static String PREF_PROFILE_EMAIL = "PREF_PROFILE_EMAIL";
    public static String PREF_PROFILE_IMAGE_URL = "PREF_PROFILE_IMAGE_URL";
    public static String PREF_ISDELETE = "isDelete";

    public static String PREF_PROFILE_COUNTRY = "PREF_PROFILE_COUNTRY";
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
