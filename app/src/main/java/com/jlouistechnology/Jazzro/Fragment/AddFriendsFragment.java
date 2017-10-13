package com.jlouistechnology.Jazzro.Fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jlouistechnology.Jazzro.Adapter.ListContactAdapter;
import com.jlouistechnology.Jazzro.Helper.CheckInternet;
import com.jlouistechnology.Jazzro.Helper.ConnectionDetector;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Interface.OnClickAddContactListener;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.Model.Group;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.FragmentAddFriendsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Created by aipxperts on 17/8/17.
 */

public class AddFriendsFragment extends BaseFragment {

    FragmentAddFriendsBinding mBinding;
    private boolean isHavingData = true;
    public ArrayList<Contact> mainConatctArrayList = new ArrayList<>();
    public ArrayList<Contact> copyContactlist = new ArrayList<>();
    public ListContactAdapter adapter;
    public int pageNumber = 0;
    private int limitpage = 100;
    public String groupName;
    public String groupColor;
    public String groupId;
    boolean isSearch = false;
    ArrayList<String> id_list = new ArrayList<String>();
    ArrayList<String> GroupSelectedList = new ArrayList<>();
    ConnectionDetector connectionDetector;


    String[] id_new;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_friends, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        connectionDetector = new ConnectionDetector(getActivity());
        Bundle args = getArguments();
        Gson gson = new Gson();
        groupName = args.getString("name");
        groupColor = args.getString("color");
        groupId = args.getString("id");
        String json = Pref.getValue(mContext, "selectedGroupContact", "");
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        GroupSelectedList = gson.fromJson(json, type);

        ArrayList<ColorModel> colorList = new ArrayList<>();
        colorList = Utils.colorList();
        final ArrayList<ColorModel> finalColorList = colorList;

        for (int i = 0; i < finalColorList.size(); i++) {

            if (groupColor.equalsIgnoreCase(finalColorList.get(i).name)) {
                Pref.setValue(mContext, "add_selected_group_color", finalColorList.get(i).background);
                ((DashboardNewActivity) getActivity()).setHeaderColor((Color.parseColor(finalColorList.get(i).background)));

                changeStatusbarColor((Color.parseColor(finalColorList.get(i).background)));
            }
        }

        setup();
        setupSearch();
        setuptoolbar();
        if(CheckInternet.isInternetConnected(mContext)) {

            new ExecuteTasktWO((pageNumber), limitpage).execute();
        }else
        {
            connectionDetector.showToast(getActivity(), R.string.NO_INTERNET_CONNECTION);
        }
    }

    private void setup() {

        mainConatctArrayList.clear();
        copyContactlist.clear();
        pageNumber = 0;


        adapter = new ListContactAdapter(getActivity(), mainConatctArrayList, AddFriendsFragment.class);
        adapter.setOnClickAddContactListener(onClickAddContactListener);
        mBinding.lvContactlist.setAdapter(adapter);


        mBinding.lvContactlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                if (mBinding.lvContactlist.getLastVisiblePosition() == (adapter.getCount() - 1)) {
                    int first = mBinding.lvContactlist.getLastVisiblePosition() + 1;
                    int count = mBinding.lvContactlist.getChildCount();

                    if (scrollState == SCROLL_STATE_IDLE && first == adapter.getCount() && isHavingData) {
                        pageNumber++;
                        if (!isSearch) {
                            if(CheckInternet.isInternetConnected(mContext)) {

                                new ExecuteTasktWO((pageNumber), limitpage).execute();
                            }else
                            {
                                connectionDetector.showToast(getActivity(), R.string.NO_INTERNET_CONNECTION);

                            }

                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

    }

    private void setuptoolbar() {

        ((DashboardNewActivity) getActivity()).visibilityTxtTitleleft(View.GONE);
        ((DashboardNewActivity) getActivity()).visibilityTxtTitleright(View.VISIBLE);
        ((DashboardNewActivity) getActivity()).visibilityimgleft(View.VISIBLE);

        ((DashboardNewActivity) getActivity()).SettextTxtTitle("Add contacts to " + groupName);
        ((DashboardNewActivity) getActivity()).SettextTxtTitleLeft("Cancel");
        ((DashboardNewActivity) getActivity()).SettextTxtTitleRight("Done");

        ((DashboardNewActivity) getActivity()).mBinding.header.imgLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        ((DashboardNewActivity) getActivity()).mBinding.header.txtTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainConatctArrayList.size()>0) {
                    id_list.clear();
                    for (int i = 0; i < mainConatctArrayList.size(); i++) {
                        if (mainConatctArrayList.get(i).isSelected == true) {
                            id_list.add(mainConatctArrayList.get(i).getId());
                        }
                    }
                    if(id_list.size()>0) {
                        if(CheckInternet.isInternetConnected(mContext)) {
                            new ExecuteTask_add_contact().execute();
                        }else
                        {
                            connectionDetector.showToast(getActivity(), R.string.NO_INTERNET_CONNECTION);
                        }
                    }else
                    {
                        Toast.makeText(mContext, "Select one or more contacts to Add in group", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("id_list", "" + id_list);
                }else
                {
                    Toast.makeText(mContext,"No contacts yet",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void setupSearch() {
        mBinding.edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN) {
                    hideKeyboard();
                }

                return false;
            }
        });
        mBinding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i3, int i1, int i2) {
                mainConatctArrayList.clear();

                if (charSequence.toString().length() == 0) {
                    mBinding.ivcancelSearch.setVisibility(View.GONE);
                    mainConatctArrayList.addAll(copyContactlist);
                    isSearch = false;
                } else {
                    isSearch = true;
                    mBinding.ivcancelSearch.setVisibility(View.VISIBLE);

                    for (int i = 0; i < copyContactlist.size(); i++) {
                        if (copyContactlist.get(i).fname.toLowerCase().contains(charSequence.toString()) || copyContactlist.get(i).lname.toLowerCase().contains(charSequence.toString())) {
                            mainConatctArrayList.add(copyContactlist.get(i));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mBinding.ivcancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearch = false;
                mainConatctArrayList.clear();
                pageNumber = 0;
                isHavingData = true;
                mBinding.ivcancelSearch.setVisibility(View.GONE);
                mainConatctArrayList.addAll(copyContactlist);
                adapter.notifyDataSetChanged();
                mBinding.edSearch.setText("");
                mBinding.lvContactlist.setSelection(0);
                hideKeyboard();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        changeStatusbarColor(getResources().getColor(R.color.colorAccent));
        ((DashboardNewActivity) getActivity()).Setimagebackgroundresource(R.mipmap.contact_bar);
    }

    class ExecuteTask_add_contact extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(mContext);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = WebService.Add_contacts_toGroup(id_list, groupId, WebService.ADD_CONTACTS_TO_GROUP, Pref.getValue(mContext, Constants.TOKEN, ""));
            Log.e("res....add contact", "" + res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                WebService.dismissProgress();
                JSONObject json1 = new JSONObject(result);
                String status = json1.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    Toast.makeText(mContext, "Contacts successfully added in group", Toast.LENGTH_SHORT).show();
                    ((FragmentActivity) mContext).getSupportFragmentManager().popBackStack();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
            res = WebService.GetData1(WebService.CONTACT + "/?page=" + index + "&perpage=" + length + "&sortfield=fname" + "&sortdir=asc", Pref.getValue(getActivity(), Constants.TOKEN, ""));
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("my_result11", result + "--");
            try {
                WebService.showProgress(getActivity());
                if(result=="")
                {
                    WebService.dismissProgress();
                }
                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.optJSONObject("data");

                JSONArray jsonArray = json2.getJSONArray("data");
                Contact[] contact = new Contact[jsonArray.length()];

                if (jsonArray.length() == 0) {
                    isHavingData = false;
                } else {
                    isHavingData = true;
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    //             WebService.showProgress(context);
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String fname = jsonObject.optString("fname");
                    String id = jsonObject.optString("id");
                    String lname = jsonObject.optString("lname");
                    String streetaddress = jsonObject.optString("streetaddress");
                    String city = jsonObject.optString("city");
                    String state = jsonObject.optString("state");
                    String zipcode = jsonObject.optString("zipcode");
                    String note = jsonObject.optString("note");
                    String image = jsonObject.optString("image");
                    String image_url = jsonObject.optString("image_url");
                    String phone1 = jsonObject.optString("phone1");
                    String phone2 = jsonObject.optString("phone2");
                    String phone3 = jsonObject.optString("phone3");
                    String email1 = jsonObject.optString("email1");
                    String email2 = jsonObject.optString("email2");
                    String email3 = jsonObject.optString("email3");
                    String company_name = jsonObject.optString("company_name");
                    String company_title = jsonObject.optString("company_title");
                    String uniqueId = jsonObject.optString("uniqueId");
                    String birthday = jsonObject.optString("birthday");
                    String work_anniversary = jsonObject.optString("work_anniversary");
                    String created_at_formatted = jsonObject.optString("created_at_formatted");
                    String updated_at_formatted = jsonObject.optString("updated_at_formatted");
                    contact[i] = new Contact();
                    contact[i].setId(id);
                    contact[i].setFname(fname);
                    contact[i].setLname(lname);
                    contact[i].setStreetaddress(streetaddress);
                    contact[i].setCity(city);
                    contact[i].setState(state);
                    contact[i].setZipcode(zipcode);
                    contact[i].setNote(note);
                    contact[i].setImage(image);
                    contact[i].setImage_url(image_url);
                    contact[i].setPhone1(phone1);
                    contact[i].setPhone2(phone2);
                    contact[i].setPhone3(phone3);
                    contact[i].setEmail1(email1);
                    contact[i].setEmail2(email2);
                    contact[i].setEmail3(email3);
                    contact[i].setCompany_name(company_name);
                    contact[i].setCompany_title(company_title);
                    contact[i].setUniqueId(uniqueId);
                    contact[i].setBirthday(birthday);
                    contact[i].setWork_anniversary(work_anniversary);
                    contact[i].setCreated_at_formatted(created_at_formatted);
                    contact[i].setUpdated_at_formatted(updated_at_formatted);

                    JSONArray jsonArray1 = jsonObject.getJSONArray("group");
                    Group[] group = new Group[jsonArray1.length()];
                    ArrayList<Group> arrayList_group = new ArrayList<>();
                    Log.e("group_size",""+jsonArray1.length());
                    for (int temp = 0; temp < jsonArray1.length(); temp++) {

                        JSONObject jsonObject1 = jsonArray1.getJSONObject(temp);
                        String id_g = jsonObject1.optString("id");
                        String label_g = jsonObject1.optString("label");
                        String color_g = jsonObject1.optString("color");

                        group[temp] = new Group();
                        group[temp].setId1(id_g);
                        group[temp].setLabel(label_g);
                        group[temp].setColor1(color_g);
                        arrayList_group.add(group[temp]);
                    }
                    contact[i].setGroup_list(arrayList_group);

                    boolean isAdd = true;
                    for (int j = 0; j < GroupSelectedList.size(); j++) {
                        if (GroupSelectedList.get(j).equalsIgnoreCase(jsonObject.optString("id"))) {
                            isAdd = false;
                        }
                    }
                    if(jsonArray1.length()<5) {
                        if (isAdd) {
                            mainConatctArrayList.add(contact[i]);
                            copyContactlist.add(contact[i]);
                        }
                    }

                }
                if (mainConatctArrayList.size() > 0) {
                    mBinding.lvContactlist.setVisibility(View.VISIBLE);
                    mBinding.txtMsg.setVisibility(View.GONE);


                } else {
                    mBinding.lvContactlist.setVisibility(View.GONE);
                    mBinding.txtMsg.setVisibility(View.VISIBLE);
                }
                if (mainConatctArrayList.size() > 0) {
                    adapter.notifyDataSetChanged();
                    WebService.dismissProgress();
                } else {
                    isHavingData = false;
                    WebService.dismissProgress();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    OnClickAddContactListener onClickAddContactListener = new OnClickAddContactListener() {
        @Override
        public void onClick(int poition) {


            if (mainConatctArrayList.get(poition).isSelected) {
                mainConatctArrayList.get(poition).isSelected = false;
            } else {
                mainConatctArrayList.get(poition).isSelected = true;
            }
            adapter.notifyDataSetChanged();

        }
    };

}
