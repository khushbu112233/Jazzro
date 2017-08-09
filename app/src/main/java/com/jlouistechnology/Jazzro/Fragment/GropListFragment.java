package com.jlouistechnology.Jazzro.Fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jlouistechnology.Jazzro.Adapter.NewGroupListAdapter;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.Model.GroupListServiceModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.GroupListFragmentLayoutBinding;
import com.jlouistechnology.Jazzro.interfaces.OnClickEditGroupListener;
import com.jlouistechnology.Jazzro.network.ApiClient;
import com.jlouistechnology.Jazzro.network.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.jlouistechnology.Jazzro.Fragment.MyConnectFragment.context;

/**
 * Created by aipxperts-ubuntu-01 on 4/8/17.
 */

public class GropListFragment extends Fragment {
    GroupListFragmentLayoutBinding mBinding;
    View rootView;
    ArrayList<GroupListDataDetailModel> griupList = new ArrayList<GroupListDataDetailModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.group_list_fragment_layout, container, false);
        rootView = mBinding.getRoot();
        context = getActivity();
        preview();
        grouplistTask();
        ((DashboardNewActivity) context).mBinding.header.imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGroupFragment fragment = new NewGroupFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

            }
        });

        mBinding.listGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return rootView;
    }

    private void preview() {
        ((DashboardNewActivity) context).SettextTxtTitle("Groups");
        ((DashboardNewActivity) context).visibilityimgright(View.VISIBLE);
        ((DashboardNewActivity) context).SetimageresourceImgright(R.mipmap.plus_contact);
    }

    private void grouplistTask() {


        if (WebService.isNetworkAvailable(getActivity())) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<GroupListServiceModel> call = apiService.groupList(Pref.getValue(getActivity(), Constants.TOKEN, ""),
                    "1", Pref.getValue(getActivity(), "total_group", ""), "label", "asc");
            call.enqueue(new Callback<GroupListServiceModel>() {
                @Override
                public void onResponse(Call<GroupListServiceModel> call, retrofit2.Response<GroupListServiceModel> response) {
                    Log.e("VVV", "111" + new Gson().toJson(response.body()));

                    if (response.body().status != 400) {
                        griupList = (response.body().data.data);
                        if (griupList.size() > 0) {
                            mBinding.listGroup.setVisibility(View.VISIBLE);
                            mBinding.txtMsg.setVisibility(View.GONE);
                        } else {
                            mBinding.listGroup.setVisibility(View.GONE);
                            mBinding.txtMsg.setVisibility(View.VISIBLE);
                        }

                        Pref.setValue(context, "selectedGroud", "");
                        Pref.setValue(context, "selectedGroup_label", "");
                        NewGroupListAdapter newGroupListAdapter = new NewGroupListAdapter(context, griupList, ((DashboardNewActivity) context).mBinding.header.txtTitleRight, "main");
                        newGroupListAdapter.onClickEdit(onClickEditGroupListener);
                        mBinding.listGroup.setAdapter(newGroupListAdapter);
                        // groupChoiceOPenDialog(griupList);

                    }

                }

                @Override
                public void onFailure(Call<GroupListServiceModel> call, Throwable t) {
                    Log.e("VVV", "Failuar : " + t.toString());
                }
            });
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        ((DashboardNewActivity) context).Set_header_visibility();
    }

    OnClickEditGroupListener onClickEditGroupListener = new OnClickEditGroupListener() {
        @Override
        public void onClick(int position) {
            Gson gson = new Gson();
            Bundle args = new Bundle();
            EditGroupFragment fragment = new EditGroupFragment();
            args.putSerializable("data", gson.toJson(griupList.get(position)));
            fragment.setArguments(args);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

        }
    };

}
