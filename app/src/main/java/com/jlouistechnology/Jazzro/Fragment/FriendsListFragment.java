package com.jlouistechnology.Jazzro.Fragment;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jlouistechnology.Jazzro.Adapter.EditGroupAdapter;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.Model.PeticularGroupContactModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.FragmentFriendsListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aipxperts on 17/8/17.
 */

public class FriendsListFragment extends BaseFragment {
    FragmentFriendsListBinding mBinding;
    private GroupListDataDetailModel groupData;
    boolean isHavingData = true;
    private ArrayList<PeticularGroupContactModel> groupList = new ArrayList<>();
    private ArrayList<PeticularGroupContactModel> copyGroupList = new ArrayList<>();
    EditGroupAdapter adapter1;
    int pageNumber = 1;
    int limit = 30;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_friends_list, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        groupList.clear();
        copyGroupList.clear();
        Bundle args = getArguments();
        Gson gson = new Gson();
        groupData = gson.fromJson((String) args.getSerializable("data"), GroupListDataDetailModel.class);

        setuptoolbar();
        setup();
    }

    private void setup() {


        adapter1 = new EditGroupAdapter(getActivity(), groupList, FriendsListFragment.class);
        mBinding.contactListview.setAdapter(adapter1);

        new ExecuteTasktWO(pageNumber, limit).execute();
        setupSearch();

        mBinding.ivPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                Bundle args = new Bundle();
                AddFriendsFragment fragment = new AddFriendsFragment();
                args.putString("name", groupData.label);
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
            res = WebService.GetData1(WebService.GROUP + "/" + groupData.id + "/? withContacts=" + 1 + "& page=" + index + "&perpage=" + length + "&sortfield=fname" + "&sortdir=asc", Pref.getValue(getActivity(), Constants.TOKEN, ""));
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("my_result", result + "----");
            WebService.dismissProgress();
            try {
                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.optJSONObject("data");
                JSONObject contectObject = json2.getJSONObject("contacts");
                JSONArray jsonArray = contectObject.getJSONArray("data");
                if (jsonArray.length() == 0) {
                    isHavingData = false;
                    mBinding.contactListview.setVisibility(View.GONE);
                    mBinding.llNotFound.setVisibility(View.VISIBLE);

                } else {
                    isHavingData = true;
                    mBinding.contactListview.setVisibility(View.VISIBLE);
                    mBinding.llNotFound.setVisibility(View.GONE);
                }


                Gson gson = new Gson();
                // String json = gson.toJson(facebookuserzeebaListModelArrayList);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject dataObject = jsonArray.getJSONObject(i);

                    PeticularGroupContactModel facebookuserzeebaListModel = gson.fromJson(dataObject.toString(), PeticularGroupContactModel.class);

                    groupList.add(facebookuserzeebaListModel);
                    copyGroupList.add(facebookuserzeebaListModel);
                    adapter1.notifyDataSetChanged();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
