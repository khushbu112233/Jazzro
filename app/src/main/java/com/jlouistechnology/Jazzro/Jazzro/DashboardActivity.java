package com.jlouistechnology.Jazzro.Jazzro;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.jlouistechnology.Jazzro.Fragment.AddContactGroupFragment;
import com.jlouistechnology.Jazzro.Fragment.GroupListFragment;
import com.jlouistechnology.Jazzro.Fragment.MyConnectFragment;
import com.jlouistechnology.Jazzro.Fragment.SyncNewContactProcessFragment;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.DatabaseHandler;
import com.jlouistechnology.Jazzro.Helper.DatabaseHelper;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Model.ContactVO;
import com.jlouistechnology.Jazzro.Model.PhoneContact;
import com.jlouistechnology.Jazzro.Model.SyncDetail;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Service.GetallPhoneContact;
import com.jlouistechnology.Jazzro.Service.GetallPhoneContactBackgroundAsync;
import com.jlouistechnology.Jazzro.Service.GetallPhoneContact_auto_sync;
import com.jlouistechnology.Jazzro.Service.GetallPhoneContact_auto_sync_from_middle;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import static com.jlouistechnology.Jazzro.Service.GetallPhoneContact.phoneContactArrayList_remove_duplicate;

/**
 * Created by aipxperts on 8/12/16.
 */
public class DashboardActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public static ImageView txt_menu;
    public static ImageView img_right_header, ivLogout, ivAdd;
    public static EditText edt_search;
    public static ImageView img_search, img_cancel_search;
    public static LinearLayout ln_logout_button;
    public static SearchView search;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    TextView txt_menu1;
    Constants constants;
    public static ProgressBar progressBar_sync;
    public static int NAVIGATION_KEY = 2;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    public static String encodeString = "";
    DatabaseHandler db;
    SyncDetail syncDetail;
    ArrayList<String> email_array;
    ArrayList<String> is_sync_array;
    public static int isfromSync=0;
    private BroadcastReceiver mReceiver;
    String[] permissions = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard_layout);
        init();
        constants.StatusBar();

        Pref.setValue(DashboardActivity.this, "Edit", "0");
        Pref.setValue(DashboardActivity.this, "Editg", "0");
        Pref.setValue(DashboardActivity.this, "current", "dashboard");
        Pref.setValue(DashboardActivity.this, "back_press", "0");
        txt_menu1.setVisibility(View.GONE);
        txt_menu.setImageResource(R.mipmap.menu);
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        new ExecuteTask_get_data().execute();
        txt_menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_menu1.setVisibility(View.GONE);
                mDrawerLayout.closeDrawers();

            }
        });

        ln_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setMessage("Are you sure you want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
                                        edit().clear().apply();

                                Intent intent = new Intent(DashboardActivity.this, LoginInNewScreenActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                Pref.deleteAll(DashboardActivity.this);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle(getResources().getString(R.string.app_name));
                alert.show();


            }
        });
        //sync check database value.
        DatabaseHelper dh = new DatabaseHelper(DashboardActivity.this);
        dh.open();
        Cursor c = dh.get_sync(Pref.getValue(DashboardActivity.this, Constants.PREF_PROFILE_EMAIL, ""));
        c.moveToFirst();
        while (!c.isAfterLast()) {

            String is_sync = c.getString(1);
            if (is_sync.equalsIgnoreCase("1")) {
                Pref.setValue(DashboardActivity.this, "auto_sync", "1");

                        progressBar_sync.setVisibility(View.VISIBLE);

                        Toast.makeText(DashboardActivity.this, R.string.Sync_starts_in_background,Toast.LENGTH_LONG).show();

                        //new LoadData().execute();
                        if(Pref.getValue(DashboardActivity.this, "first_login", "").equals("1"))
                        {

                            startService(new Intent(DashboardActivity.this, GetallPhoneContact_auto_sync.class));
                        }
                        else {
                            startService(new Intent(DashboardActivity.this, GetallPhoneContact_auto_sync_from_middle.class));
                            //getAllContacts(DashboardActivity.this);
                        }
                break;

            } else {
                Pref.setValue(DashboardActivity.this, "auto_sync", "0");
            }
            c.moveToNext();
        }

        dh.close();

        AddContactGroupFragment fragment = new AddContactGroupFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

    }

    public void init(){

        constants = new Constants(DashboardActivity.this);
        db = new DatabaseHandler(DashboardActivity.this);
        syncDetail = new SyncDetail(DashboardActivity.this);
        email_array = new ArrayList<>();
        is_sync_array = new ArrayList<>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        txt_menu1 = (TextView) findViewById(R.id.txt_menu1);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        ViewGroup.LayoutParams params = mDrawerList.getLayoutParams();
        params.width = width;
        params.height = height;
        txt_menu = (ImageView) findViewById(R.id.txt_menu);
        img_right_header = (ImageView) findViewById(R.id.img_right_header);
        ivAdd = (ImageView) findViewById(R.id.ivAdd);
        ivLogout = (ImageView) findViewById(R.id.ivLogout);
        edt_search = (EditText) findViewById(R.id.edt_search);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_cancel_search = (ImageView) findViewById(R.id.img_cancle_search);
        search = (SearchView) findViewById(R.id.search);
        ln_logout_button = (LinearLayout) findViewById(R.id.ln_logout_button);
        progressBar_sync=(ProgressBar)findViewById(R.id.progressBar_sync);

    }
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(DashboardActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    Log.d("GGG", "ALL PERMISION GRANTED");
                } else {
                    Log.d("GGG", "ALL PERMISION Nnot GRANTED");
                    permisionDialog();
                    // no permissions granted.
                }
                return;
            }
        }
    }



    private void callAddnewContactAPI(ArrayList<PhoneContact> phoneContactArrayList_remove_duplicate, final Context context) {

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new ExecuteContactTask(contacArray).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new ExecuteContactTask(contacArray).execute();
            }

        }else
        {
            if(isfromSync==1)
            {
                DashboardActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "All contacts already sync.", Toast.LENGTH_SHORT).show();
                    }
                });
                //  Toast.makeText(context,"All contacts already sync",Toast.LENGTH_LONG).show();
                SyncNewContactProcessFragment.ProgressDialog.dismiss();
                SyncNewContactProcessFragment.ProgressDialog = null;

                getSupportFragmentManager().popBackStack();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            String pos = data.getStringExtra("drawer_position");
            if (pos.equals("0")) {
                if (currentFragment instanceof AddContactGroupFragment) {
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    int count = fm.getBackStackEntryCount();
                    for (int i = 0; i < count - 1; ++i) {
                        fm.popBackStackImmediate();
                    }

                    AddContactGroupFragment fragment9 = new AddContactGroupFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment9).addToBackStack(null).commit();
                }
            } else if (pos.equals("1")) {
                if (currentFragment instanceof MyConnectFragment) {
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    int count = fm.getBackStackEntryCount();
                    for (int i = 0; i < count - 1; ++i) {
                        fm.popBackStackImmediate();
                    }

                    MyConnectFragment fragment = new MyConnectFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                }
            } else if (pos.equals("2")) {
                if (currentFragment instanceof GroupListFragment) {
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    int count = fm.getBackStackEntryCount();
                    for (int i = 0; i < count - 1; ++i) {
                        fm.popBackStackImmediate();
                    }


                    GroupListFragment fragment2 = new GroupListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment2).addToBackStack(null).commit();
                }
            } /*else if (pos.equals("3")) {

                encodeString = data.getStringExtra("encodeString");
                Log.e("encodeString", encodeString);
                if (currentFragment instanceof NewContactFragment) {
                } else {

                    FragmentManager fm = getSupportFragmentManager();
                    int count = fm.getBackStackEntryCount();
                    for (int i = 0; i < count - 1; ++i) {
                        fm.popBackStackImmediate();
                    }

                    NewContactFragment fragment1 = new NewContactFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment1).addToBackStack(null).commit();
                }
                //startActivity(new Intent(DashboardActivity.this, CaptureActivity.class));
                //finish();
            }*/ /*else if (pos.equals("4")) {
                if (currentFragment instanceof NewContactFragment) {
                } else {

                    FragmentManager fm = getSupportFragmentManager();
                    int count = fm.getBackStackEntryCount();
                    for (int i = 0; i < count - 1; ++i) {
                        fm.popBackStackImmediate();
                    }

                    NewContactFragment fragment1 = new NewContactFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment1).addToBackStack(null).commit();

                   *//* NewCardScannerFragment fragment1 = new NewCardScannerFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment1).addToBackStack(null).commit();*//*
                }
            } *//*else if (pos.equals("5")) {
                if (currentFragment instanceof ContactDetailsFragment) {
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    int count = fm.getBackStackEntryCount();
                    for (int i = 0; i < count - 1; ++i) {
                        fm.popBackStackImmediate();
                    }

                    Pref.setValue(DashboardActivity.this, "is_Profile", "true");
                    ContactDetailsFragment fragment6 = new ContactDetailsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment6).addToBackStack(null).commit();
                }
            }*/ else if (pos.equals("3")) {

                SyncNewContactProcessFragment fragmentS = new SyncNewContactProcessFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentS).addToBackStack(null).commit();

            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    public void isHideLogout(boolean isHide) {
        if (isHide) {
            ivLogout.setVisibility(View.GONE);
            ln_logout_button.setVisibility(View.GONE);
        } else {
            ivLogout.setVisibility(View.GONE);
            ln_logout_button.setVisibility(View.VISIBLE);
        }
    }

    public void permisionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please grant all the permission to access this application.")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkPermissions();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        finish();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.setTitle(getResources().getString(R.string.app_name));
        alert.show();
    }
// user details

    class ExecuteTask_get_data extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(DashboardActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            String token=Pref.getValue(DashboardActivity.this, Constants.TOKEN, "");
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
                        Toast.makeText(DashboardActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                WebService.dismissProgress();
                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.optJSONObject("data");
                String fname = json2.optString("fname");
                String lname = json2.optString("lname");
                String num_groups = json2.optString("num_groups");

                Pref.setValue(DashboardActivity.this, "total_group", num_groups);

                Pref.setValue(DashboardActivity.this, "fname", fname + " " + lname);

                DashboardActivity.this.overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class ExecuteContactTask extends AsyncTask<String, Integer, String> {
        JSONArray jsonArray = new JSONArray();

        public ExecuteContactTask(JSONArray jsonArray) {

            this.jsonArray = jsonArray;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("size_of_array",""+jsonArray.length());
            String res = WebService.PostData1Daynamic(jsonArray, WebService.CONTACT, Pref.getValue(DashboardActivity.this, Constants.TOKEN, ""), "sync");

            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject json1 = new JSONObject(result);
                String status = json1.optString("status");

                if (SyncNewContactProcessFragment.ProgressDialog != null) {

                    if(status.equalsIgnoreCase("200")) {


                            SyncNewContactProcessFragment.ProgressDialog.dismiss();
                            SyncNewContactProcessFragment.ProgressDialog = null;
                            GetallPhoneContact.phoneContactArrayList_remove_duplicate.clear();
                            getSupportFragmentManager().popBackStack();
                      //  }

                    }
                    else {
                        SyncNewContactProcessFragment.ProgressDialog.dismiss();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void getAllContacts(final Context context) {

//        Log.e("rec_size",Pref.getValue(context,"total_rec",0)+" "+GetallPhoneContact.phoneContactArrayList_remove_duplicate.size()+"--"+Pref.getValue(DashboardActivity.this, "first_login", ""));
        if (Pref.getValue(DashboardActivity.this, "first_login", "").equals("1")) {


            if(GetallPhoneContact.phoneContactArrayList_remove_duplicate.size()==0) {

                Toast.makeText(DashboardActivity.this,"Please wait getting data from phone contacts!",Toast.LENGTH_LONG).show();
            }
            else {

                Pref.setValue(DashboardActivity.this, "first_login", "0");
                int page_size = phoneContactArrayList_remove_duplicate.size() / 100;
                Pref.setValue(DashboardActivity.this, "no_of_page", page_size);
                GetallPhoneContactBackgroundAsync.callAddnewContactAPINew(GetallPhoneContact.phoneContactArrayList_remove_duplicate, context);
            }

        } else {

            ContentResolver contentResolver = getContentResolver();
            String whereName = ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + ">" + Pref.getValue(DashboardActivity.this, "last_sync_contact_id", 0) + " AND (" + ContactsContract.Data.MIMETYPE + "=? )";
            String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
            Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

            ArrayList<PhoneContact> phoneContactArrayList = new ArrayList<>();
            ArrayList<PhoneContact> phoneContactArrayList_remove_duplicate1 = new ArrayList<>();
            int pre_rec_id = 0;
            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID));
                String given = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                String family = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));

                String phoneNumber = "";
                String email = "";
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (phoneCursor.moveToNext()) {
                        //  if (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        break;
                    }
                    phoneCursor.close();
                }
                Cursor emailCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);

                while (emailCursor.moveToNext()) {
                    email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    break;
                }
                emailCursor.close();
                if (given != null && family != null) {

                    if (Pattern.matches("[a-zA-Z]+", given) == true && Pattern.matches("[a-zA-Z]+", family) == true) {
                        if (pre_rec_id != Integer.parseInt(id)) {
                            PhoneContact[] phoneContact = new PhoneContact[1];
                            phoneContact[0] = new PhoneContact();
                            phoneContact[0].setFname(given);
                            phoneContact[0].setLname(family);
                            phoneContact[0].setUniqueId(id);
                            // Log.e("phoneNumber","000"+phoneNumber);
                            if (phoneNumber != null) {
                                phoneContact[0].setPhone1(phoneNumber);
                            }
                            if (email != null) {
                                if (Constants.isValidEmail(email)) {
                                    if (email.length() <= 100 && email.length() > 0) {

                                        phoneContact[0].setEmail1(email);
                                    }
                                }
                            }
                            phoneContactArrayList.add(phoneContact[0]);
                            pre_rec_id = Integer.parseInt(id);
                        }
                    }
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
                    phoneContactArrayList_remove_duplicate1.add(phoneContact[0]);
                } else {

                    if (Integer.parseInt(phoneContactArrayList.get(i).getUniqueId()) != Integer.parseInt(phoneContactArrayList.get(i - 1).getUniqueId())) {
                        PhoneContact[] phoneContact = new PhoneContact[1];
                        phoneContact[0] = new PhoneContact();
                        phoneContact[0].setFname(phoneContactArrayList.get(i).getFname());
                        phoneContact[0].setLname(phoneContactArrayList.get(i).getLname());
                        phoneContact[0].setUniqueId(phoneContactArrayList.get(i).getUniqueId());
                        phoneContact[0].setPhone1(phoneContactArrayList.get(i).getPhone1());
                        phoneContact[0].setEmail1(phoneContactArrayList.get(i).getEmail1());
                        phoneContactArrayList_remove_duplicate1.add(phoneContact[0]);
                    }
                }
            }
            if (phoneContactArrayList_remove_duplicate1.size() > 0) {
                Pref.setValue(DashboardActivity.this, "last_sync_contact_id", Integer.parseInt(phoneContactArrayList_remove_duplicate1.get(phoneContactArrayList_remove_duplicate1.size() - 1).getUniqueId()));
            }
            //  Log.e("last_sync_contact_id", "" + Pref.getValue(DashboardActivity.this, "last_sync_contact_id", 0));
            // end countiog proccess

            Log.e("size_of_array", "" + phoneContactArrayList_remove_duplicate1.size());

            if (phoneContactArrayList_remove_duplicate1.size() > 0) {
                GetallPhoneContactBackgroundAsync.callAddnewContactAPINew(phoneContactArrayList_remove_duplicate1, context);
            } else {
                DashboardActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(DashboardActivity.this,"No more contacts to sync!",Toast.LENGTH_LONG).show();

                        Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
                        sendBroadcast(i);

                    }
                });
            }
        }
        // Log.e("get time",""+System.currentTimeMillis());

    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Intent intent1=new Intent(context,GetallPhoneContact_auto_sync.class);
                context.stopService(intent1);
                Intent intent2=new Intent(context,GetallPhoneContact_auto_sync_from_middle.class);
                context.stopService(intent2);
                //extract our message from intent
                String msg_for_me = intent.getStringExtra("some_msg");
             //   Toast.makeText(DashboardActivity.this, R.string.Sync_completed,Toast.LENGTH_LONG).show();

                //log our message value
              // Toast.makeText(DashboardActivity.this,msg_for_me+"",Toast.LENGTH_LONG).show();
                progressBar_sync.setVisibility(View.GONE);


            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();

        this.unregisterReceiver(mReceiver);
    }

    public class LoadData extends AsyncTask<Void, Void, Void> {
        ProgressDialog ProgressDialog;
        @Override
        protected void onPreExecute()
        {

            ProgressDialog = new ProgressDialog(DashboardActivity.this);
            ProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ProgressDialog.show();
            ProgressDialog.setIndeterminate(true);
            ProgressDialog.setCancelable(false);
            ProgressDialog.setContentView(R.layout.syncprogressdialog);
        };
        @Override
        protected Void doInBackground(Void... params)
        {
           // getAllContacts(DashboardActivity.this);
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if(ProgressDialog.isShowing()) {
                ProgressDialog.dismiss();
            }
        };
    }
   /* public class MyReceiver extends BroadcastReceiver {

        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Implement code here to be performed when
            // broadcast is detected
            Log.e("action",""+intent.getAction());
            Log.e("list",""+intent.getStringArrayListExtra("getcontact").size());
        }
    }*/


}