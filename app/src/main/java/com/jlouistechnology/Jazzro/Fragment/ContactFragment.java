package com.jlouistechnology.Jazzro.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Adapter.ListContactAdapter;
import com.jlouistechnology.Jazzro.Helper.ConnectionDetector;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.Model.Group;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.ContactFragmentLayoutBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aipxperts-ubuntu-01 on 3/8/17.
 */

public class ContactFragment extends Fragment {
    ContactFragmentLayoutBinding mBinding;
    Context context;
    View rootView;
    ConnectionDetector cd;
    public static int pageNumber = 1;
    private int limitpage = 100;
    public static ListContactAdapter adapter;
    public static ArrayList<Contact> mainConatctArrayList = new ArrayList<>();
    private boolean isHavingData = true;
    boolean isPagination = true;
    int isMainLoad = 1;
    public static int storePageNumber = 1;
    public static ArrayList<Contact> copyContactlist = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.contact_fragment_layout, container, false);
        rootView = mBinding.getRoot();
        context = getActivity();
        mainConatctArrayList.clear();
        copyContactlist.clear();

        isMainLoad = 1;
        pageNumber = 1;
        preview();

        Pref.setValue(context, "selectedGroud", "");
        Pref.setValue(context, "selectedGroup_label", "");
        call_without_search_api();
        setadapterinit();

        /**
         * search set
         */

        mBinding.searchContact.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getAction() == KeyEvent.ACTION_DOWN) {
                            if (!TextUtils.isEmpty(mBinding.searchContact.getText().toString().trim())) {
                                isPagination = true;
                                isHavingData=true;
                                pageNumber = 1;
                                isMainLoad = 0;
                                mBinding.ivcancelSearch.setVisibility(View.VISIBLE);
                                mainConatctArrayList.clear();
                                adapter.notifyDataSetChanged();
                                new ExecuteTask((pageNumber), limitpage).execute(mBinding.searchContact.getText().toString().trim());
                                Utils.hideKeyboard(getActivity());
                            }
                            return true;
                        }

                        return false;
                    }


                });
        mBinding.ivcancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.searchContact.setText("");
                pageNumber = 1;
                isMainLoad = 1;
                isHavingData = true;
                isPagination = true;
                call_without_search_api();
                mainConatctArrayList.clear();
               // mainConatctArrayList.addAll(copyContactlist);
                adapter.notifyDataSetChanged();
                mBinding.ivcancelSearch.setVisibility(View.GONE);
            }
        });

        /**
         * on scroll set pagination
         */
        mBinding.listContact.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isPagination) {

                    int first = mBinding.listContact.getFirstVisiblePosition();
                    int count = mBinding.listContact.getChildCount();


                if (scrollState == SCROLL_STATE_IDLE &&first + count == adapter.getCount() && isHavingData) {
                        pageNumber++;


                        if (isMainLoad == 1) {

                            storePageNumber = pageNumber;
                            if (mBinding.listContact.getLastVisiblePosition() == (adapter.getCount() - 1)) {
                                call_without_search_api();
                            }

                        } else {

                            if(adapter.getCount()<limitpage)
                            {

                            }
                            else {
                                new ExecuteTask((pageNumber), limitpage).execute(mBinding.searchContact.getText().toString().trim());
                            }
                        }
                        Log.e("888", "page no : " + pageNumber);

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        /**
         * add new contact
         */

        ((DashboardNewActivity)context).mBinding.header.imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNewContactFragment fragment = new AddNewContactFragment();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

            }
        });

        return rootView;

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
                        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        ((DashboardNewActivity)context).finish();

                        return true;
                    }
                }
                return false;
            }
        });
    }


    private void preview() {
        cd = new ConnectionDetector(context);
        ((DashboardNewActivity)context).SettextTxtTitle("Contacts");
        ((DashboardNewActivity)context).visibilityimgright(View.VISIBLE);
        ((DashboardNewActivity)context).visibilityimgleftProgress(View.VISIBLE);
        ((DashboardNewActivity)context).SetimageresourceImgleftprogress();
        ((DashboardNewActivity)context).SetimageresourceImgright(R.mipmap.plus_contact);
        ((DashboardNewActivity)context).Setimagebackgroundresource(R.mipmap.contact_bar);

    }

    /**
     * set adapter
     *
     */

    public void setadapterinit()
    {
        adapter = new ListContactAdapter(context, mainConatctArrayList,ContactFragment.class);
        mBinding.listContact.setAdapter(adapter);

    }
    /**
     * call without search api
     */
    public void call_without_search_api()
    {
        if (cd.isConnectingToInternet()) {

            new ExecuteTasktWO((pageNumber), limitpage).execute();

        } else {
            cd.showToast(context, R.string.NO_INTERNET_CONNECTION);
        }
    }

    /**
     * call api without search
     */
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
            WebService.showProgress(context);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            res = WebService.GetData1(WebService.CONTACT + "/?page=" + index + "&perpage=" + length + "&sortfield=fname" + "&sortdir=asc", Pref.getValue(context, Constants.TOKEN, ""));
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("my_result",result+"--");
            try {
                WebService.showProgress(context);
                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.optJSONObject("data");


                String total = json2.optString("total");
                // totoalContact = total;
                String per_page = json2.optString("per_page");
                String current_page = json2.optString("current_page");
                String last_page = json2.optString("last_page");
                String from = json2.optString("from");
                String to = json2.optString("to");
                // Pref.setValue(context, "total_contact", total);
                JSONArray jsonArray = json2.getJSONArray("data");
                // Log.e("jsonArray", "" + jsonArray);

                Contact[] contact = new Contact[jsonArray.length()];

                if (jsonArray.length() == 0) {
                    isHavingData = false;
                }else
                {
                    isHavingData=true;
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
                    //  if (contact[i].getFname().trim().length() > 0) {
                    mainConatctArrayList.add(contact[i]);
                    copyContactlist.add(contact[i]);

                    // }
                }
                if(mainConatctArrayList.size()>0)
                {
                    mBinding.llContactList.setVisibility(View.VISIBLE);
                    mBinding.llNotFound.setVisibility(View.GONE);

                }else
                {
                    mBinding.llContactList.setVisibility(View.GONE);
                    mBinding.llNotFound.setVisibility(View.VISIBLE);
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

    /**
     * search api
     */
    public class ExecuteTask extends AsyncTask<String, Integer, String> {
        int index;
        int length;

        public ExecuteTask(int index, int length) {

            this.index = index;
            this.length = length;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(context);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            String query1 = params[0].toString().trim();
            res = WebService.GetData1(WebService.CONTACT + "/?query=" + query1 + "&page=" + index + "&perpage=" + length + "&sortfield=fname" + "&sortdir=asc", Pref.getValue(context, Constants.TOKEN, ""));


            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                Log.e("my_result",result+"-----");

                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.getJSONObject("data");


                String total = json2.optString("total");
                String per_page = json2.optString("per_page");
                String current_page = json2.optString("current_page");
                String last_page = json2.optString("last_page");
                String from = json2.optString("from");
                String to = json2.optString("to");
                JSONArray jsonArray = json2.getJSONArray("data");
                if (jsonArray.length() == 0) {
                    isHavingData = false;
                }else
                {
                    isHavingData=true;
                }
                if (jsonArray.length() > 0) {

                    Contact[] contact = new Contact[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {

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

                        mainConatctArrayList.add(contact[i]);


                        JSONArray jsonArray1 = jsonObject.getJSONArray("group");
                        Group[] group = new Group[jsonArray1.length()];
                        ArrayList<Group> arrayList_group = new ArrayList<>();
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

                    }
                }

                if (mainConatctArrayList.size() > 0) {
                    adapter.notifyDataSetChanged();

                    WebService.dismissProgress();

                } else {
                    Toast.makeText(context,"No result found!",Toast.LENGTH_LONG).show();
                    WebService.dismissProgress();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        ((DashboardNewActivity)context).Set_header_visibility();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((DashboardNewActivity)context).Set_header_visibility();
        preview();

        Utils.hideKeyboard(context);
        mBinding.searchContact.setText("");
    }

}
