package com.jlouistechnology.Jazzro.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Adapter.ListContactAdapter;
import com.jlouistechnology.Jazzro.Helper.ConnectionDetector;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.FontCustom;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardActivity;
import com.jlouistechnology.Jazzro.Jazzro.NewCardScannerActivity;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.Model.Group;
import com.jlouistechnology.Jazzro.Webservice.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import com.jlouistechnology.Jazzro.R;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.edt_search;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_cancel_search;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_right_header;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_search;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.ivAdd;

/**
 * Created by aipxperts on 8/12/16.
 */
public class MyConnectFragment extends Fragment implements View.OnClickListener, StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {
    public static SearchView search;
    public static ListView list_contacts;

    public static Context context;
    private static EditText edSearch;
    private TextView txtName;
    public static ListContactAdapter adapter;

    public static ArrayList<Contact> mainConatctArrayList = new ArrayList<>();

    public static ArrayList<Contact> copyContactlist = new ArrayList<>();

    public static int offset = 1;
    public static ConnectionDetector cd;

    Activity activity;
    public static int pageNumber = 1;
    public static int storePageNumber = 1;

    private int limitpage = 30;
    private boolean isHavingData = true;
    boolean isPagination = true;
    Bundle savedInstanceState;
    LinearLayout ll_main;
    int isMainLoad = 1;
    String last_page = "0";
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        rootView = inflater.inflate(R.layout.fragment_connect, container, false);
        context = getActivity();


        init();
        call_without_search_api();
        setadapterinit();


        DashboardActivity.txt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactGroupFragment fragment = new AddContactGroupFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

            }
        });
        img_right_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pref.setValue(getActivity(), "is_Profile", "true");
                ContactDetailsFragment fragment = new ContactDetailsFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, NewCardScannerActivity.class);
                startActivityForResult(intent, 111);

            }
        });


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                isPagination = true;
                isHavingData=true;
                isMainLoad = 0;
                mainConatctArrayList.clear();
                adapter.notifyDataSetChanged();
                pageNumber = 1;
                // mainConatctArrayList.clear();
                new ExecuteTask((pageNumber), limitpage).execute(query.toString().trim());


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                try {
                    if (TextUtils.isEmpty(newText)) {

                        isPagination = true;
                        Log.e("newText", "11 " + newText);

                    } else {
                        img_cancel_search.setVisibility(View.VISIBLE);
                        Log.e("newText", "222 " + newText);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!TextUtils.isEmpty(edt_search.getText().toString().trim())) {
                        isPagination = true;
                        isHavingData=true;
                        pageNumber = 1;
                        isMainLoad = 0;
                        img_cancel_search.setVisibility(View.VISIBLE);
                        mainConatctArrayList.clear();
                        adapter.notifyDataSetChanged();
                        new ExecuteTask((pageNumber), limitpage).execute(edt_search.getText().toString().trim());
                        Utils.hideKeyboard(getActivity());
                    }
                    return true;
                }
                return false;
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                isMainLoad = 1;
                edt_search.setText("");

                pageNumber = storePageNumber;
                mainConatctArrayList.clear();
                mainConatctArrayList.addAll(copyContactlist);
                adapter.notifyDataSetChanged();


                return false;
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edt_search.getText().toString().trim())) {
                    isPagination = false;
                    isHavingData=true;
                    pageNumber = 1;
                    isMainLoad = 0;
                    mainConatctArrayList.clear();
                    adapter.notifyDataSetChanged();
                    img_cancel_search.setVisibility(View.VISIBLE);
                    new ExecuteTask((pageNumber), limitpage).execute(edt_search.getText().toString().trim());

                    Utils.hideKeyboard(getActivity());
                }
            }
        });

        img_cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");

                isMainLoad = 1;
                isHavingData = true;
                isPagination = true;

                pageNumber = storePageNumber;
                mainConatctArrayList.clear();
                mainConatctArrayList.addAll(copyContactlist);
                adapter.notifyDataSetChanged();


                isPagination = true;

                img_cancel_search.setVisibility(View.GONE);
            }
        });



        list_contacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (isPagination) {

                    int first = list_contacts.getFirstVisiblePosition();
                    int count = list_contacts.getChildCount();


                    if (first + count == adapter.getCount() && isHavingData) {
                        pageNumber++;


                        if (isMainLoad == 1) {

                            storePageNumber = pageNumber;

                                call_without_search_api();


                        } else {

                            if(adapter.getCount()<limitpage)
                            {

                            }
                            else {
                                new ExecuteTask((pageNumber), limitpage).execute(edt_search.getText().toString().trim());
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


        return rootView;
    }

    /**
     * Init all widgets
     */

    public void init()
    {
        cd = new ConnectionDetector(context);
        list_contacts = (ListView) rootView.findViewById(R.id.list_contacts);
        search = (SearchView) rootView.findViewById(R.id.search);
        txtName = (TextView) rootView.findViewById(R.id.txtName);
        ll_main = (LinearLayout) rootView.findViewById(R.id.ll_main);
        txtName.setText("Contacts");
        txtName.setTypeface(FontCustom.setTitleFont(getActivity()));
        search.setQuery("", false);
        edSearch = (EditText) rootView.findViewById(R.id.edSearch);
        edt_search.setText("");
        mainConatctArrayList.clear();
        copyContactlist.clear();
        DashboardActivity.txt_menu.setImageResource(R.mipmap.home);
        ivAdd.setVisibility(View.GONE);
        edt_search.setVisibility(View.VISIBLE);
        edt_search.setHint("Search Contacts");
        img_search.setVisibility(View.VISIBLE);
        edt_search.setFocusableInTouchMode(true);
        search.setIconifiedByDefault(false);
        search.setSubmitButtonEnabled(true);
        ((DashboardActivity) getActivity()).isHideLogout(true);
        Pref.setValue(context, "offset", 0);
        offset = 1;
        pageNumber=1;

        Log.e("onCreateView", "storePageNumber:"+storePageNumber+" : "+pageNumber);

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
     * set adapter
     *
     */

    public void setadapterinit()
    {
        adapter = new ListContactAdapter(context, mainConatctArrayList);
        list_contacts.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("SearchView", "OnPause");
        edt_search.setVisibility(View.GONE);
        img_search.setVisibility(View.GONE);
        img_cancel_search.setVisibility(View.GONE);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        this.activity = context;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        header.setAlpha(1);
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
            try {
                //  WebService.showProgress(context);
                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.optJSONObject("data");


                String total = json2.optString("total");
                // totoalContact = total;
                String per_page = json2.optString("per_page");
                String current_page = json2.optString("current_page");
                last_page = json2.optString("last_page");
                String from = json2.optString("from");
                String to = json2.optString("to");
                // Pref.setValue(context, "total_contact", total);
                JSONArray jsonArray = json2.getJSONArray("data");
                // Log.e("jsonArray", "" + jsonArray);

                Contact[] contact = new Contact[jsonArray.length()];

                if (jsonArray.length() == 0) {
                    isHavingData = false;
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
                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.getJSONObject("data");


                String total = json2.optString("total");
                String per_page = json2.optString("per_page");
                String current_page = json2.optString("current_page");
                last_page = json2.optString("last_page");
                String from = json2.optString("from");
                String to = json2.optString("to");
                JSONArray jsonArray = json2.getJSONArray("data");

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

                    WebService.dismissProgress();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onresume", "storePageNumber:"+storePageNumber);
        pageNumber=1;
        isHavingData=true;
        img_right_header.setImageResource(R.mipmap.profile);
        if(isMainLoad==0) {
            if(!edt_search.getText().toString().trim().equalsIgnoreCase("")) {
                img_cancel_search.setVisibility(View.VISIBLE);
            }
        }
        edt_search.setVisibility(View.VISIBLE);
        img_search.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {

    }
 }
