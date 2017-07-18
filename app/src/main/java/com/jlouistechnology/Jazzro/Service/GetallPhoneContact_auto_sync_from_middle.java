package com.jlouistechnology.Jazzro.Service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class GetallPhoneContact_auto_sync_from_middle extends Service {

    /** interface for clients that bind */
    IBinder mBinder;
    public static ArrayList<PhoneContact> phoneContactArrayList_remove_duplicate;
    /** indicates whether onRebind should be used */
    boolean mAllowRebind;
    public static String BROADCAST_THREAD_KEY = "broadcast_key";
    public static String EXTRAARRAYID = "getcontact";
    private ProgressBar progress;



    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // new LoadData().execute();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                String[] PROJECTION = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID,ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER ,ContactsContract.CommonDataKinds.Email.ADDRESS };
          //  ContentResolver contentResolver = getContentResolver();
            String whereName = ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + ">"+ Pref.getValue(getApplicationContext(),"last_sync_contact_id",0)+" AND (" + ContactsContract.Data.MIMETYPE + "=? )";
            String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
           // Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

                            ContentResolver cr = getContentResolver();
                            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
                            ArrayList<PhoneContact> phoneContactArrayList=new ArrayList<>();
                            phoneContactArrayList_remove_duplicate=new ArrayList<>();
                            phoneContactArrayList_remove_duplicate.clear();
                            if(cursor.getCount()>0) {
                                cursor.moveToFirst();
                                int pre_rec_id = 0;
                                if (cursor != null) {
                                    try {
                                        int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                                        int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID);
                                        int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                        // int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                                        String name = "", lname = "", number, id, email = "";
                                        while (cursor.moveToNext()) {
                                            //if(cursor.getString(nameIndex).equalsIgnoreCase(" ")) {

                                            String display_name = cursor.getString(nameIndex);
                                            if (display_name.contains(" ")) {
                                                String[] str = display_name.split(" ");
                                                name = str[0];
                                                lname = str[1];


                                                if (name.length() > 0 && lname.length() > 0) {
                                                    if (Pattern.matches("[a-zA-Z]+", name) == true && Pattern.matches("[a-zA-Z]+", lname) == true) {
                                                        number = cursor.getString(numberIndex);
                                                        id = cursor.getString(idIndex);
                                                        email = null;
                                                        //email = cursor.getString(emailIndex);
                                                        Cursor emailCursor = cr.query(
                                                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                                                null,
                                                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                                                new String[]{id}, null);

                                                        while (emailCursor.moveToNext()) {
                                                            email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                                            break;
                                                        }
                                                        emailCursor.close();

                                                        PhoneContact[] phoneContact = new PhoneContact[1];
                                                        phoneContact[0] = new PhoneContact();
                                                        phoneContact[0].setFname(name);
                                                        phoneContact[0].setLname(lname);
                                                        phoneContact[0].setUniqueId(id);
                                                        if (number != null) {
                                                            if (number.length() > 0) {
                                                                phoneContact[0].setPhone1(number);
                                                            }
                                                        }
                                                        if (email != null) {
                                                            if (Constants.isValidEmail(email)) {
                                                                if (email.length() <= 100 && email.length() > 0) {

                                                                    phoneContact[0].setEmail1(email);
                                                                }
                                                            }
                                                        }


                                                        phoneContactArrayList.add(phoneContact[0]);
                                                    }
                                                }
                                            }
                                        }

                                    } finally {

                                        cursor.close();
                                    }
                                }
                                cursor.close();

                                Collections.sort(phoneContactArrayList, new Comparator<PhoneContact>() {
                                    @Override
                                    public int compare(PhoneContact sp1, PhoneContact sp2) {
                                        return Integer.valueOf(sp1.getUniqueId()).compareTo(Integer.valueOf(sp2.getUniqueId()));

                                    }
                                });

                                for (int i = 0; i < phoneContactArrayList.size(); i++) {
                                    if (i == 0) {
                                        PhoneContact[] phoneContact = new PhoneContact[1];
                                        phoneContact[0] = new PhoneContact();
                                        phoneContact[0].setFname(phoneContactArrayList.get(i).getFname());
                                        phoneContact[0].setLname(phoneContactArrayList.get(i).getLname());
                                        phoneContact[0].setUniqueId(phoneContactArrayList.get(i).getUniqueId());
                                        phoneContact[0].setPhone1(phoneContactArrayList.get(i).getPhone1());
                                        phoneContact[0].setEmail1(phoneContactArrayList.get(i).getEmail1());
                                        phoneContactArrayList_remove_duplicate.add(phoneContact[0]);
                                    } else {

                                        if (Integer.parseInt(phoneContactArrayList.get(i).getUniqueId()) != Integer.parseInt(phoneContactArrayList.get(i - 1).getUniqueId())) {
                                            PhoneContact[] phoneContact = new PhoneContact[1];
                                            phoneContact[0] = new PhoneContact();
                                            phoneContact[0].setFname(phoneContactArrayList.get(i).getFname());
                                            phoneContact[0].setLname(phoneContactArrayList.get(i).getLname());
                                            phoneContact[0].setUniqueId(phoneContactArrayList.get(i).getUniqueId());
                                            phoneContact[0].setPhone1(phoneContactArrayList.get(i).getPhone1());
                                            phoneContact[0].setEmail1(phoneContactArrayList.get(i).getEmail1());
                                            phoneContactArrayList_remove_duplicate.add(phoneContact[0]);
                                        }
                                    }
                                }
                                Log.e("last_sync_contact_id", "------------middle" + Pref.getValue(getApplicationContext(), "last_sync_contact_id", 0));

                                Pref.setValue(getApplicationContext(), "total_rec", phoneContactArrayList_remove_duplicate.size());
                                ArrayList<PhoneContact> phoneContactArrayList_new = new ArrayList<>();
                                phoneContactArrayList_new.clear();
                                int last_rec = 0;
                                if (phoneContactArrayList_remove_duplicate.size() > 0) {

                                    for (int i = 0; i < phoneContactArrayList_remove_duplicate.size(); i++) {

                                        Log.e("size_of_array", "newdata1111111111:" + i + " " + phoneContactArrayList_remove_duplicate.get(i).getUniqueId() + " " + Pref.getValue(GetallPhoneContact_auto_sync_from_middle.this, "last_sync_contact_id", 0));


                                        if (Integer.parseInt(phoneContactArrayList_remove_duplicate.get(i).getUniqueId()) > (Pref.getValue(GetallPhoneContact_auto_sync_from_middle.this, "last_sync_contact_id", 0))) {
                                           // Log.e("size_of_array", "newdata1111111111:" + i + " " + Pref.getValue(GetallPhoneContact_auto_sync_from_middle.this, "last_sync_contact_id", 0));

                                            phoneContactArrayList_new.add(phoneContactArrayList_remove_duplicate.get(i));
                                        }
                                    }

                                }

                                if (phoneContactArrayList_remove_duplicate.size() > 0) {
                                    Pref.setValue(getApplicationContext(), "last_sync_contact_id", Integer.parseInt(phoneContactArrayList_remove_duplicate.get(phoneContactArrayList_remove_duplicate.size() - 1).getUniqueId()));
                                }

                                if (phoneContactArrayList_new.size() > 0) {

                                    JSONArray contacArray = new JSONArray();
                                    for (int i = 0; i < phoneContactArrayList_new.size(); i++) {

                                        try {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("fname", phoneContactArrayList_new.get(i).getFname());
                                            jsonObject.put("lname", phoneContactArrayList_new.get(i).getLname());
                                            jsonObject.put("uniqueId", phoneContactArrayList_new.get(i).getUniqueId());
                                            jsonObject.put("phone1", phoneContactArrayList_new.get(i).getPhone1());
                                            jsonObject.put("email1", phoneContactArrayList_new.get(i).getEmail1());

                                            contacArray.put(jsonObject);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (contacArray.length() > 0) {

                                        if (Utils.checkInternetConnection(GetallPhoneContact_auto_sync_from_middle.this)) {

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                                new ExecuteContactTask(contacArray, GetallPhoneContact_auto_sync_from_middle.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                            } else {
                                                new ExecuteContactTask(contacArray, GetallPhoneContact_auto_sync_from_middle.this).execute();
                                            }
                                        } else {
                                            Toast.makeText(GetallPhoneContact_auto_sync_from_middle.this, getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Handler mHandler = new Handler(Looper.getMainLooper());
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                Toast.makeText(GetallPhoneContact_auto_sync_from_middle.this, "No more contacts to sync!", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
                                                sendBroadcast(i);

                                            }
                                        });   }
                                    // end countiog proccess
                                } else {

                                    Handler mHandler = new Handler(Looper.getMainLooper());
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            Toast.makeText(GetallPhoneContact_auto_sync_from_middle.this, "No more contacts to sync!", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
                                            sendBroadcast(i);

                                        }
                                    });


                                }
                            }
                            else {
                                Handler mHandler = new Handler(Looper.getMainLooper());
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(GetallPhoneContact_auto_sync_from_middle.this, "No more contacts to sync!", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
                                        sendBroadcast(i);

                                    }
                                });
                            }


            }
        };
        new Thread(runnable).start();
        Log.e("size_of_array","1234");

        return START_STICKY;
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

    static class ExecuteContactTask extends AsyncTask<String, Integer, String> {
        JSONArray jsonArray = new JSONArray();
        Context context;

        public ExecuteContactTask(JSONArray jsonArray, Context context) {

            this.jsonArray = jsonArray;
            this.context=context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("size_of_array",""+jsonArray.length());
            String res = WebService.PostData1Daynamic(jsonArray, WebService.CONTACT, Pref.getValue(context, Constants.TOKEN, ""), "sync");

            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject json1 = new JSONObject(result);
                Log.e("result",result+" ");
                String status = json1.optString("status");

                      if(status.equalsIgnoreCase("200")) {

                       //   if(Pref.getValue(context, "first_login", "").equals("1")) {
                              Pref.setValue(context, "first_login", "0");
                          //}
                          Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
                          context.sendBroadcast(i);
                       //   Toast.makeText(context, R.string.Sync_completed,Toast.LENGTH_LONG).show();

                      }
                    else {

                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
