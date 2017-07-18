package com.jlouistechnology.Jazzro.Service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Fragment.SyncNewContactProcessFragment;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardActivity;
import com.jlouistechnology.Jazzro.Model.PhoneContact;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Created by aipxperts-ubuntu-01 on 28/6/17.
 */

public class GetallPhoneContactBackgroundAsync extends Service {

    /** interface for clients that bind */
    IBinder mBinder;

    /** indicates whether onRebind should be used */
    boolean mAllowRebind;




    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return START_NOT_STICKY;
    }


    /** A client is binding to the service with bindService() */

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {

    }

    public static void callAddnewContactAPINew(ArrayList<PhoneContact> phoneContactArrayList_remove_duplicate, final Context context) {

        callAddnewContactAPI(phoneContactArrayList_remove_duplicate,context);
    }

    public static void callAddnewContactAPI(ArrayList<PhoneContact> phoneContactArrayList_remove_duplicate, final Context context) {

        JSONArray contacArray = new JSONArray();

        int page_limit=0;
        if(phoneContactArrayList_remove_duplicate.size()>100)
        {
            page_limit=100;
        }
        else {

            page_limit=phoneContactArrayList_remove_duplicate.size();
        }

        for (int i = 0; i < phoneContactArrayList_remove_duplicate.size(); i++) {

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fname", phoneContactArrayList_remove_duplicate.get(i).getFname());
                jsonObject.put("lname", phoneContactArrayList_remove_duplicate.get(i).getLname());
                jsonObject.put("uniqueId",phoneContactArrayList_remove_duplicate.get(i).getUniqueId());
                jsonObject.put("phone1", phoneContactArrayList_remove_duplicate.get(i).getPhone1());
                jsonObject.put("email1", phoneContactArrayList_remove_duplicate.get(i).getEmail1());

                contacArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(contacArray.length()>0) {

            if (Utils.checkInternetConnection(context)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new GetallPhoneContact_auto_sync.ExecuteContactTask(contacArray,context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new GetallPhoneContact_auto_sync.ExecuteContactTask(contacArray,context).execute();
                }
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(context,"No more contacts to sync!",Toast.LENGTH_LONG).show();
            Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
            context.sendBroadcast(i);
        }

        }



}
