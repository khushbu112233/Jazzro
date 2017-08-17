package com.jlouistechnology.Jazzro.Fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jlouistechnology.Jazzro.Adapter.SelectedGroupDetailListAdapter;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.Model.Group;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.Widget.TextView_Regular;
import com.jlouistechnology.Jazzro.databinding.DetailContactLayoutBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aipxperts-ubuntu-01 on 9/8/17.
 */

public class DetailContactFragment extends Fragment {

    Context context;
    DetailContactLayoutBinding mBinding;
    View rootView;
    ArrayList<Group> arrayList_group = new ArrayList<>();
    ArrayList<Contact> ContactArrayList = new ArrayList<>();
    ArrayList<String> value_list = new ArrayList<>();
    ArrayList<String> color_value_list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.detail_contact_layout, container, false);
        rootView = mBinding.getRoot();
        context = getActivity();
        preview();

        ((DashboardNewActivity)context).mBinding.header.txtTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditContactFragment fragment = new EditContactFragment();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

            }
        });
        ((DashboardNewActivity)context).mBinding.header.imgLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardNewActivity) context).getSupportFragmentManager().popBackStack();
            }
        });
        arrayList_group.clear();
        return rootView;
    }

    private void preview() {
        ((DashboardNewActivity)context).visibilityimgleftback(View.VISIBLE);
        ((DashboardNewActivity)context).visibilityTxtTitleright(View.VISIBLE);
        ((DashboardNewActivity)context).SettextTxtTitleRight("Edit");
        ((DashboardNewActivity)context).SetimageresourceImgleft(R.mipmap.back_white);
        ((DashboardNewActivity)context).Setimagebackgroundresource(R.mipmap.detail_bar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DashboardNewActivity)context).Set_header_visibility();
    }

    class ExecuteTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(context);
        }

        @Override
        protected String doInBackground(String... params) {

            String res = WebService.GetData1(WebService.CONTACT + "/" + Pref.getValue(context,"Detail_id",""), Pref.getValue(context, Constants.TOKEN, ""));

            Log.e("res....", "" + res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            String jsonString="";
            try {
                JSONObject jsonObject1 = new JSONObject(result);
                if(jsonObject1.optString("status").equalsIgnoreCase("400")) {
                    Toast.makeText(getActivity(),"Data is not found.",Toast.LENGTH_LONG).show();

                }else if(jsonObject1.optJSONObject("data").length()>0)
                {
                    ContactArrayList.clear();
                    arrayList_group.clear();
                    color_value_list.clear();
                    value_list.clear();

                    JSONObject jsonObject;
                    jsonObject = jsonObject1.getJSONObject("data");

                    jsonString = jsonObject.toString();

                    Contact[] contact = new Contact[1];

                    String fname = jsonObject.optString("fname");
                    String id = jsonObject.optString("id");
                    String lname = jsonObject.optString("lname");
                    String streetaddress = jsonObject.optString("streetaddress");
                    String city = jsonObject.optString("city");
                    String state = jsonObject.optString("state");
                    String zipcode = jsonObject.optString("zipcode");
                    String note = jsonObject.optString("note");
                    String image_url = jsonObject.optString("image_url");
                    final String phone1 = jsonObject.optString("phone1");
                    final String phone2 = jsonObject.optString("phone2");
                    final String phone3 = jsonObject.optString("phone3");
                    final String email1 = jsonObject.optString("email1");
                    final String email2 = jsonObject.optString("email2");
                    final String email3 = jsonObject.optString("email3");
                    String txt_company_detail = jsonObject.optString("company_name");
                    String company_title = jsonObject.optString("company_title");
                    String uniqueId = jsonObject.optString("uniqueId");
                    String birthday = jsonObject.optString("birthday");
                    String work_anniversary = jsonObject.optString("work_anniversary");
                    String created_at_formatted = jsonObject.optString("created_at_formatted");
                    String updated_at_formatted = jsonObject.optString("updated_at_formatted");
                    contact[0] = new Contact();
                    contact[0].setId(id);
                    contact[0].setFname(fname);
                    contact[0].setLname(lname);
                    contact[0].setStreetaddress(streetaddress);
                    contact[0].setCity(city);
                    contact[0].setState(state);
                    contact[0].setZipcode(zipcode);
                    contact[0].setNote(note);
                    contact[0].setImage_url(image_url);
                    contact[0].setPhone1(phone1);
                    contact[0].setPhone2(phone2);
                    contact[0].setPhone3(phone3);
                    contact[0].setEmail1(email1);
                    contact[0].setEmail2(email2);
                    contact[0].setEmail3(email3);
                    contact[0].setCompany_title(company_title);
                    contact[0].setUniqueId(uniqueId);
                    contact[0].setBirthday(birthday);
                    contact[0].setWork_anniversary(work_anniversary);
                    contact[0].setCreated_at_formatted(created_at_formatted);
                    contact[0].setUpdated_at_formatted(updated_at_formatted);


                    JSONArray jsonArray1 = jsonObject.getJSONArray("group");
                    Group[] group = new Group[jsonArray1.length()];
                    for (int temp = 0; temp < jsonArray1.length(); temp++) {

                        JSONObject jsonObject2 = jsonArray1.getJSONObject(temp);
                        String id_g = jsonObject2.optString("id");
                        String label_g = jsonObject2.optString("label");
                        String color_g = jsonObject2.optString("color");

                        group[temp] = new Group();
                        group[temp].setId1(id_g);
                        group[temp].setLabel(label_g);
                        group[temp].setColor1(color_g);
                        arrayList_group.add(group[temp]);
                    }
                    contact[0].setGroup_list(arrayList_group);
                    ContactArrayList.add(contact[0]);
                    Gson gson=new Gson();
                    String json = gson.toJson(ContactArrayList);
                    Pref.setValue(context,"ContactArrayList",json);
                    // Pref.setValue(getActivity(), Constants.PREF_EMAIL, email1);
                    //  Pref.setValue(getActivity(),Constants.PREF_PHONE,phone1);
                    /**
                     * arraylist have value then display groups
                     */
                    if(arrayList_group.size()>0)
                    {
                        mBinding.llGroups.setVisibility(View.VISIBLE);
                        mBinding.txtGroup.setText("Groups");
                        final ArrayList<String> color_selected = new ArrayList<>();
                        String group_name="";

                        for (int i=0;i<arrayList_group.size();i++)
                        {
                            value_list.add(arrayList_group.get(i).getLabel());
                            color_value_list.add(arrayList_group.get(i).getColor1());

                            if(i==0)
                            {
                                color_selected.add(arrayList_group.get(i).getColor1());
                                group_name=Utils.capitalize(arrayList_group.get(i).getLabel());
                            }  else
                            {

                                color_selected.add(arrayList_group.get(i).getColor1());
                                group_name=group_name+","+Utils.capitalize(arrayList_group.get(i).getLabel());
                            }

                        }
                        //  mBinding.txtGroupName.setText(group_name);
                    }else
                    {
                        mBinding.llGroups.setVisibility(View.GONE);
                    }
                    SelectedGroupDetailListAdapter selectedGroupDetailListAdapter = new SelectedGroupDetailListAdapter(context,color_value_list,value_list);
                    mBinding.selectedGroup.setAdapter(selectedGroupDetailListAdapter);
                    /**
                     * set image
                     */
                    Picasso.with(context).load(image_url).into(mBinding.contactPhoto);

                    /**
                     * set first and last name
                     */
                    ((DashboardNewActivity)context).SettextTxtTitle(fname + " " + lname);
                    /**
                     * phone number set according to parameter
                     */
                    setRuntimeVisibility(phone1,phone2,phone3,email1,email2,email3);


                    mBinding.ivemail1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            email_click(email1);
                        }
                    });

                    mBinding.ivemail2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            email_click(email2);
                        }
                    });

                    mBinding.ivemail3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            email_click(email3);
                        }
                    });
                    mBinding.ivcall1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            call_click(phone1);
                        }
                    });
                    mBinding.ivcall2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            call_click(phone2);
                        }
                    });
                    mBinding.ivcall3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            call_click(phone3);
                        }
                    });
                    mBinding.ivmsg1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            msg_click(phone1);
                        }
                    });
                    mBinding.ivmsg2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            msg_click(phone1);
                        }
                    });
                    mBinding.ivmsg3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            msg_click(phone1);
                        }
                    });

                }
                WebService.dismissProgress();
            } catch (Exception e) {
                e.printStackTrace();
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

                        getActivity().getSupportFragmentManager().popBackStack();

                        return true;
                    }
                }
                return false;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();

        ((DashboardNewActivity)context).Set_header_visibility();
        preview();
        Utils.hideKeyboard(context);
        Pref.setValue(context, "selectedGroud", "");

        new ExecuteTask().execute();
    }

    private void msg_click(String phone) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.setData(Uri.parse("sms:" + phone));
        startActivity(sendIntent);
    }

    private void call_click(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        if(phone.startsWith("+")) {
            callIntent.setData(Uri.parse("tel:" + phone));
        }else
        {
            callIntent.setData(Uri.parse("tel:" + "+" +phone));
        }
        startActivity(callIntent);
    }

    private void email_click(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sent from Android");
        intent.putExtra(Intent.EXTRA_TEXT, "");

        startActivity(Intent.createChooser(intent, "Android"));
    }

    /**
     * set runtime visibility
     */
    private void setRuntimeVisibility(String phone1, String phone2, String phone3, String email1, String email2, String email3) {
        if(!phone1.equalsIgnoreCase(""))
        {
            mBinding.llCall1.setVisibility(View.VISIBLE);
            phone(phone1,mBinding.txtPhoneNumber1,mBinding.txtPhone1,"Mobile");
        }else
        {
            mBinding.llCall1.setVisibility(View.GONE);
        }
        if(!phone2.equalsIgnoreCase(""))
        {
            mBinding.llCall2.setVisibility(View.VISIBLE);
            phone(phone2,mBinding.txtPhoneNumber2, mBinding.txtPhone2,"Home");
        }else
        {
            mBinding.llCall2.setVisibility(View.GONE);
        }

        if(!phone3.equalsIgnoreCase(""))
        {
            mBinding.llCall3.setVisibility(View.VISIBLE);
            phone(phone3,mBinding.txtPhoneNumber3, mBinding.txtPhone3,"Work");
        }else
        {
            mBinding.llCall3.setVisibility(View.GONE);
        }
        if(!email1.equalsIgnoreCase(""))
        {
            mBinding.llMsg1.setVisibility(View.VISIBLE);
            Email(email1,mBinding.txtEmailValue1,mBinding.txtMsg1,"Primary");
        }else
        {
            mBinding.llMsg1.setVisibility(View.GONE);
        }
        if(!email2.equalsIgnoreCase(""))
        {
            mBinding.llMsg2.setVisibility(View.VISIBLE);
            Email(email2,mBinding.txtEmailValue2,mBinding.txtMsg2,"Secondary");
        }else
        {
            mBinding.llMsg2.setVisibility(View.GONE);
        }
        if(!email3.equalsIgnoreCase(""))
        {
            mBinding.llMsg3.setVisibility(View.VISIBLE);
            Email(email3,mBinding.txtEmailValue3,mBinding.txtMsg3,"Tertiary");
        }else
        {
            mBinding.llMsg3.setVisibility(View.GONE);
        }
    }

    /**
     * set run time value of email
     */
    private void Email(String email, TextView_Regular txtEmailValue, TextView_Regular txtEmailValue1,String msg) {
        txtEmailValue.setText(email);
        txtEmailValue1.setText(msg);

    }

    /**
     * set run time value of phone
     */
    private void phone(String phone, TextView_Regular txtPhone, TextView_Regular txtPhone1,String msg) {
        txtPhone.setText(phone);
        txtPhone1.setText(msg);
    }


}
