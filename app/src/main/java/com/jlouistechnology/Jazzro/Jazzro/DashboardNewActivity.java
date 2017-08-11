package com.jlouistechnology.Jazzro.Jazzro;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.jlouistechnology.Jazzro.Fragment.AddNewContactFragment;
import com.jlouistechnology.Jazzro.Fragment.ContactFragment;
import com.jlouistechnology.Jazzro.Fragment.GropListFragment;
import com.jlouistechnology.Jazzro.Fragment.SettingFragment;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.DashboardNewLayoutBinding;

/**
 * Created by aipxperts-ubuntu-01 on 3/8/17.
 */

public class DashboardNewActivity extends FragmentActivity {
    public DashboardNewLayoutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.dashboard_new_layout);






        ContactFragment fragment = new ContactFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).commit();
        mBinding.footer.txtContact.setTextColor(getResources().getColor(R.color.new_black));
        mBinding.footer.txtGroups.setTextColor(getResources().getColor(R.color.black_40));
        mBinding.footer.txtSettings.setTextColor(getResources().getColor(R.color.black_40));
        mBinding.footer.imgContacts.setImageResource(R.mipmap.contact_selected);
        mBinding.footer.imgGroups.setImageResource(R.mipmap.group_unselect);
        mBinding.footer.imgSettings.setImageResource(R.mipmap.settings_unselect);


        mBinding.footer.llContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactFragment fragment = new ContactFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).commit();
                mBinding.footer.txtContact.setTextColor(getResources().getColor(R.color.new_black));
                mBinding.footer.txtGroups.setTextColor(getResources().getColor(R.color.black_40));
                mBinding.footer.txtSettings.setTextColor(getResources().getColor(R.color.black_40));
                mBinding.footer.imgContacts.setImageResource(R.mipmap.contact_selected);
                mBinding.footer.imgGroups.setImageResource(R.mipmap.group_unselect);
                mBinding.footer.imgSettings.setImageResource(R.mipmap.settings_unselect);

            }
        });
        mBinding.footer.llGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GropListFragment fragment = new GropListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).commit();

                mBinding.footer.txtContact.setTextColor(getResources().getColor(R.color.black_40));
                mBinding.footer.txtGroups.setTextColor(getResources().getColor(R.color.new_black));
                mBinding.footer.txtSettings.setTextColor(getResources().getColor(R.color.black_40));
                mBinding.footer.imgContacts.setImageResource(R.mipmap.contact_unselected);
                mBinding.footer.imgGroups.setImageResource(R.mipmap.group_selected);
                mBinding.footer.imgSettings.setImageResource(R.mipmap.settings_unselect);


            }
        });
        mBinding.footer.llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.footer.txtContact.setTextColor(getResources().getColor(R.color.black_40));
                mBinding.footer.txtGroups.setTextColor(getResources().getColor(R.color.black_40));
                mBinding.footer.txtSettings.setTextColor(getResources().getColor(R.color.new_black));
                mBinding.footer.imgContacts.setImageResource(R.mipmap.contact_unselected);
                mBinding.footer.imgGroups.setImageResource(R.mipmap.group_unselect);
                mBinding.footer.imgSettings.setImageResource(R.mipmap.setting_selected);

                SettingFragment fragment = new SettingFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).commit();


            }
        });

    }

    /**
     * header visibility function
     * @param visible
     */
    public void visibilityTxtTitle(int visible)
    {

        mBinding.header.txtTitle.setVisibility(visible);
    }
    public void visibilityimgleft(int visible)
    {

        mBinding.header.imgLeft.setVisibility(visible);
    }
    public void visibilityTxtTitleleft(int visible)
    {
        mBinding.header.txtTitleLeft.setVisibility(visible);
    }
    public void visibilityimgright(int visible)
    {
        mBinding.header.imgRight.setVisibility(visible);
    }
    public void visibilityimgleftback(int visible)
    {
        mBinding.header.imgLeftBack.setVisibility(visible);
    }


    public void visibilityTxtTitleright(int visible)
    {
        mBinding.header.txtTitleRight.setVisibility(visible);
    }

    /**
     * set header title and image in all fragment
     * @param text
     */
    public void SettextTxtTitle(String text)
    {
        mBinding.header.txtTitle.setText(text);
    }
    public void SettextTxtTitleLeft(String text)
    {
        mBinding.header.txtTitleLeft.setText(text);
    }
    public void SettextTxtTitleRight(String text)
    {
        mBinding.header.txtTitleRight.setText(text);
    }
    public void SetimageresourceImgleft(int id)
    {
        mBinding.header.imgLeft.setImageResource(id);
    }
    public void SetimageresourceImgright(int id)
    {
        mBinding.header.imgRight.setImageResource(id);
    }
    public void Setimagebackgroundresource(int id)
    {
        mBinding.header.llHeader.setBackgroundResource(id);
    }
    /**
     * visibility gone all header when fragment pause call
     */
    public void Set_header_visibility()
    {
        visibilityimgright(View.INVISIBLE);
        visibilityTxtTitleright(View.INVISIBLE);
        visibilityimgleft(View.INVISIBLE);
        visibilityTxtTitleleft(View.INVISIBLE);
        visibilityimgleftback(View.GONE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);

        Log.e("3333", "" + requestCode);

        if(requestCode==200&&resultCode==RESULT_OK)
        {
            if(getCurrentFragment() instanceof AddNewContactFragment)
            {
                ((AddNewContactFragment) getCurrentFragment()).onActivity(data);
            }

        }else if(requestCode==201&&resultCode==RESULT_OK)
        {
            if(getCurrentFragment() instanceof  AddNewContactFragment) {
                ((AddNewContactFragment) getCurrentFragment()).onActivityGallery(data);
            }
        }

    }
    public Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_container);
        //    Log.e("currentFragment",""+currentFragment);
        return currentFragment;

    }


}
