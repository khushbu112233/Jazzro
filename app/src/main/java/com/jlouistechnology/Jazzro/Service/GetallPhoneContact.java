package com.jlouistechnology.Jazzro.Service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Jazzro.DashboardActivity;
import com.jlouistechnology.Jazzro.Model.PhoneContact;

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

public class GetallPhoneContact extends Service {

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
                            cursor.moveToFirst();
                            int pre_rec_id=0;
                            if (cursor != null) {
                                try {
                                    int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                                    int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID);
                                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                    // int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                                    String name="",lname="", number,id,email = "";
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
                                                    email=null;
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
                                                    Log.e("data", display_name + "--------" + name + "--" + lname+" "+number+" "+email);

                                                    phoneContactArrayList.add(phoneContact[0]);
                                                }
                                            }
                                        }
                                    }

                                }
                                finally {

                                    cursor.close();
                                }
                            }
                            cursor.close();

                            Collections.sort(phoneContactArrayList, new Comparator<PhoneContact>() {
                                @Override
                                public int compare(PhoneContact sp1, PhoneContact sp2) {
                                    return  Integer.valueOf(sp1.getUniqueId()).compareTo(Integer.valueOf(sp2.getUniqueId()));

                                }
                            });

                            for(int i=0;i<phoneContactArrayList.size();i++)
                            {
                                if(i==0)
                                {
                                    PhoneContact[] phoneContact = new PhoneContact[1];
                                    phoneContact[0] = new PhoneContact();
                                    phoneContact[0].setFname(phoneContactArrayList.get(i).getFname());
                                    phoneContact[0].setLname(phoneContactArrayList.get(i).getLname());
                                    phoneContact[0].setUniqueId(phoneContactArrayList.get(i).getUniqueId());
                                    phoneContact[0].setPhone1(phoneContactArrayList.get(i).getPhone1());
                                    phoneContact[0].setEmail1(phoneContactArrayList.get(i).getEmail1());
                                    phoneContactArrayList_remove_duplicate.add(phoneContact[0]);
                                }
                                else {

                                    if(Integer.parseInt(phoneContactArrayList.get(i).getUniqueId())!=Integer.parseInt(phoneContactArrayList.get(i-1).getUniqueId()))
                                    {
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
                            Pref.setValue(getApplicationContext(),"total_rec",phoneContactArrayList_remove_duplicate.size());
                            if(phoneContactArrayList_remove_duplicate.size()>0) {
                                Pref.setValue(getApplicationContext(), "last_sync_contact_id", Integer.parseInt(phoneContactArrayList_remove_duplicate.get(phoneContactArrayList_remove_duplicate.size() - 1).getUniqueId()));
                            }
                            Log.e("last_sync_contact_id",""+ Pref.getValue(getApplicationContext(),"last_sync_contact_id",0));

                            if(phoneContactArrayList_remove_duplicate.size()>0) {

                                for(int i=0;i<phoneContactArrayList_remove_duplicate.size();i++)
                                {
                                    Log.e("data",phoneContactArrayList_remove_duplicate.get(i).getFname()+" "+phoneContactArrayList_remove_duplicate.get(i).getLname()+" "+phoneContactArrayList_remove_duplicate.get(i).getUniqueId()+" "+phoneContactArrayList_remove_duplicate.get(i).getPhone1()+" "+phoneContactArrayList_remove_duplicate.get(i).getEmail1());
                                }

                            }

                            // end countiog proccess



            }
        };
        new Thread(runnable).start();
        Log.e("size_of_array","1234");

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



}
