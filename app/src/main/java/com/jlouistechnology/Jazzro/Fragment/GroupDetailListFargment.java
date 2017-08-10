package com.jlouistechnology.Jazzro.Fragment;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.jlouistechnology.Jazzro.Adapter.PeticularGrouListAdapter;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.FontCustom;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardActivity;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.Model.PeticularGroupContactModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.FragmentGroupDetailListBinding;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.edt_search;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_cancel_search;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_search;

/**
 * Created by aipxperts on 3/2/17.
 */
public class GroupDetailListFargment extends BaseFragment {


    private FragmentGroupDetailListBinding mBinding;
    private GroupListDataDetailModel mainGroupDataModel;
    private ArrayList<PeticularGroupContactModel> groupList = new ArrayList<>();
    private ArrayList<PeticularGroupContactModel> myList = new ArrayList<>();
    private PeticularGrouListAdapter adapter;
    int isPause = 0;
    int pageNumber = 1;
    int limit = 30;
    //String last_page="0";
    private boolean isHavingData = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_detail_list, container, false);

        //   Glide.with(getActivity()).load(R.mipmap.background_text).centerCrop().into(mBinding.ivTop);
        ((DashboardActivity) getActivity()).isHideLogout(true);
        isPause = 0;
        mBinding.txtName.setTypeface(FontCustom.setTitleFont(getActivity()));
        Bundle args = getArguments();
        if (args != null) {

            mainGroupDataModel = args.getParcelable("grouplistModel");
        }
        DashboardActivity.ivAdd.setVisibility(View.GONE);
        WebService.dismissProgress();
        adapter = new PeticularGrouListAdapter(getActivity(), groupList);
        mBinding.list.setAdapter(adapter);

        edt_search.setVisibility(View.VISIBLE);
        edt_search.setHint("Search Contacts");
        edt_search.setText("");
        img_search.setVisibility(View.VISIBLE);
        edt_search.setFocusableInTouchMode(true);
          edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (edt_search.getText().toString().length() > 0) {
                        setSearch();
                        Utils.hideKeyboard(getActivity());
                    }
                    return true;
                }
                return false;
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_search.getText().toString().length() > 0) {
                    setSearch();
                    Utils.hideKeyboard(getActivity());
                }
            }
        });

        if (Utils.checkInternetConnection(getActivity())) {
            pageNumber = 1;
            groupList.clear();
            myList.clear();
            new ExecuteTasktWO(pageNumber, limit).execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
        }
        //setSearch();

        mBinding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Pref.setValue(getActivity(), Constants.PREF_ID, groupList.get(position).id);
                Pref.setValue(getActivity(), "is_Profile", "false");
                ContactDetailsFragment fragment = new ContactDetailsFragment();
                ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();


            }
        });
        mBinding.list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (mBinding.list.getLastVisiblePosition() == (adapter.getCount() - 1)) {
                    int first = mBinding.list.getFirstVisiblePosition();
                    int count = mBinding.list.getChildCount();
                    if (first + count == adapter.getCount() &&isHavingData) {
                        pageNumber++;
                        //  if(pageNumber<=Integer.parseInt(last_page)) {
                        if (Utils.checkInternetConnection(getActivity())) {
                            new ExecuteTasktWO(pageNumber, limit).execute();
                            //   groupListTask(pageNumber, limit);

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        edt_search.setText("");
        edt_search.setVisibility(View.GONE);
        img_search.setVisibility(View.GONE);
        img_cancel_search.setVisibility(View.GONE);
        isPause = 1;
        // groupList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        // groupList.clear();

        edt_search.setVisibility(View.VISIBLE);
        img_search.setVisibility(View.VISIBLE);
        if (isPause == 1) {
            isPause = 0;
        }

        //setSearch();
    }

    private void setSearch() {
        groupList.clear();
        if (edt_search.getText().toString().length() > 0) {
            for (int i = 0; i < myList.size(); i++) {
                if (myList.get(i).fname.toLowerCase(Locale.getDefault()).contains(edt_search.getText().toString())) {
                    groupList.add(myList.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        }


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    img_cancel_search.setVisibility(View.VISIBLE);
                } else {
                    img_cancel_search.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(s)) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = s.length() - 1; i >= 0; i--) {
                    if (s.charAt(i) == '\n') {
                        s.delete(i, i + 1);
                        return;
                    }
                }
            }
        });

        img_cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
                img_cancel_search.setVisibility(View.GONE);
                groupList.clear();
                myList.clear();
                //  if (groupList.size() == 0) {
                pageNumber = 1;
                if (Utils.checkInternetConnection(getActivity())) {
                    // groupListTask(pageNumber,limit);
                    new ExecuteTasktWO(pageNumber, limit).execute();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
                }

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
            res = WebService.GetData1(WebService.GROUP + "/" + mainGroupDataModel.id + "/? withContacts=" + 1 + "& page=" + index + "&perpage=" + length + "&sortfield=fname" + "&sortdir=asc", Pref.getValue(getActivity(), Constants.TOKEN, ""));
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("my_result",result+"----");
            WebService.dismissProgress();
            try {
                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.optJSONObject("data");
                JSONObject contectObject = json2.getJSONObject("contacts");
                JSONArray jsonArray = contectObject.getJSONArray("data");
                if (jsonArray.length() == 0) {
                    isHavingData = false;

                }else
                {
                    isHavingData=true;
                }
                /*if(jsonArray.length()<limit)
                {
                    isHavingData=false;
                }*/
                mBinding.txtName.setText(json2.getString("label"));

                // groupList.clear();
                Gson gson = new Gson();
                // String json = gson.toJson(facebookuserzeebaListModelArrayList);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject dataObject = jsonArray.getJSONObject(i);

                    PeticularGroupContactModel facebookuserzeebaListModel = gson.fromJson(dataObject.toString(), PeticularGroupContactModel.class);

                    groupList.add(facebookuserzeebaListModel);
                    myList.add(facebookuserzeebaListModel);
                    adapter.notifyDataSetChanged();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
