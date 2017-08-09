package com.jlouistechnology.Jazzro.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
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
