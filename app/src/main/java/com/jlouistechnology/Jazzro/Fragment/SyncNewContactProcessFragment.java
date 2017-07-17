package com.jlouistechnology.Jazzro.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.DatabaseHandler;
import com.jlouistechnology.Jazzro.Helper.DatabaseHelper;
import com.jlouistechnology.Jazzro.Helper.FontCustom;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.PrefManager;
import com.jlouistechnology.Jazzro.Jazzro.DashboardActivity;
import com.jlouistechnology.Jazzro.Jazzro.LoginInNewScreenActivity;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.progressBar_sync;
import static com.jlouistechnology.Jazzro.Service.GetallPhoneContact.phoneContactArrayList_remove_duplicate;


/**
 * Created by aipxperts-ubuntu-01 on 5/5/17.
 */

public class SyncNewContactProcessFragment extends Fragment {
    View rootView;
    Context context;
    TextView txt_title,txt_sync_now,txt_contact_title;
    Switch SyncSwitch;
    DatabaseHandler db;
    SyncDetail syncDetail;
    ArrayList<String> email_array;
    ArrayList<String > is_sync_array;
    DatabaseHelper dh;
    public static  ProgressDialog ProgressDialog;
    int is_exit=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sync_new_contact_layout, container, false);
        context=getActivity();
        /**
         * initial component
         */
        init();
        /**
         * set typeface of component
         */
        preview();
        dh.open();
        Cursor c = dh.get_sync(Pref.getValue(context, Constants.PREF_PROFILE_EMAIL, ""));
        c.moveToFirst();
        while (!c.isAfterLast()) {
            is_exit = c.getCount();
            Log.e("khushbu", is_exit + "");
            c.moveToNext();
        }
        dh.close();


        DashboardActivity.txt_menu.setImageResource(R.mipmap.home);
        DashboardActivity.txt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactGroupFragment fragment = new AddContactGroupFragment();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });
        ((DashboardActivity) getActivity()).isHideLogout(false);


        /**
         * check aync is on or off from database(When app install from device remove this from database other wise change when logout and login with another account)
         */
        if(Pref.getValue(getActivity(),"auto_sync","").equalsIgnoreCase("1")){
            SyncSwitch.setChecked(true);
            SyncSwitch.setBackgroundResource(R.mipmap.sync_switch_on);

        }else{
            SyncSwitch.setChecked(false);
            SyncSwitch.setBackgroundResource(R.mipmap.sync_switch);

        }

        /**
         * sync click on and off
         */
        SyncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SyncSwitch.setBackgroundResource(R.mipmap.sync_switch_on);
                    Pref.setValue(context,"auto_sync","1");
                    DatabaseHelper dh = new DatabaseHelper(context);
                    dh.open();
                    /**
                     * here when first time login then add otherwise update database
                     */
                    if(is_exit==0) {
                        dh.insert_sync(Pref.getValue(context, Constants.PREF_PROFILE_EMAIL, ""), "1");
                    }else
                    {
                        dh.update_sync(Pref.getValue(context, Constants.PREF_PROFILE_EMAIL, ""), "1");
                    }
                    dh.close();
                }else{
                    SyncSwitch.setBackgroundResource(R.mipmap.sync_switch);
                    Pref.setValue(context,"auto_sync","0");
                    DatabaseHelper dh = new DatabaseHelper(context);
                    dh.open();
                    if(is_exit==0)
                    {
                        dh.insert_sync(Pref.getValue(context, Constants.PREF_PROFILE_EMAIL, ""),"0");
                    }else
                    {
                        dh.update_sync(Pref.getValue(context, Constants.PREF_PROFILE_EMAIL, ""),"0");
                    }
                    dh.close();

                }
            }
        });

        /**
         * Sync button click
         */
        txt_sync_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(context, R.string.Sync_starts_in_background,Toast.LENGTH_LONG).show();
                progressBar_sync.setVisibility(View.VISIBLE);

                Log.e("s", "first" + System.currentTimeMillis());
                if (Pref.getValue(context, "first_login", "").equals("1")) {


                    Intent intent=new Intent(context,GetallPhoneContact_auto_sync.class);
                    context.startService(intent);


                }
                else {
                    Intent intent=new Intent(context,GetallPhoneContact_auto_sync_from_middle.class);
                    context.startService(intent);

                    //((DashboardActivity) getActivity()).getAllContacts(getActivity());
                    //new LoadData().execute();
                }



            }
        });


        return rootView;

    }




    private void preview() {

        txt_title.setTypeface(FontCustom.setTitleFont(context));
        txt_sync_now.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_contact_title.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_sync_now.setEnabled(true);
    }

    public void init() {
        txt_title = (TextView)rootView.findViewById(R.id.txt_title);
        txt_sync_now = (TextView)rootView.findViewById(R.id.txt_sync_now);
        txt_contact_title = (TextView)rootView.findViewById(R.id.txt_contact_title);
        SyncSwitch = (Switch)rootView.findViewById(R.id.SyncSwitch);
        db = new DatabaseHandler(context);
        syncDetail = new SyncDetail(context);
        email_array = new ArrayList<>();
        is_sync_array = new ArrayList<>();
        dh = new DatabaseHelper(context);


    }

    public class LoadData extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        //declare other objects as per your need
        @Override
        protected void onPreExecute()
        {

            ProgressDialog = new ProgressDialog(context);
            ProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ProgressDialog.show();
            ProgressDialog.setIndeterminate(true);
            ProgressDialog.setCancelable(false);
            ProgressDialog.setContentView(R.layout.syncprogressdialog);
            //do initialization of required objects objects here
        };
        @Override
        protected Void doInBackground(Void... params)
        {
            DashboardActivity.isfromSync=1;

           // ((DashboardActivity) getActivity()).getAllContacts(getActivity());
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            /*if(ProgressDialog.isShowing()) {
                ProgressDialog.dismiss();
            }*/
        };
    }
}
