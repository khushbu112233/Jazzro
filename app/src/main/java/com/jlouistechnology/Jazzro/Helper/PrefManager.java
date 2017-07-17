package com.jlouistechnology.Jazzro.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aipxperts on 6/12/16.
 */
public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String PREF_IS_SYNC = "pref_is_sync";
    private String PREF_CONACTID = "pref_conact_id";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setConactID(String id) {
        editor.putString(PREF_CONACTID, id);
        editor.commit();
    }

    public String getConactId() {
        return pref.getString(PREF_CONACTID, "");
    }

    public void setIsSync(String isSync) {
        editor.putString(PREF_IS_SYNC, isSync);
        editor.commit();
    }

    public String isSync() {
        return pref.getString(PREF_IS_SYNC, "0");
    }

}
