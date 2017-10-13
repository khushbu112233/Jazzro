package com.jlouistechnology.Jazzro.Jazzro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Fragment.AddNewContactFragment;
import com.jlouistechnology.Jazzro.Fragment.ContactFragment;
import com.jlouistechnology.Jazzro.Fragment.EditContactFragment;
import com.jlouistechnology.Jazzro.Fragment.GropListFragment;
import com.jlouistechnology.Jazzro.Fragment.SettingFragment;
import com.jlouistechnology.Jazzro.Helper.CheckInternet;
import com.jlouistechnology.Jazzro.Helper.ConnectionDetector;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.DatabaseHelper;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Service.GetallPhoneContact_auto_sync;
import com.jlouistechnology.Jazzro.Service.GetallPhoneContact_auto_sync_from_middle;
import com.jlouistechnology.Jazzro.databinding.DashboardNewLayoutBinding;

/**
 * Created by aipxperts-ubuntu-01 on 3/8/17.
 */

public class DashboardNewActivity extends FragmentActivity {
    public static DashboardNewLayoutBinding mBinding;
    public static ProgressBar progressBar_sync;

    private BroadcastReceiver mReceiver;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.dashboard_new_layout);


        StatusBar();

        connectionDetector = new ConnectionDetector(DashboardNewActivity.this);
        mBinding.header.progressBarSync.setImageResource(R.mipmap.refresh);

        ContactFragment fragment = new ContactFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).commit();
        Contact_footer();


        mBinding.footer.llContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactFragment fragment = new ContactFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).commit();
                Contact_footer();
            }
        });
        mBinding.footer.llGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GropListFragment fragment = new GropListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).commit();
                Group_footer();

            }
        });
        mBinding.footer.llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingFragment fragment = new SettingFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).commit();

                Setting_footer();
            }
        });

        mBinding.header.progressBarSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardNewActivity.this, R.string.Sync_starts_in_background, Toast.LENGTH_LONG).show();
                //   ((DashboardNewActivity)context).mBinding.header.progressBarSync.setVisibility(View.VISIBLE);

                mBinding.header.progressBarSync.setImageResource(R.drawable.my_progress_interminate1);
                Log.e("first_login", "first:" + Pref.getValue(DashboardNewActivity.this, "first_login", ""));
                if (Pref.getValue(DashboardNewActivity.this, "first_login", "").equals("1")) {


                    Intent intent = new Intent(DashboardNewActivity.this, GetallPhoneContact_auto_sync.class);
                    startService(intent);


                } else {
                    Intent intent = new Intent(DashboardNewActivity.this, GetallPhoneContact_auto_sync_from_middle.class);
                    startService(intent);

                    //((DashboardActivity) getActivity()).getAllContacts(getActivity());
                    //new LoadData().execute();
                }
            }
        });
        //sync check database value.
        DatabaseHelper dh = new DatabaseHelper(DashboardNewActivity.this);
        dh.open();
        Cursor c = dh.get_sync(Pref.getValue(DashboardNewActivity.this, Constants.PREF_PROFILE_EMAIL, ""));
        c.moveToFirst();
        while (!c.isAfterLast()) {

            String is_sync = c.getString(1);
            if (is_sync.equalsIgnoreCase("1")) {
                Pref.setValue(DashboardNewActivity.this, "auto_sync", "1");

                mBinding.header.progressBarSync.setVisibility(View.VISIBLE);


                Toast.makeText(DashboardNewActivity.this, R.string.Sync_starts_in_background, Toast.LENGTH_LONG).show();
                mBinding.header.progressBarSync.setImageResource(R.drawable.my_progress_interminate1);

                //new LoadData().execute();
                if(CheckInternet.isInternetConnected(DashboardNewActivity.this)) {
                    if (Pref.getValue(DashboardNewActivity.this, "first_login", "").equals("1")) {

                        startService(new Intent(DashboardNewActivity.this, GetallPhoneContact_auto_sync.class));
                    } else {
                        startService(new Intent(DashboardNewActivity.this, GetallPhoneContact_auto_sync_from_middle.class));
                        //getAllContacts(DashboardActivity.this);
                    }
                }else {
                    connectionDetector.showToast(DashboardNewActivity.this, R.string.NO_INTERNET_CONNECTION);
                }
                break;

            } else {
                Pref.setValue(DashboardNewActivity.this, "auto_sync", "0");
            }
            c.moveToNext();
        }

        dh.close();

    }

    public void Contact_footer() {
        mBinding.footer.txtContact.setTextColor(getResources().getColor(R.color.new_black));
        mBinding.footer.txtGroups.setTextColor(getResources().getColor(R.color.black_40));
        mBinding.footer.txtSettings.setTextColor(getResources().getColor(R.color.black_40));
        mBinding.footer.imgContacts.setImageResource(R.mipmap.contact_selected);
        mBinding.footer.imgGroups.setImageResource(R.mipmap.group_unselect);
        mBinding.footer.imgSettings.setImageResource(R.mipmap.settings_unselect);

    }

    public void Group_footer() {

        mBinding.footer.txtContact.setTextColor(getResources().getColor(R.color.black_40));
        mBinding.footer.txtGroups.setTextColor(getResources().getColor(R.color.new_black));
        mBinding.footer.txtSettings.setTextColor(getResources().getColor(R.color.black_40));
        mBinding.footer.imgContacts.setImageResource(R.mipmap.contact_unselected);
        mBinding.footer.imgGroups.setImageResource(R.mipmap.group_selected);
        mBinding.footer.imgSettings.setImageResource(R.mipmap.settings_unselect);

    }

    public void Setting_footer() {
        mBinding.footer.txtContact.setTextColor(getResources().getColor(R.color.black_40));
        mBinding.footer.txtGroups.setTextColor(getResources().getColor(R.color.black_40));
        mBinding.footer.txtSettings.setTextColor(getResources().getColor(R.color.new_black));
        mBinding.footer.imgContacts.setImageResource(R.mipmap.contact_unselected);
        mBinding.footer.imgGroups.setImageResource(R.mipmap.group_unselect);
        mBinding.footer.imgSettings.setImageResource(R.mipmap.setting_selected);

    }

    public void StatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mReceiver);
    }

    /**
     * header visibility function
     *
     * @param visible
     */
    public void visibilityTxtTitle(int visible) {

        mBinding.header.txtTitle.setVisibility(visible);
    }

    public void visibilityimgleft(int visible) {

        mBinding.header.imgLeftBack.setVisibility(visible);
    }

    public void visibilityimgleftProgress(int visible) {

        mBinding.header.progressBarSync.setVisibility(visible);
    }

    public void visibilityTxtTitleleft(int visible) {
        mBinding.header.txtTitleLeft.setVisibility(visible);
    }

    public void visibilityimgright(int visible) {

        mBinding.header.imgRight.setVisibility(visible);
    }

    public void visibilityimgleftback(int visible) {
        mBinding.header.imgLeftBack.setVisibility(visible);
    }


    public void visibilityTxtTitleright(int visible) {
        mBinding.header.txtTitleRight.setVisibility(visible);
    }

    /**
     * set header title and image in all fragment
     *
     * @param text
     */
    public void SettextTxtTitle(String text) {
        mBinding.header.txtTitle.setText(text);
    }

    public void SettextTxtTitleLeft(String text) {
        mBinding.header.txtTitleLeft.setText(text);
    }

    public void SettextTxtTitleRight(String text) {
        mBinding.header.txtTitleRight.setText(text);
    }

    public static void SetimageresourceImgleftprogress() {
        mBinding.header.progressBarSync.setImageResource(R.mipmap.refresh);
    }

    public void SetimageresourceImgleft(int id) {
        mBinding.header.progressBarSync.setImageResource(id);
    }

    public void SetimageresourceImgright(int id) {
        mBinding.header.imgRight.setImageResource(id);
    }

    public void setHeaderColor(int color) {
        mBinding.header.llHeader.setBackgroundColor(color);
    }

    public void Setimagebackgroundresource(int id) {
        mBinding.header.llHeader.setBackgroundResource(id);
    }

    /**
     * visibility gone all header when fragment pause call
     */
    public void Set_header_visibility() {
        visibilityimgright(View.INVISIBLE);
        visibilityTxtTitleright(View.INVISIBLE);
        visibilityimgleft(View.INVISIBLE);
        visibilityTxtTitleleft(View.INVISIBLE);
        visibilityimgleftProgress(View.INVISIBLE);
        visibilityimgleftback(View.INVISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        Log.e("3333", "" + requestCode);

        if (requestCode == 200 && resultCode == RESULT_OK) {
            if (getCurrentFragment() instanceof AddNewContactFragment) {
                ((AddNewContactFragment) getCurrentFragment()).onActivity(data);
            }else if(getCurrentFragment() instanceof EditContactFragment){
                ((EditContactFragment)getCurrentFragment()).onActivity(data);
            }

        } else if (requestCode == 201 && resultCode == RESULT_OK) {
            if (getCurrentFragment() instanceof AddNewContactFragment) {
                ((AddNewContactFragment) getCurrentFragment()).onActivityGallery(data);
            }else if(getCurrentFragment() instanceof EditContactFragment){
                ((EditContactFragment) getCurrentFragment()).onActivityGallery(data);

            }
        }

    }

    public Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_container);
        //    Log.e("currentFragment",""+currentFragment);
        return currentFragment;

    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
// getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }*/
        Log.e("current_on_resume", "" + getCurrentFragment());
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Intent intent1 = new Intent(context, GetallPhoneContact_auto_sync.class);
                context.stopService(intent1);
                Intent intent2 = new Intent(context, GetallPhoneContact_auto_sync_from_middle.class);
                context.stopService(intent2);
                //extract our message from intent
                String msg_for_me = intent.getStringExtra("some_msg");
                //   Toast.makeText(DashboardActivity.this, R.string.Sync_completed,Toast.LENGTH_LONG).show();

                //log our message value
                // Toast.makeText(DashboardActivity.this,msg_for_me+"",Toast.LENGTH_LONG).show();
                SetimageresourceImgleftprogress();


            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }
}
