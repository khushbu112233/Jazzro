package com.jlouistechnology.Jazzro.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.jlouistechnology.Jazzro.Adapter.NewGroupListAdapter;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Interface.OnClickEditGroupListener;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.Model.GroupListServiceModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.GroupListFragmentLayoutBinding;
import com.jlouistechnology.Jazzro.network.ApiClient;
import com.jlouistechnology.Jazzro.network.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by aipxperts-ubuntu-01 on 4/8/17.
 */

public class GropListFragment extends Fragment {
    GroupListFragmentLayoutBinding mBinding;
    View rootView;
    Context context;
    ArrayList<String> SelectedGroupList = new ArrayList<>();
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
                Gson gson = new Gson();
                Bundle args = new Bundle();
                FriendsListFragment fragment = new FriendsListFragment();
                args.putSerializable("data", gson.toJson(griupList.get(i)));
                fragment.setArguments(args);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

            }
        });

        return rootView;
    }

    private void preview() {
        ((DashboardNewActivity) context).Setimagebackgroundresource(R.mipmap.contact_bar);
        ((DashboardNewActivity) context).SettextTxtTitle("Groups");
        ((DashboardNewActivity) context).visibilityimgright(View.VISIBLE);
        ((DashboardNewActivity) context).SetimageresourceImgright(R.mipmap.plus_contact);
    }

    private void grouplistTask() {

        WebService.showProgress(getActivity());
        if (WebService.isNetworkAvailable(getActivity())) {

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<GroupListServiceModel> call = apiService.groupList(Pref.getValue(getActivity(), Constants.TOKEN, ""),
                    "1", Pref.getValue(getActivity(), "total_group", ""), "label", "asc");
            call.enqueue(new Callback<GroupListServiceModel>() {
                @Override
                public void onResponse(Call<GroupListServiceModel> call, retrofit2.Response<GroupListServiceModel> response) {
                    Log.e("VVV", "111" + new Gson().toJson(response.body()));
                    WebService.dismissProgress();
                    if (response.body().status != 400) {
                        griupList = (response.body().data.data);
                        if (griupList.size() > 0) {
                            mBinding.listGroup.setVisibility(View.VISIBLE);
                            mBinding.txtMsg.setVisibility(View.GONE);
                            NewGroupListAdapter newGroupListAdapter = new NewGroupListAdapter(context, griupList, ((DashboardNewActivity) context).mBinding.header.imgLeftBack, "main", SelectedGroupList);
                            mBinding.listGroup.setAdapter(newGroupListAdapter);
                            // groupChoiceOPenDialog(griupList);
                            newGroupListAdapter.onClickEdit(onClickEditGroupListener);

                        } else {
                            mBinding.listGroup.setVisibility(View.GONE);
                            mBinding.txtMsg.setVisibility(View.VISIBLE);
                        }

                        Pref.setValue(context, "selectedGroud", "");
                        Pref.setValue(context, "selectedGroup_label", "");

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

        Utils.hideKeyboard(context);
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
// Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
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
