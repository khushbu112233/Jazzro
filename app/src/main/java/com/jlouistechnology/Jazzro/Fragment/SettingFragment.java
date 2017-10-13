package com.jlouistechnology.Jazzro.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Helper.CheckInternet;
import com.jlouistechnology.Jazzro.Helper.ConnectionDetector;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.DatabaseHelper;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Jazzro.LoginInNewScreenActivity;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Service.GetallPhoneContact_auto_sync;
import com.jlouistechnology.Jazzro.Service.GetallPhoneContact_auto_sync_from_middle;
import com.jlouistechnology.Jazzro.databinding.FragmentSettingBinding;
import com.squareup.picasso.Picasso;

/**
 * Created by aipxperts on 9/8/17.
 */

public class SettingFragment extends BaseFragment {

    FragmentSettingBinding mBinding;
    Context context;

    DatabaseHelper dh;
    int is_exit = 0;
    ConnectionDetector connectionDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_setting, container, false);


        context = getActivity();
        connectionDetector = new ConnectionDetector(context);
        dh = new DatabaseHelper(context);

        dh.open();
        Cursor c = dh.get_sync(Pref.getValue(context, Constants.PREF_PROFILE_EMAIL, ""));
        c.moveToFirst();
        while (!c.isAfterLast()) {
            is_exit = c.getCount();
            Log.e("khushbu", is_exit + "");
            c.moveToNext();
        }

        Picasso.with(context).load(R.mipmap.profile_image).into(mBinding.ivProfile);

        dh.close();

        if (Pref.getValue(getActivity(), "auto_sync", "").equalsIgnoreCase("1")) {
            mBinding.ivToggle.setChecked(true);
            mBinding.ivToggle.setBackgroundResource(R.mipmap.toggel);

        } else {
            mBinding.ivToggle.setChecked(false);
            mBinding.ivToggle.setBackgroundResource(R.mipmap.toggle_off);

        }
        ;

        mBinding.txtName.setText(Pref.getValue(getActivity(), "fname", ""));
        mBinding.txtPhoneNumber.setText(Pref.getValue(getActivity(), "phone", ""));


        mBinding.ivToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    mBinding.ivToggle.setBackgroundResource(R.mipmap.toggel);
                    Pref.setValue(context, "auto_sync", "1");
                    DatabaseHelper dh = new DatabaseHelper(context);
                    dh.open();
                    /**
                     * here when first time login then add otherwise update database
                     */
                    if (is_exit == 0) {
                        dh.insert_sync(Pref.getValue(context, Constants.PREF_PROFILE_EMAIL, ""), "1");
                    } else {
                        dh.update_sync(Pref.getValue(context, Constants.PREF_PROFILE_EMAIL, ""), "1");
                    }
                    dh.close();
                } else {
                    mBinding.ivToggle.setBackgroundResource(R.mipmap.toggle_off);
                    Pref.setValue(context, "auto_sync", "0");
                    DatabaseHelper dh = new DatabaseHelper(context);
                    dh.open();
                    if (is_exit == 0) {
                        dh.insert_sync(Pref.getValue(context, Constants.PREF_PROFILE_EMAIL, ""), "0");
                    } else {
                        dh.update_sync(Pref.getValue(context, Constants.PREF_PROFILE_EMAIL, ""), "0");
                    }
                    dh.close();

                }
            }
        });

        mBinding.llRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CheckInternet.isInternetConnected(mContext)) {
                    Toast.makeText(context, R.string.Sync_starts_in_background, Toast.LENGTH_LONG).show();
                    //   ((DashboardNewActivity)context).mBinding.header.progressBarSync.setVisibility(View.VISIBLE);
                    ((DashboardNewActivity) context).mBinding.header.progressBarSync.setImageResource(R.drawable.my_progress_interminate1);

                    Log.e("s", "first" + System.currentTimeMillis());
                    if (Pref.getValue(context, "first_login", "").equals("1")) {
                        Intent intent = new Intent(context, GetallPhoneContact_auto_sync.class);
                        context.startService(intent);

                    } else {
                        Intent intent = new Intent(context, GetallPhoneContact_auto_sync_from_middle.class);
                        context.startService(intent);

                        //((DashboardActivity) getActivity()).getAllContacts(getActivity());
                        //new LoadData().execute();
                    }
                }else
                {
                    connectionDetector.showToast(getActivity(), R.string.NO_INTERNET_CONNECTION);
                }
            }
        });

        mBinding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.llLogout.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                PreferenceManager.getDefaultSharedPreferences(context).
                                        edit().clear().apply();

                                Intent intent = new Intent(context, LoginInNewScreenActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                ((FragmentActivity) context).finish();
                                Pref.deleteAll(context);
                                mBinding.llLogout.setEnabled(true);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                mBinding.llLogout.setEnabled(true);
                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle(getResources().getString(R.string.app_name));
                alert.show();
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((DashboardNewActivity) context).Set_header_visibility();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
// Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                 /*       FragmentManager fm = getActivity().getSupportFragmentManager();
                        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        ((DashboardNewActivity) context).Contact_footer();
                        ContactFragment fragment = new ContactFragment();
                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();
                      */
                        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        ((DashboardNewActivity) context).Contact_footer();
                        ContactFragment fragment = new ContactFragment();
                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();


                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        ((DashboardNewActivity) context).Set_header_visibility();
        setupToolbar();


    }

    private void setupToolbar() {

        ((DashboardNewActivity) context).Setimagebackgroundresource(R.mipmap.contact_bar);
        ((DashboardNewActivity) context).SettextTxtTitle("Settings");
    }
}