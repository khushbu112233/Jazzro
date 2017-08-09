package com.jlouistechnology.Jazzro.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.CountryToPhonePrefix;
import com.jlouistechnology.Jazzro.Helper.FieldsValidator;
import com.jlouistechnology.Jazzro.Helper.FontCustom;
import com.jlouistechnology.Jazzro.Helper.ParsedResponse;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardActivity;
import com.jlouistechnology.Jazzro.Jazzro.NewCardScannerActivity;
import com.jlouistechnology.Jazzro.Model.CardScannerModel;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.Model.GroupListServiceModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.network.ApiClient;
import com.jlouistechnology.Jazzro.network.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_right_header;

/**
 * Created by aipxperts on 12/1/17.
 */
public class NewContactFragment extends Fragment {
    private Timer timer = new Timer();
    private int REQ_COUNT = 0;
    public boolean isTimer = true;
    Context context;
    TextView txt_title;
    ImageView img_photo;
    TextView txt_cancel,txt_save;
    EditText edt_first_name, edt_last_name, edt_company,edt_phone1,edt_email1;
    TextView txt_lable;
    ArrayList<Integer> ll_phone_remove_id_list, ll_email_remove_id_list;
    FieldsValidator mValidator;
    String firstName, lastName,email,phone;
    int check_Valid_or_not = 0;
    private String jsonStringFromContact = "";
    private String updateID = "";
    ArrayList<String> group_selected_id=new ArrayList<>();
    private String countryCode = "";
    View rootView;
    TextView[] views = new TextView[NewCardScannerActivity.lst_contact_details.size()];
    TextView[] textViews_select = new TextView[NewCardScannerActivity.lst_contact_details.size()];
    TextView[] textViews_content = new TextView[NewCardScannerActivity.lst_contact_details.size()];
    LinearLayout[] linear_content = new LinearLayout[NewCardScannerActivity.lst_contact_details.size()];
    LinearLayout activity_display_content;
    ArrayList<String> new_content=new ArrayList<>();
    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.new_contact_layout, container, false);

        /**
         * this is for email validation field in
         */
        context=getActivity();
        init();
        /**
         * set font style
         */
        Preview();
        DashboardActivity.txt_menu.setImageResource(R.mipmap.home);
        DashboardActivity.txt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactGroupFragment fragment = new AddContactGroupFragment();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

            }
        });

        HashSet<String> listToSet = new HashSet<String>(NewCardScannerActivity.lst_contact_details);

        //Creating Arraylist without duplicate values
        List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
        Log.v("size_data_main", listWithoutDuplicates.size() + "--");

        for (int i = 0; i < listWithoutDuplicates.size(); i++) {

            if(listWithoutDuplicates.get(i).matches("[a-zA-Z0-9@+.: ]*"))
            {
                new_content.add(listWithoutDuplicates.get(i));
            }

        }

        if(new_content.size()>0)
        {
            txt_lable.setVisibility(View.VISIBLE);
        }
        else {
            txt_lable.setVisibility(View.GONE);
        }
        setsuggestiondata();
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_Valid_or_not = 0;
                mAddContactBtnClicked();
            }
        });
        return rootView;
    }

    public void Preview() {
        txt_title.setTypeface(FontCustom.setTitleFont(context));
        edt_phone1.setTypeface(FontCustom.setFontOpenSansLight(context));
        edt_email1.setTypeface(FontCustom.setFontOpenSansLight(context));
        edt_company.setTypeface(FontCustom.setFontOpenSansLight(context));
        edt_first_name.setTypeface(FontCustom.setFontOpenSansLight(context));
        edt_last_name.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_save.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_cancel.setTypeface(FontCustom.setFontOpenSansLight(context));


    }

    public void showdialog1(final int pos)
    {
        final Dialog mDialogRowBoardList = new Dialog(context);
        mDialogRowBoardList.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogRowBoardList.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialogRowBoardList.setContentView(R.layout.list_selection);
        mDialogRowBoardList.setCancelable(false);
        mWindow = mDialogRowBoardList.getWindow();
        mLayoutParams = mWindow.getAttributes();
        mLayoutParams.gravity = Gravity.CENTER;
        mWindow.setAttributes(mLayoutParams);

        ImageView  back=(ImageView)mDialogRowBoardList.findViewById(R.id.back);
        TextView tv_firstname=(TextView)mDialogRowBoardList.findViewById(R.id.tv_firstname);
        TextView tv_lastname=(TextView)mDialogRowBoardList.findViewById(R.id.tv_lastname);
        TextView tv_company_name=(TextView)mDialogRowBoardList.findViewById(R.id.tv_company_name);
        TextView tv_phone_number=(TextView)mDialogRowBoardList.findViewById(R.id.tv_phone_number);
        TextView tv_email=(TextView)mDialogRowBoardList.findViewById(R.id.tv_email);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDialogRowBoardList.dismiss();

            }
        });

        tv_firstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_first_name.setText(new_content.get(pos));
                resetdata(pos);
                mDialogRowBoardList.dismiss();
            }
        });

        tv_lastname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_last_name.setText(new_content.get(pos));
                resetdata(pos);
                mDialogRowBoardList.dismiss();
            }
        });
        tv_company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_company.setText(new_content.get(pos));
                resetdata(pos);
                mDialogRowBoardList.dismiss();
            }
        });
        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_email1.setText(new_content.get(pos));
                resetdata(pos);
                mDialogRowBoardList.dismiss();
            }
        });
        tv_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_phone1.setText(new_content.get(pos));
                resetdata(pos);
                mDialogRowBoardList.dismiss();
            }
        });

        mDialogRowBoardList.show();

    }


    public void setsuggestiondata()
    {
        try {
            if (new_content.size() > 0) {
                for (int i = 0; i < new_content.size(); i++) {
                    linear_content[i] = new LinearLayout(context);
                    linear_content[i].setOrientation(LinearLayout.HORIZONTAL);
                    linear_content[i].setWeightSum(2);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0.5f
                    );
                    LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.5f
                    );
                    textViews_content[i] = new TextView(context);
                    textViews_content[i].setText(new_content.get(i));
                    textViews_content[i].setPadding(5, 5, 5, 5);
                    textViews_content[i].setTypeface(FontCustom.setFontOpenSansLight(context));
                    textViews_content[i].setSingleLine(true);
                    textViews_select[i] = new TextView(context);
                    textViews_select[i].setText("Select");

                    textViews_select[i].setBackgroundColor(Color.parseColor("#203c72"));
                    textViews_select[i].setTextColor(Color.parseColor("#f3f5f4"));

                    textViews_select[i].setPadding(5, 5, 5, 5);
                    textViews_select[i].setGravity(Gravity.CENTER);
                    textViews_select[i].setHeight(60);
                    param1.setMargins(10, 10, 10, 10);
                    param.setMargins(10, 10, 10, 10);
                    views[i] = new TextView(context);
                    views[i].setHeight(2);
                    views[i].setBackgroundColor(Color.parseColor("#203c72"));

                    linear_content[i].addView(textViews_content[i]);
                    linear_content[i].addView(textViews_select[i]);
                    views[i].setLayoutParams(param);
                    textViews_content[i].setLayoutParams(param);
                    textViews_select[i].setLayoutParams(param1);
                    activity_display_content.addView(linear_content[i]);
                    activity_display_content.addView(views[i]);

                    final int finalI = i;
                    textViews_select[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showdialog1(finalI);
                        }
                    });
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resetdata(int pos)
    {
        ArrayList<String> new_data=new ArrayList<>();
        for(int i=0;i<new_content.size();i++)
        {
            if(i!=pos)
            {
                new_data.add(new_content.get(i));
            }
        }

        new_content.clear();
        new_content.addAll(new_data);
        new_data.clear();

        activity_display_content.removeAllViews();
        setsuggestiondata();
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void init()
    {

        mValidator = new FieldsValidator(getActivity());
        img_photo = (ImageView) rootView.findViewById(R.id.img_photo);
        txt_cancel = (TextView) rootView.findViewById(R.id.txt_cancel);
        edt_email1 = (EditText)rootView.findViewById(R.id.edt_email);
        edt_phone1 = (EditText)rootView.findViewById(R.id.edt_phone);
        txt_title = (TextView)rootView.findViewById(R.id.txt_title);
        txt_save = (TextView)rootView.findViewById(R.id.txt_save);
        activity_display_content = (LinearLayout)rootView.findViewById(R.id.activity_display_content);
        txt_lable=(TextView)rootView.findViewById(R.id.txt_lable);
        edt_first_name = (EditText) rootView.findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) rootView.findViewById(R.id.edt_last_name);
        edt_company = (EditText) rootView.findViewById(R.id.edt_company);
        ll_phone_remove_id_list = new ArrayList<>();
        ll_email_remove_id_list = new ArrayList<>();
        ((DashboardActivity) getActivity()).isHideLogout(false);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if(Pref.getValue(context,"Edit","").equalsIgnoreCase("1"))

        {
            txt_title.setText("Update Contact");

        }else if (Pref.getValue(context,"Edit","").equalsIgnoreCase("0"))
        {
            txt_title.setText("New Contact");
        }
        img_right_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pref.setValue(getActivity(), "is_Profile", "true");
                ContactDetailsFragment fragment = new ContactDetailsFragment();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });


        TelephonyManager tm = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phNo = tm.getLine1Number();
        String country = tm.getSimCountryIso();

        String CON = country.toUpperCase();
        CountryToPhonePrefix.addDatatomap();
        if (!TextUtils.isEmpty(CON)) {
            CountryToPhonePrefix.prefixFor(CON);
            countryCode = CountryToPhonePrefix.prefixFor(CON);
        }


        DashboardActivity.ivAdd.setVisibility(View.GONE);
        //displaySimpleDialog();

        if (!TextUtils.isEmpty(DashboardActivity.encodeString)) {
            new cardRader3(DashboardActivity.encodeString).execute();
            DashboardActivity.encodeString = "";

        }

        /**
         * this is for phone field add in the list...
         */
        Bundle arg = getArguments();
        if (arg != null && arg.containsKey("jsonString")) {
            jsonStringFromContact = arg.getString("jsonString", "");
            if (!TextUtils.isEmpty(jsonStringFromContact)) {
                JSONObject jsonObject = null;
               // AddContactGroupFragment.isCountExecute = false;
                try {
                    jsonObject = new JSONObject(jsonStringFromContact);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String phone1 = jsonObject.optString("phone1");

                updateID = jsonObject.optString("id");


                String email1 = jsonObject.optString("email1");

                String fname = jsonObject.optString("fname");
                String lname = jsonObject.optString("lname");
                String company_name = jsonObject.optString("company_name");

                edt_first_name.setText(fname);
                edt_last_name.setText(lname);
                edt_company.setText(company_name);
                edt_phone1.setText(phone1);
                edt_email1.setText(email1);

            }
        } else {
          //  AddContactGroupFragment.isCountExecute = true;
        }




    }


    private void mAddContactBtnClicked() {

        check_Valid_or_not = 0;
        if (edt_first_name.getText().toString().equals("") || TextUtils.isEmpty(edt_first_name.getText().toString().trim())) {
            check_Valid_or_not = 1;
            edt_first_name.setError(getString(R.string.please_provide_firstname));

        }

        if (edt_last_name.getText().toString().equals("") || TextUtils.isEmpty(edt_last_name.getText().toString().trim())) {
            check_Valid_or_not = 1;
            edt_last_name.setError(getString(R.string.please_provide_lastname));

        }
        if(edt_email1.getText().toString().length()>0) {
            if (!isEmailValid(edt_email1.getText().toString())) {
                check_Valid_or_not = 1;
                edt_email1.setError(getString(R.string.please_provide_valid_email));
            }
        }

        if(edt_phone1.getText().toString().length()>0) {
            if ((edt_phone1.getText().toString().length() < 10) || (edt_phone1.getText().toString().length() > 15)) {
                check_Valid_or_not = 1;
                edt_phone1.setError(getString(R.string.please_provide_valid_phone_number));

            }
        }
        int count = 0;

        if (check_Valid_or_not == 0) {
            firstName = edt_first_name.getText().toString();
            lastName = edt_last_name.getText().toString();
            email = edt_email1.getText().toString();
            phone = edt_phone1.getText().toString();
  /**
 * here when update id is empty mean new contact other wise update contact.
 */
            if (TextUtils.isEmpty(updateID)) {
                displaySimpleDialog();

            } else {

                ArrayList<String> selectedIDS = new ArrayList<String>();
                callAddnewContactAPI(selectedIDS);
            }

        }


    }

    private void displaySimpleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Would you like to add this contact in groups ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        grouplistTask();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        ArrayList<String> selectedIDS = new ArrayList<String>();
                        callAddnewContactAPI(selectedIDS); // usesr done without selected group.
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(getResources().getString(R.string.app_name));
        alert.show();
    }

    private void grouplistTask() {


        if (WebService.isNetworkAvailable(getActivity())) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<GroupListServiceModel> call = apiService.groupList(Pref.getValue(getActivity(), Constants.TOKEN, ""),
                    "1",Pref.getValue(getActivity(), "total_group", ""), "label", "asc");
            call.enqueue(new Callback<GroupListServiceModel>() {
                @Override
                public void onResponse(Call<GroupListServiceModel> call, retrofit2.Response<GroupListServiceModel> response) {
                    Log.e("VVV", new Gson().toJson(response.body()));

                    if (response.body().status != 400) {
                        ArrayList<GroupListDataDetailModel> griupList = new ArrayList<GroupListDataDetailModel>();
                        griupList = (response.body().data.data);

                        groupChoiceOPenDialog(griupList);

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

    private void groupChoiceOPenDialog(final ArrayList<GroupListDataDetailModel> dialogList) {

        final AlertDialog.Builder builderDialog = new AlertDialog.Builder(getActivity());
        builderDialog.setTitle("Select Groups");
        int count = dialogList.size();
        final boolean[] is_checked = new boolean[count];

        List<CharSequence> list = new ArrayList<CharSequence>();
        for (int i = 0; i < dialogList.size(); i++) {
            list.add(dialogList.get(i).label);
        }

        CharSequence[] cs = list.toArray(new CharSequence[list.size()]);

        builderDialog.setMultiChoiceItems(cs, is_checked,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton, boolean isChecked) {
                    }
                });

        builderDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }


                });
        builderDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        final AlertDialog alert = builderDialog.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = true;
                //Do stuff, possibly set wantToCloseDialog to true then...
                if(wantToCloseDialog) {

                    int count = 0;

                    ListView list = (alert).getListView();
                    // make selected item in the comma seprated string
                    StringBuilder stringBuilder = new StringBuilder();
                    ArrayList<String> position = new ArrayList<String>();
                    for (int i = 0; i < list.getCount(); i++) {
                        boolean checked = list.isItemChecked(i);

                        if (checked) {
                            count++;
                            if (stringBuilder.length() > 0)
                                stringBuilder.append(",");
                            stringBuilder.append(list.getItemAtPosition(i));
                            position.add("" + i);


                        }

                    }
                    if (count == 0) {
                        Toast.makeText(context, "Plaese select atleast one group", Toast.LENGTH_LONG).show();
                    }else
                    {
                        alert.dismiss();
                    }
                        /*Check string builder is empty or not. If string builder is not empty.
                          It will display on the screen.
                         */
                    if (stringBuilder.toString().trim().equals("")) {

                        stringBuilder.setLength(0);

                    } else {

                        ArrayList<String> selectedIDS = new ArrayList<String>();
                        for (int i = 0; i < position.size(); i++) {
                            selectedIDS.add(dialogList.get(Integer.parseInt(position.get(i))).id);
                        }

                        callAddnewContactAPI(selectedIDS);

                    }
                }
            }
        });

        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = true;
                if(wantToCloseDialog)
                    alert.dismiss();
            }
        });

    }

    private void callAddnewContactAPI(ArrayList<String> selectedIDS) {


        group_selected_id=selectedIDS;
        Log.e("group_selected_id",""+group_selected_id);
        if (!TextUtils.isEmpty(updateID)) {
            Pref.setValue(context,"updateID_add",updateID);
            Log.e("updateID",""+updateID);

        }
        Pref.setValue(context,"firstName_add",firstName);
        Pref.setValue(context,"lastName_add",lastName);
        Pref.setValue(context,"company_name_add",edt_company.getText().toString());
        if(phone.length()>0) {

            Pref.setValue(context,"phone1_add",countryCode + phone);
        }
        if(email.length()>0) {

            Pref.setValue(context,"email1_add",email);
        }

        if (WebService.isNetworkAvailable(getActivity())) {
            new ExecuteTask().execute();
        }else
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }
    }

    class ExecuteTask extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(context);

            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        @Override
        protected String doInBackground(String... params) {

          String res ="";/*= WebService.PostData2(group_selected_id,Pref.getValue(context,"updateID_add",""),Pref.getValue(context,"firstName_add",""),Pref.getValue(context,"lastName_add",""), Pref.getValue(context,"company_name_add",""), Pref.getValue(context,"phone1_add",""),Pref.getValue(context,"email1_add",""), WebService.SINGLE_CONTACT,Pref.getValue(context, Constants.TOKEN, ""));*/
            Log.d("nnn", " Response : " + res);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("my_result",result+"---------");

            try {
                WebService.dismissProgress();
                JSONObject json2;
                json2 = new JSONObject(result);

                Pref.setValue(context,"updateID_add","");
                Pref.setValue(context,"firstName_add","");
                Pref.setValue(context,"lastName_add","");
                Pref.setValue(context,"company_name_add","");
                Pref.setValue(context,"phone1_add","");
                Pref.setValue(context,"email1_add","");
                Pref.setValue(context,"groups_add","");

                if(Pref.getValue(context,"Edit","").equalsIgnoreCase("1"))

                {
                    Toast.makeText(getActivity(),"Contact updated successfully!", Toast.LENGTH_SHORT).show();

                }else if (Pref.getValue(context,"Edit","").equalsIgnoreCase("0"))
                {
                    Toast.makeText(getActivity(),"Contact added successfully!", Toast.LENGTH_SHORT).show();
                }


                getActivity().onBackPressed();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("scan_email",""+Pref.getValue(context,"scan_email",""));
        Log.e("scan_phone",""+Pref.getValue(context,"scan_pno",""));
        if(!(Pref.getValue(context,"scan_email","").equalsIgnoreCase("")))
        {
            edt_email1.setText(Pref.getValue(context,"scan_email",""));

        }
        if(!(Pref.getValue(context,"scan_pno","").equalsIgnoreCase(""))) {
            edt_phone1.setText(Pref.getValue(context, "scan_pno", ""));
        }
        img_right_header.setImageResource(R.mipmap.profile);



        init();
        if(Pref.getValue(context,"back_press","").equals("1"))
        {
            Pref.setValue(context,"back_press","0");
            getActivity().getSupportFragmentManager().popBackStack();
        }

    }

    public class cardRader1 extends AsyncTask<String, Integer, String> {

        private String key, res;

        public cardRader1(String key) {
            this.key = key;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(getActivity());

        }

        @Override
        protected String doInBackground(String... params) {

            res = WebService.cardReaderApi1(key);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                Log.e("Res : ", "" + res);
                String status = jsonObject.getString("status");
                String id = jsonObject.getString("id");
                if (status.equals("202")) {
                    // Toast.makeText(getActivity(), jsonObject.getString("detail"), Toast.LENGTH_SHORT).show();
                    new cardRader3(id).execute();
                } else {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public class cardRader3 extends AsyncTask<String, Integer, String> {

        private String key, res;
        ParsedResponse p;
        boolean error;
        ArrayList<CardScannerModel> arrayList = new ArrayList<>();


        public cardRader3(String key) {
            this.key = key;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // WebService.showProgress(getActivity());

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                p = WebService.cardReader2(key);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {


            error = p.error;
            if (!error) {
                arrayList = (ArrayList<CardScannerModel>) p.o;
                REQ_COUNT++;

                if (arrayList.get(0).status.equalsIgnoreCase("PROCESSING")) {
                    WebService.showProgress(getActivity());
                } else {

                    WebService.dismissProgress();
                    Toast.makeText(getActivity(), "Request not processable", Toast.LENGTH_SHORT).show();
                    isTimer = false;
                    timer.cancel();
                }


                if (REQ_COUNT == 6) {
                    Log.e("BBB", "second");
                    WebService.dismissProgress();
                    Toast.makeText(getActivity(), "Request not processable", Toast.LENGTH_SHORT).show();
                    timer.cancel();
                    isTimer = false;
                }

                if (!TextUtils.isEmpty(arrayList.get(0).fname)) {
                    edt_first_name.setText(arrayList.get(0).fname);
                }
                if (!TextUtils.isEmpty(arrayList.get(0).lastName)) {
                    edt_last_name.setText(arrayList.get(0).lastName);
                }

                if (isTimer) {
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        public void run() {

                            new cardRader3(key).execute();
                            timer.cancel();
                        }
                    }, 2000, 2000);
                }
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
