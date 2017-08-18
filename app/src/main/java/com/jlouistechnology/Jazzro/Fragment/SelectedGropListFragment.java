package com.jlouistechnology.Jazzro.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jlouistechnology.Jazzro.Adapter.NewGroupListAdapter;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.Group;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.Model.GroupListServiceModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.GroupListFragmentLayoutBinding;
import com.jlouistechnology.Jazzro.network.ApiClient;
import com.jlouistechnology.Jazzro.network.ApiInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by aipxperts-ubuntu-01 on 4/8/17.
 */

public class SelectedGropListFragment extends Fragment {
    GroupListFragmentLayoutBinding mBinding;
    Context context;
    View rootView;
    ArrayList<String> SelectedGroupList = new ArrayList<>();
    ArrayList<GroupListDataDetailModel> griupList = new ArrayList<GroupListDataDetailModel>();
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.group_list_fragment_layout, container, false);
        rootView = mBinding.getRoot();
        context = getActivity();
        preview();

        grouplistTask();
        mBinding.listGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("group_id",""+griupList.get(position).id);
            }
        });
        ((DashboardNewActivity)context).mBinding.header.imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGroupFragment fragment = new NewGroupFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

            }
        });
        return rootView;
    }

    private void preview() {
        ((DashboardNewActivity)context).Setimagebackgroundresource(R.mipmap.contact_bar);
        ((DashboardNewActivity)context).SettextTxtTitle("Select groups");
        ((DashboardNewActivity)context).visibilityimgright(View.VISIBLE);
        ((DashboardNewActivity)context).visibilityimgleftback(View.VISIBLE);
        ((DashboardNewActivity)context).SetimageresourceImgleft(R.mipmap.back_white);
        ((DashboardNewActivity)context).SetimageresourceImgright(R.mipmap.plus_contact);
    }
    private void grouplistTask() {

        WebService.showProgress(context);
        if (WebService.isNetworkAvailable(getActivity())) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<GroupListServiceModel> call = apiService.groupList(Pref.getValue(getActivity(), Constants.TOKEN, ""),
                    "1",Pref.getValue(getActivity(), "total_group", ""), "label", "asc");
            call.enqueue(new Callback<GroupListServiceModel>() {
                @Override
                public void onResponse(Call<GroupListServiceModel> call, retrofit2.Response<GroupListServiceModel> response) {
                    Log.e("VVV","111"+ new Gson().toJson(response.body()));

                    if (response.body().status != 400) {
                        griupList = (response.body().data.data);
                        if(griupList.size()>0)
                        {
                            mBinding.listGroup.setVisibility(View.VISIBLE);
                            mBinding.txtMsg.setVisibility(View.GONE);
                        }else
                        {
                            mBinding.listGroup.setVisibility(View.GONE);
                            mBinding.txtMsg.setVisibility(View.VISIBLE);
                        }
                        WebService.dismissProgress();

                        Pref.setValue(context, "selectedGroud", "");
                        Pref.setValue(context, "selectedGroup_label", "");
                        NewGroupListAdapter newGroupListAdapter = new NewGroupListAdapter(context,griupList,((DashboardNewActivity)context).mBinding.header.imgLeftBack,"selected",SelectedGroupList);
                        mBinding.listGroup.setAdapter(newGroupListAdapter);
                        // groupChoiceOPenDialog(griupList);

                    }

                }

                @Override
                public void onFailure(Call<GroupListServiceModel> call, Throwable t) {
                    Log.e("VVV", "Failuar : " + t.toString());
                    WebService.dismissProgress();
                }
            });
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ((DashboardNewActivity) context).Set_header_visibility();

        preview();
        Gson gson = new Gson();
        if (!Pref.getValue(context, "SelectedGroupList", "").equalsIgnoreCase("")) {
            String json = Pref.getValue(context, "SelectedGroupList", "");
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            SelectedGroupList = gson.fromJson(json, type);

            for (int i = 0; i < SelectedGroupList.size(); i++) {
                Log.e("selected", "****  " + SelectedGroupList.get(i));
            }
        }
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

                        return true;
                    }
                }
                return false;
            }
        });

    }
}
