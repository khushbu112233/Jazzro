package com.jlouistechnology.Jazzro.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Jazzro.DashboardActivity;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Jazzro.LoginInNewScreenActivity;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.FragmentSettingBinding;

/**
 * Created by aipxperts on 9/8/17.
 */

public class SettingFragment extends BaseFragment {

    FragmentSettingBinding mBinding;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_setting, container, false);

        mBinding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                ((FragmentActivity)context).finish();
                                Pref.deleteAll(context);
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
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        setupToolbar();

        mBinding.ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBinding.ivToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void setupToolbar() {
        context = getActivity();
        ((DashboardNewActivity) context).mBinding.header.imgLeft.setVisibility(View.INVISIBLE);
        ((DashboardNewActivity) context).mBinding.header.txtTitleLeft.setVisibility(View.INVISIBLE);
        ((DashboardNewActivity) context).mBinding.header.txtTitleRight.setVisibility(View.INVISIBLE);
        ((DashboardNewActivity) context).mBinding.header.txtTitleRight.setVisibility(View.INVISIBLE);
        ((DashboardNewActivity) context).SettextTxtTitle("Settings");
    }
}
