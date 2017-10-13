package com.jlouistechnology.Jazzro.Fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jlouistechnology.Jazzro.Adapter.EditGroupAdapter;
import com.jlouistechnology.Jazzro.Helper.CheckInternet;
import com.jlouistechnology.Jazzro.Helper.ConnectionDetector;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.Model.PeticularGroupContactModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.FragmentFriendsListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by aipxperts on 17/8/17.
 */

public class FriendsListFragment extends BaseFragment {
    FragmentFriendsListBinding mBinding;
    private GroupListDataDetailModel groupData;
    private ArrayList<PeticularGroupContactModel> groupList = new ArrayList<>();
    ArrayList<String> GroupContactList=new ArrayList<>();
    private ArrayList<PeticularGroupContactModel> copyGroupList = new ArrayList<>();
    ConnectionDetector connectionDetector;
    EditGroupAdapter adapter1;
    int pageNumber = 1;
    int limit = 100;
    boolean isHavingMoreData = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_friends_list, container, false);

        connectionDetector= new ConnectionDetector(getActivity());
        //Pref.setValue(mContext, "selectedGroupContact", "");
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNumber = 1;
        isHavingMoreData = true;
        mBinding.edSearch.setText("");
        groupList.clear();
        copyGroupList.clear();
        Bundle args = getArguments();
        Gson gson = new Gson();
        if (groupData == null)
            groupData = gson.fromJson((String) args.getSerializable("data"), GroupListDataDetailModel.class);

        if (!TextUtils.isEmpty(Utils.groupColor)) {
            groupData.color = Utils.groupColor;
        }
        if (!TextUtils.isEmpty(Utils.groupName)) {
            groupData.label = Utils.groupName;
        }

        ArrayList<ColorModel> colorList = new ArrayList<>();
        colorList = Utils.colorList();
        final ArrayList<ColorModel> finalColorList = colorList;

        if (!groupData.color.equalsIgnoreCase("Choose a color")) {
            for (int i = 0; i < finalColorList.size(); i++) {

                if (groupData.color.equalsIgnoreCase(finalColorList.get(i).name)) {
                    ((DashboardNewActivity) getActivity()).setHeaderColor((Color.parseColor(finalColorList.get(i).background)));
                    changeStatusbarColor((Color.parseColor(finalColorList.get(i).background)));
                }
            }
        }

        if(CheckInternet.isInternetConnected(mContext)) {

            new ExecuteTasktWO(pageNumber, limit).execute();
        }
        else
        {
            connectionDetector.showToast(getActivity(), R.string.NO_INTERNET_CONNECTION);

        }

        setuptoolbar();
        setup();

       /* mBinding.contactListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mBinding.contactListview.getLastVisiblePosition() == (adapter1.getCount() - 1)) {

                    int first = mBinding.contactListview.getFirstVisiblePosition();
                    int count = mBinding.contactListview.getChildCount();

                    if (scrollState == SCROLL_STATE_IDLE && first + count == adapter1.getCount() && isHavingMoreData) {
                        pageNumber++;
                        new ExecuteTasktWO(pageNumber, limit).execute();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/


    }

    private void setup() {


        adapter1 = new EditGroupAdapter(getActivity(), groupList, FriendsListFragment.class);
        mBinding.contactListview.setAdapter(adapter1);


        setupSearch();

        mBinding.ivPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                Bundle args = new Bundle();
                AddFriendsFragment fragment = new AddFriendsFragment();
                args.putString("name", groupData.label);
                args.putString("color", groupData.color);
                args.putString("id", groupData.id);
                Log.e("GroupContactList",""+GroupContactList);
                String json = gson.toJson(GroupContactList);
                Pref.setValue(mContext, "selectedGroupContact", json);
                fragment.setArguments(args);
                ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

            }
        });


    }

    private void setupSearch() {
        mBinding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i3, int i1, int i2) {
                groupList.clear();

                if (charSequence.toString().length() == 0) {
                    mBinding.ivcancelSearch.setVisibility(View.GONE);
                    groupList.addAll(copyGroupList);
                } else {
                    mBinding.ivcancelSearch.setVisibility(View.VISIBLE);

                    for (int i = 0; i < copyGroupList.size(); i++) {
                        if (copyGroupList.get(i).fname.toLowerCase().contains(charSequence.toString()) || copyGroupList.get(i).lname.toLowerCase().contains(charSequence.toString())) {
                            groupList.add(copyGroupList.get(i));
                        }
                    }
                }
                Collections.sort(groupList, new Comparator<PeticularGroupContactModel>() {
                    @Override
                    public int compare(PeticularGroupContactModel sp1, PeticularGroupContactModel sp2) {
                        return sp1.fname.compareTo(sp2.fname);
                    }
                });
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBinding.ivcancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupList.clear();
                mBinding.ivcancelSearch.setVisibility(View.GONE);
                groupList.addAll(copyGroupList);
                adapter1.notifyDataSetChanged();
                mBinding.edSearch.setText("");
                hideKeyboard();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        ((DashboardNewActivity) getActivity()).visibilityTxtTitleright(View.GONE);
        ((DashboardNewActivity) getActivity()).visibilityimgleft(View.GONE);
        changeStatusbarColor(getResources().getColor(R.color.colorAccent));
        ((DashboardNewActivity) getActivity()).Setimagebackgroundresource(R.mipmap.contact_bar);

        Utils.groupName = "";
        Utils.groupColor = "";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        changeStatusbarColor(getResources().getColor(R.color.colorAccent));

    }

    private void setuptoolbar() {

        ((DashboardNewActivity) getActivity()).visibilityTxtTitleleft(View.GONE);
        ((DashboardNewActivity) getActivity()).visibilityTxtTitleright(View.VISIBLE);
        ((DashboardNewActivity) getActivity()).visibilityimgleft(View.VISIBLE);
        ((DashboardNewActivity) getActivity()).SettextTxtTitle(groupData.label);
        ((DashboardNewActivity) getActivity()).SettextTxtTitleLeft("Cancel");
        ((DashboardNewActivity) getActivity()).SettextTxtTitleRight("Edit");

        ((DashboardNewActivity) getActivity()).mBinding.header.imgLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ((DashboardNewActivity) getActivity()).mBinding.header.txtTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                Bundle args = new Bundle();
                EditGroupFragment fragment = new EditGroupFragment();
                args.putSerializable("data", gson.toJson(groupData));
                fragment.setArguments(args);
                ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();


            }
        });


    }


    class ExecuteTasktWO extends AsyncTask<String, Integer, String> {
        int index;
        int length;

        public ExecuteTasktWO(int index, int length) {

            this.index = index;
            this.length = length;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            res = WebService.GetData1(WebService.GROUP + "/" + groupData.id +"/contacts", Pref.getValue(getActivity(), Constants.TOKEN, ""));
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("my_result", result + "----");

            try {
                String json = result.replaceAll("\\\\", "");
                String json1 = json.substring(1,json.length()-1);
                JSONArray jsonArray = new JSONArray(json1);
               /*json2 = json1.optJSONObject("data");
               JSONObject contectObject = json2.getJSONObject("contacts");
               JSONArray jsonArray = contectObject.getJSONArray("data");*/
                if (jsonArray.length() == 0) {
                    isHavingMoreData = false;
                    if (groupList.size() == 0) {

                        mBinding.contactListview.setVisibility(View.GONE);
                        mBinding.llNotFound.setVisibility(View.VISIBLE);
                    }
                } else {

                    isHavingMoreData = true;
                    mBinding.contactListview.setVisibility(View.VISIBLE);
                    mBinding.llNotFound.setVisibility(View.GONE);
                }

                Gson gson = new Gson();
                // String json = gson.toJson(facebookuserzeebaListModelArrayList);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject dataObject = jsonArray.getJSONObject(i);

                    PeticularGroupContactModel facebookuserzeebaListModel = gson.fromJson(dataObject.toString(), PeticularGroupContactModel.class);

                    groupList.add(facebookuserzeebaListModel);
                    Collections.sort(groupList, new Comparator<PeticularGroupContactModel>() {
                        @Override
                        public int compare(PeticularGroupContactModel sp1, PeticularGroupContactModel sp2) {
                            return sp1.fname.compareTo(sp2.fname);
                        }
                    });

                    copyGroupList.add(facebookuserzeebaListModel);
                    adapter1.notifyDataSetChanged();

                    WebService.dismissProgress();
                }
                GroupContactList.clear();
                for(int i=0;i<groupList.size();i++) {
                    if (!GroupContactList.contains(groupList.get(i).id)) {
                        GroupContactList.add(groupList.get(i).id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

                WebService.dismissProgress();
            }
        }
    }
}
