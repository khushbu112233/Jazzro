package com.jlouistechnology.Jazzro.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jlouistechnology.Jazzro.Adapter.ContactEmailAdapter;
import com.jlouistechnology.Jazzro.Adapter.ContactPhoneAdapter;
import com.jlouistechnology.Jazzro.Adapter.SelectedGroupDetailListAdapter;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Interface.OnClickDeleteListener;
import com.jlouistechnology.Jazzro.Interface.OnClickEditEmailListener;
import com.jlouistechnology.Jazzro.Interface.OnClickEditPhoneListener;
import com.jlouistechnology.Jazzro.Interface.OnClickPhoneDeleteListener;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.Model.Group;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.EditContactLayoutBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aipxperts-ubuntu-01 on 9/8/17.
 */

public class EditContactFragment extends Fragment {
    Context context;
    EditContactLayoutBinding mBinding;
    View rootView;
    ArrayList<Contact> ContactArrayList = new ArrayList<>();
    ArrayList<String> phone_arraylist = new ArrayList<>();
    ArrayList<String> email_arraylist = new ArrayList<>();
    ContactPhoneAdapter contactPhoneAdapter;
    ContactEmailAdapter contactEmailAdapter;
    ArrayList<String> selectedGroud = new ArrayList<>();
    ArrayList<String> selectedGroup_label = new ArrayList<>();
    ArrayList<String> selectedGroup_color = new ArrayList<>();


    ArrayList<String> selectedGroud1 = new ArrayList<>();
    ArrayList<String> selectedGroup_label1 = new ArrayList<>();
    String firstName, lastName, email1 = "", phone1 = "", email2 = "", phone2 = "", email3 = "", phone3 = "";
    int check_Valid_or_not = 0;
    private String countryCode = "";
    private String updateID = "";
    ArrayList<String> group_selected_id = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.edit_contact_layout, container, false);
        rootView = mBinding.getRoot();
        context = getActivity();
        //  preview();
 /*       if(selectedGroup_label.size()>0) {
            if (selectedGroup_label.size() == 1) {
                float height = 1 * (getResources().getDimension(R.dimen.listview_height));

                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mBinding.selectedGroup.getLayoutParams();
                lp.height = (int) height;
                mBinding.selectedGroup.setLayoutParams(lp);
            } else if (selectedGroup_label.size() == 2) {

                float height = 2 * (getResources().getDimension(R.dimen.listview_height));

                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mBinding.selectedGroup.getLayoutParams();
                lp.height = (int) height;
                mBinding.selectedGroup.setLayoutParams(lp);
            } else {
                float height = 3 * (getResources().getDimension(R.dimen.listview_height));

                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mBinding.selectedGroup.getLayoutParams();
                lp.height = (int) height;
                mBinding.selectedGroup.setLayoutParams(lp);

            }
        }else
        {
            float height = 30.0f;

            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mBinding.selectedGroup.getLayoutParams();
            lp.height = (int) height;
            mBinding.selectedGroup.setLayoutParams(lp);


        }*/
        /**
         * remove email when click minus image
         */
        OnClickDeleteListener onClickDeleteListener = new OnClickDeleteListener() {
            @Override
            public void OnClickDeleteListener(int pos) {
                Log.e("pos", "111" + pos);
                if (email_arraylist.size() > 0) {
                    for (int i = 0; i < email_arraylist.size(); i++) {

                        if (i == pos) {
                            email_arraylist.remove(i);
                            if (email_arraylist.size() < 3) {
                                mBinding.llAddEmail.setVisibility(View.VISIBLE);
                                mBinding.view2.setVisibility(View.VISIBLE);
                            } else {
                                mBinding.llAddEmail.setVisibility(View.GONE);
                                mBinding.view2.setVisibility(View.VISIBLE);
                            }
                            break;
                        }
                        if (email_arraylist.size() < 3) {
                            mBinding.llAddEmail.setVisibility(View.VISIBLE);
                            mBinding.view2.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.llAddEmail.setVisibility(View.GONE);
                            mBinding.view2.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        };
        /**
         * remove phone when click minus image
         */
        OnClickPhoneDeleteListener onClickPhoneDeleteListener = new OnClickPhoneDeleteListener() {
            @Override
            public void OnClickPhoneDeleteListener(int pos) {
                if (phone_arraylist.size() > 0) {
                    for (int i = 0; i < phone_arraylist.size(); i++) {

                        if (i == pos) {
                            phone_arraylist.remove(i);
                            if (phone_arraylist.size() < 3) {
                                mBinding.llAddContact.setVisibility(View.VISIBLE);
                                mBinding.view1.setVisibility(View.VISIBLE);
                            } else {
                                mBinding.llAddContact.setVisibility(View.GONE);
                                mBinding.view1.setVisibility(View.VISIBLE);
                            }
                            break;
                        }
                        if (phone_arraylist.size() < 3) {
                            mBinding.llAddContact.setVisibility(View.VISIBLE);
                            mBinding.view1.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.llAddContact.setVisibility(View.GONE);
                            mBinding.view1.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }
        };
        OnClickEditPhoneListener onClickEditPhoneListener = new OnClickEditPhoneListener() {
            @Override
            public void OnClickEditPhoneListener(int pos, String value) {
                phone_arraylist.set(pos, value);
                Log.e("get value", "phone" + phone_arraylist.size());
            }
        };

        OnClickEditEmailListener onClickEditEmailListener = new OnClickEditEmailListener() {
            @Override
            public void OnClickEditEmailListener(int pos, String value) {
                email_arraylist.set(pos, value);
                Log.e("get value", "email" + email_arraylist.size());
            }
        };
        contactPhoneAdapter = new ContactPhoneAdapter(context, phone_arraylist);
        contactPhoneAdapter.OnClickEditPhoneListener(onClickEditPhoneListener);
        mBinding.listContactPhone.setAdapter(contactPhoneAdapter);
        contactPhoneAdapter.onClickPhoneDeleteListener(onClickPhoneDeleteListener);

        contactEmailAdapter = new ContactEmailAdapter(context, email_arraylist);
        contactEmailAdapter.onClickEditEmailListener(onClickEditEmailListener);
        mBinding.listContactEmail.setAdapter(contactEmailAdapter);
        contactEmailAdapter.onClickDeleteListener(onClickDeleteListener);

        mBinding.txtDeleteContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog();
            }
        });
        ((DashboardNewActivity) context).mBinding.header.txtTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();


            }
        });

        if (email_arraylist.size() == 3) {
            mBinding.llAddEmail.setVisibility(View.GONE);
            mBinding.view2.setVisibility(View.VISIBLE);
        }
        mBinding.llAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone_display();
            }
        });
        mBinding.llAddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_display();
            }
        });

        ((DashboardNewActivity) getActivity()).mBinding.header.txtTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        mBinding.llGroupList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> group_name = new ArrayList<String>();
                for (int i = 0; i < ContactArrayList.get(0).getGroup_list().size(); i++) {
                    if (!selectedGroup_label.contains(ContactArrayList.get(0).getGroup_list().get(i).getLabel())) {
                        selectedGroup_label.add(ContactArrayList.get(0).getGroup_list().get(i).getLabel());
                    }
                }
                Gson gson = new Gson();
                String json = gson.toJson(selectedGroup_label);
                Pref.setValue(context, "SelectedGroupList", json);

                SelectedGropListFragment fragment = new SelectedGropListFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

            }
        });
        ((DashboardNewActivity) context).mBinding.header.txtTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_Valid_or_not = 0;
                if (mBinding.edtFirstName.getText().toString().equals("") || TextUtils.isEmpty(mBinding.edtFirstName.getText().toString().trim())) {
                    check_Valid_or_not = 1;
                    mBinding.edtFirstName.setError(getString(R.string.please_provide_firstname));

                }

                if (mBinding.edtLastName.getText().toString().equals("") || TextUtils.isEmpty(mBinding.edtLastName.getText().toString().trim())) {
                    check_Valid_or_not = 1;
                    mBinding.edtLastName.setError(getString(R.string.please_provide_lastname));

                }
                if (email_arraylist.size() > 0) {
                    for (int i = 0; i < email_arraylist.size(); i++) {
                        if (email_arraylist.size() == 1) {
                            if (i == 0) {
                                if (email_arraylist.get(i).toString().length() > 0) {
                                    if (!isEmailValid(email_arraylist.get(i))) {
                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_email), Toast.LENGTH_LONG).show();
                                    } else {
                                        email1 = email_arraylist.get(i);
                                    }
                                }

                            }
                        } else if (email_arraylist.size() == 2) {
                            if (i == 0) {
                                if (email_arraylist.get(i).toString().length() > 0) {
                                    if (!isEmailValid(email_arraylist.get(i))) {
                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_email), Toast.LENGTH_LONG).show();
                                    } else {

                                        email1 = email_arraylist.get(i);
                                    }
                                }
                            }
                            if (i == 1) {
                                if (email_arraylist.get(i).toString().length() > 0) {
                                    if (!isEmailValid(email_arraylist.get(i))) {
                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_email), Toast.LENGTH_LONG).show();
                                    } else {
                                        email2 = email_arraylist.get(i);
                                    }
                                }
                            }
                        } else if (email_arraylist.size() == 3) {
                            if (i == 0) {
                                if (email_arraylist.get(i).toString().length() > 0) {
                                    if (!isEmailValid(email_arraylist.get(i))) {
                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_email), Toast.LENGTH_LONG).show();
                                    } else {
                                        email1 = email_arraylist.get(i);
                                    }
                                }
                            }
                            if (i == 1) {
                                if (email_arraylist.get(i).toString().length() > 0) {
                                    if (!isEmailValid(email_arraylist.get(i))) {
                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_email), Toast.LENGTH_LONG).show();
                                    } else {
                                        email2 = email_arraylist.get(i);
                                    }
                                }
                            }
                            if (i == 2) {
                                if (email_arraylist.get(i).toString().length() > 0) {
                                    if (!isEmailValid(email_arraylist.get(i))) {
                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_email), Toast.LENGTH_LONG).show();
                                    } else {
                                        email3 = email_arraylist.get(i);
                                    }
                                }
                            }
                        }
                    }
                }
                if (phone_arraylist.size() > 0) {
                    for (int i = 0; i < phone_arraylist.size(); i++) {
                        if (phone_arraylist.size() == 1) {
                            if (i == 0) {
                                if (phone_arraylist.get(i).toString().length() > 0) {
                                    if ((phone_arraylist.get(i).toString().length() < 10) || (phone_arraylist.get(i).toString().length() > 15)) {

                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_phone_number), Toast.LENGTH_LONG).show();
                                    } else {
                                        phone1 = phone_arraylist.get(i);
                                    }
                                }
                            }
                        } else if (phone_arraylist.size() == 2) {
                            if (i == 0) {
                                if (phone_arraylist.get(i).toString().length() > 0) {
                                    if ((phone_arraylist.get(i).toString().length() < 10) || (phone_arraylist.get(i).toString().length() > 15)) {

                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_phone_number), Toast.LENGTH_LONG).show();
                                    } else {
                                        phone1 = phone_arraylist.get(i);
                                    }
                                }
                            }
                            if (i == 1) {
                                if (phone_arraylist.get(i).toString().length() > 0) {
                                    if ((phone_arraylist.get(i).toString().length() < 10) || (phone_arraylist.get(i).toString().length() > 15)) {

                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_phone_number), Toast.LENGTH_LONG).show();
                                    } else {
                                        phone2 = phone_arraylist.get(i);
                                    }
                                }
                            }
                        } else if (phone_arraylist.size() == 3) {
                            if (i == 0) {
                                if (phone_arraylist.get(i).toString().length() > 0) {
                                    if ((phone_arraylist.get(i).toString().length() < 10) || (phone_arraylist.get(i).toString().length() > 15)) {

                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_phone_number), Toast.LENGTH_LONG).show();
                                    } else {
                                        phone1 = phone_arraylist.get(i);
                                    }
                                }
                            }
                            if (i == 1) {

                                if (phone_arraylist.get(i).toString().length() > 0) {
                                    if ((phone_arraylist.get(i).toString().length() < 10) || (phone_arraylist.get(i).toString().length() > 15)) {

                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_phone_number), Toast.LENGTH_LONG).show();
                                    } else {
                                        phone2 = phone_arraylist.get(i);
                                    }
                                }
                            }
                            if (i == 2) {

                                if (phone_arraylist.get(i).toString().length() > 0) {
                                    if ((phone_arraylist.get(i).toString().length() < 10) || (phone_arraylist.get(i).toString().length() > 15)) {

                                        check_Valid_or_not = 1;
                                        Toast.makeText(context, getResources().getString(R.string.please_provide_valid_phone_number), Toast.LENGTH_LONG).show();
                                    } else {
                                        phone3 = phone_arraylist.get(i);
                                    }
                                }
                            }
                        }
                    }
                }

                if (check_Valid_or_not == 0) {
                    firstName = mBinding.edtFirstName.getText().toString();
                    lastName = mBinding.edtLastName.getText().toString();

                    /**
                     * here when update id is empty mean new contact other wise update contact.
                     */
                    /*if (TextUtils.isEmpty(updateID)) {
                        displaySimpleDialog();

                    } else {

                        ArrayList<String> selectedIDS = new ArrayList<String>();
                        callAddnewContactAPI(selectedIDS);
                    }
*/
                    Pref.setValue(context, "SelectedGroupList", "");

                    callAddnewContactAPI(selectedGroud);
                }
            }
        });

        return rootView;
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

    private void callAddnewContactAPI(ArrayList<String> selectedIDS) {


        group_selected_id = selectedIDS;
        Log.e("group_selected_id", "" + group_selected_id);
        if (!TextUtils.isEmpty(updateID)) {
            Pref.setValue(context, "updateID_add", updateID);
            Log.e("updateID", "" + updateID);

        }
        Pref.setValue(context, "firstName_add", firstName);
        Pref.setValue(context, "lastName_add", lastName);
        Pref.setValue(context, "company_name_add", "");
        if (phone1.length() > 0) {

            Pref.setValue(context, "phone1_add", countryCode + phone1);
        }
        if (phone2.length() > 0) {
            Pref.setValue(context, "2_add", countryCode + phone2);
        }
        if (phone3.length() > 0) {
            Pref.setValue(context, "phone3_add", countryCode + phone3);
        }


        if (email1.length() > 0) {

            Pref.setValue(context, "email1_add", email1);
        }
        if (email2.length() > 0) {

            Pref.setValue(context, "email2_add", email2);
        }
        if (email3.length() > 0) {

            Pref.setValue(context, "email3_add", email3);
        }
        if (WebService.isNetworkAvailable(getActivity())) {
            new ExecuteTask().execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }
    }

    private void preview() {
        ((DashboardNewActivity) context).visibilityTxtTitleleft(View.VISIBLE);
        ((DashboardNewActivity) context).visibilityTxtTitleright(View.VISIBLE);
        ((DashboardNewActivity) context).Setimagebackgroundresource(R.mipmap.contact_bar);
        ((DashboardNewActivity) context).SettextTxtTitle("Edit Contact");
        ((DashboardNewActivity) context).SettextTxtTitleLeft("Cancel");
        ((DashboardNewActivity) context).SettextTxtTitleRight("Save");

    }

    @Override
    public void onPause() {
        super.onPause();
        ((DashboardNewActivity) context).Set_header_visibility();
    }

    public void email_display() {
        Log.e("phoneDelete", "000  " + email_arraylist.size());
        if (email_arraylist.size() >= 0) {
            email_arraylist.add("");
            contactEmailAdapter.notifyDataSetChanged();
        }
        if (email_arraylist.size() == 3) {
            mBinding.llAddEmail.setVisibility(View.GONE);
            mBinding.view2.setVisibility(View.VISIBLE);
        } else if (email_arraylist.size() < 3 && email_arraylist.size() > 0) {
            mBinding.llAddEmail.setVisibility(View.VISIBLE);
            mBinding.view2.setVisibility(View.VISIBLE);
        }
    }

    public void phone_display() {
        Log.e("phoneDelete", "000  " + phone_arraylist.size());
        if (phone_arraylist.size() >= 0) {

            phone_arraylist.add("");
            contactPhoneAdapter.notifyDataSetChanged();

        }
        if (phone_arraylist.size() == 3) {
            mBinding.llAddContact.setVisibility(View.GONE);
            mBinding.view1.setVisibility(View.VISIBLE);
        } else if (phone_arraylist.size() < 3 && phone_arraylist.size() > 0) {
            mBinding.llAddContact.setVisibility(View.VISIBLE);
            mBinding.view1.setVisibility(View.VISIBLE);
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
// Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
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
        ((DashboardNewActivity) context).Set_header_visibility();

        selectedGroup_label.clear();
        preview();
        Utils.hideKeyboard(context);
        String group = "";
        Gson gson = new Gson();

        /**
         * from selected group list
         *
         */
        String group1 = "";
        if (!Pref.getValue(context, "selectedGroud", "").equalsIgnoreCase("")) {

            Log.e("aaa", "" + phone_arraylist.size());
            contactPhoneAdapter.notifyDataSetChanged();

            String json = Pref.getValue(context, "selectedGroud", "");
            String json1 = Pref.getValue(context, "selectedGroup_label", "");
            String json2 = Pref.getValue(context, "selectedGroup_color", "");
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            selectedGroud = gson.fromJson(json, type);
            selectedGroup_label = gson.fromJson(json1, type);
            selectedGroup_color = gson.fromJson(json2, type);
            // Log.e("Adaper###","^^^  "+ selectedGroud.size());

            for (int i = 0; i < selectedGroud.size(); i++) {
                Log.e("Adaper###", "****  " + selectedGroud.get(i) + " " + selectedGroup_label.get(i));
                if (i == 0) {
                    group1 = selectedGroup_label.get(i);
                } else {
                    group1 = group1 + "," + selectedGroup_label.get(i);
                }
            }
            if (selectedGroud.size() > 0) {
                mBinding.txtGroupTest.setVisibility(View.GONE);
                mBinding.selectedGroup.setVisibility(View.VISIBLE);
                mBinding.llImg.setVisibility(View.VISIBLE);
            } else {
                mBinding.txtGroupTest.setVisibility(View.VISIBLE);
                mBinding.selectedGroup.setVisibility(View.GONE);
                mBinding.llImg.setVisibility(View.GONE);
            }
            if (selectedGroup_label.size() == 1) {
                float height = 1 * (getResources().getDimension(R.dimen.listview_height));

                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mBinding.selectedGroup.getLayoutParams();
                lp.height = (int) height;
                mBinding.selectedGroup.setLayoutParams(lp);
            } else if (selectedGroup_label.size() == 2) {

                float height = 2 * (getResources().getDimension(R.dimen.listview_height));

                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mBinding.selectedGroup.getLayoutParams();
                lp.height = (int) height;
                mBinding.selectedGroup.setLayoutParams(lp);
            } else {
                float height = 3 * (getResources().getDimension(R.dimen.listview_height));

                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mBinding.selectedGroup.getLayoutParams();
                lp.height = (int) height;
                mBinding.selectedGroup.setLayoutParams(lp);

            }
            SelectedGroupDetailListAdapter selectedGroupDetailListAdapter = new SelectedGroupDetailListAdapter(context, selectedGroup_color, selectedGroup_label);
            mBinding.selectedGroup.setAdapter(selectedGroupDetailListAdapter);
            contactPhoneAdapter.notifyDataSetChanged();
            contactEmailAdapter.notifyDataSetChanged();
            if (email_arraylist.size() >= 3) {
                mBinding.llAddEmail.setVisibility(View.GONE);
                mBinding.view2.setVisibility(View.VISIBLE);
            } else if (email_arraylist.size() < 3 && email_arraylist.size() > 0) {
                mBinding.llAddEmail.setVisibility(View.VISIBLE);
                mBinding.view2.setVisibility(View.VISIBLE);
            }
            if (phone_arraylist.size() >= 3) {
                mBinding.llAddContact.setVisibility(View.GONE);
                mBinding.view1.setVisibility(View.VISIBLE);
            } else if (phone_arraylist.size() < 3 && phone_arraylist.size() > 0) {
                mBinding.llAddContact.setVisibility(View.VISIBLE);
                mBinding.view1.setVisibility(View.VISIBLE);
            }
        } else {
            // phone_arraylist.clear();
            //  email_arraylist.clear();
            if (!Pref.getValue(context, "ContactArrayList", "").equalsIgnoreCase("")) {
                String json = Pref.getValue(context, "ContactArrayList", "");

                Type type = new TypeToken<ArrayList<Contact>>() {
                }.getType();

                ContactArrayList = gson.fromJson(json, type);

                if (!ContactArrayList.get(0).getPhone1().equalsIgnoreCase("")) {
                    phone_arraylist.add(ContactArrayList.get(0).getPhone1());
                }
                if (!ContactArrayList.get(0).getPhone2().equalsIgnoreCase("")) {
                    phone_arraylist.add(ContactArrayList.get(0).getPhone2());
                }
                if (!ContactArrayList.get(0).getPhone3().equalsIgnoreCase("")) {
                    phone_arraylist.add(ContactArrayList.get(0).getPhone3());
                }
                if (!ContactArrayList.get(0).getEmail1().equalsIgnoreCase("")) {
                    email_arraylist.add(ContactArrayList.get(0).getEmail1());
                }
                if (!ContactArrayList.get(0).getEmail2().equalsIgnoreCase("")) {
                    email_arraylist.add(ContactArrayList.get(0).getEmail2());
                }
                if (!ContactArrayList.get(0).getEmail3().equalsIgnoreCase("")) {
                    email_arraylist.add(ContactArrayList.get(0).getEmail3());
                }
                contactPhoneAdapter.notifyDataSetChanged();
                contactEmailAdapter.notifyDataSetChanged();
                if (email_arraylist.size() >= 3) {
                    mBinding.llAddEmail.setVisibility(View.GONE);
                    mBinding.view2.setVisibility(View.VISIBLE);
                } else if (email_arraylist.size() < 3 && email_arraylist.size() > 0) {
                    mBinding.llAddEmail.setVisibility(View.VISIBLE);
                    mBinding.view2.setVisibility(View.VISIBLE);
                }
                if (phone_arraylist.size() >= 3) {
                    mBinding.llAddContact.setVisibility(View.GONE);
                    mBinding.view1.setVisibility(View.VISIBLE);
                } else if (phone_arraylist.size() < 3 && phone_arraylist.size() > 0) {
                    mBinding.llAddContact.setVisibility(View.VISIBLE);
                    mBinding.view1.setVisibility(View.VISIBLE);
                }
/*
            ContactPhoneAdapter contactPhoneAdapter = new ContactPhoneAdapter(context,phone_arraylist);
            mBinding.listContactPhone.setAdapter(contactPhoneAdapter);
            ContactEmailAdapter contactEmailAdapter = new ContactEmailAdapter(context,email_arraylist);
            mBinding.listContactEmail.setAdapter(contactEmailAdapter);*/
                Log.e("phone_arraylist", "" + phone_arraylist.size());
                mBinding.edtFirstName.setText(ContactArrayList.get(0).getFname());
                mBinding.edtLastName.setText(ContactArrayList.get(0).getLname());
                Picasso.with(context).load(ContactArrayList.get(0).getImage_url()).into(mBinding.imgContact);

                updateID = ContactArrayList.get(0).getId();
                // setRuntimeVisibility(ContactArrayList.get(0).getPhone1(),ContactArrayList.get(0).getPhone2(),ContactArrayList.get(0).getPhone3(),ContactArrayList.get(0).getEmail1(),ContactArrayList.get(0).getEmail2(),ContactArrayList.get(0).getEmail3());
                ArrayList<Group> group_list = new ArrayList<>();
                group_list = ContactArrayList.get(0).getGroup_list();
                for (int i = 0; i < group_list.size(); i++) {
                    selectedGroud1.add(group_list.get(i).getColor1());
                    selectedGroup_label1.add(group_list.get(i).getLabel());
                    if (i == 0) {
                        group = group_list.get(i).getLabel();
                    } else {
                        group = group + "," + group_list.get(i).getLabel();
                    }
                }
                if (selectedGroud1.size() > 0) {
                    mBinding.txtGroupTest.setVisibility(View.GONE);
                    mBinding.selectedGroup.setVisibility(View.VISIBLE);
                    mBinding.llImg.setVisibility(View.VISIBLE);
                } else {
                    mBinding.txtGroupTest.setVisibility(View.VISIBLE);
                    mBinding.selectedGroup.setVisibility(View.GONE);
                    mBinding.llImg.setVisibility(View.GONE);
                }
                if (selectedGroup_label1.size() == 1) {
                    float height = 1 * (getResources().getDimension(R.dimen.listview_height));

                    ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mBinding.selectedGroup.getLayoutParams();
                    lp.height = (int) height;
                    mBinding.selectedGroup.setLayoutParams(lp);
                } else if (selectedGroup_label1.size() == 2) {

                    float height = 2 * (getResources().getDimension(R.dimen.listview_height));

                    ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mBinding.selectedGroup.getLayoutParams();
                    lp.height = (int) height;
                    mBinding.selectedGroup.setLayoutParams(lp);
                } else {
                    float height = 3 * (getResources().getDimension(R.dimen.listview_height));

                    ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mBinding.selectedGroup.getLayoutParams();
                    lp.height = (int) height;
                    mBinding.selectedGroup.setLayoutParams(lp);

                }
                SelectedGroupDetailListAdapter selectedGroupDetailListAdapter = new SelectedGroupDetailListAdapter(context, selectedGroud1, selectedGroup_label1);
                mBinding.selectedGroup.setAdapter(selectedGroupDetailListAdapter);


            }
        }

    }


//api for delete contact.

    class ExecuteTask_delete extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(context);
        }

        @Override
        protected String doInBackground(String... params) {


            String res = WebService.PostData1(ContactArrayList.get(0).getId(), Constants.Contact_Delete, params, WebService.CONTACT_DELETE, Pref.getValue(context, Constants.TOKEN, ""));
            Log.e("res....", "" + res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                WebService.dismissProgress();
                JSONObject json2;
                json2 = new JSONObject(result);

                ContactFragment fragment = new ContactFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

                Toast.makeText(getActivity(), "Contact deleted successfully!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openDeleteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Do you want to delete this contact ?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new ExecuteTask_delete().execute();
                        Pref.srtIsDeleteContact(getActivity(), true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(getResources().getString(R.string.app_name));
        alert.show();
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

            String res = WebService.PostData2(group_selected_id, Pref.getValue(context, "updateID_add", ""), Pref.getValue(context, "firstName_add", ""), Pref.getValue(context, "lastName_add", ""), Pref.getValue(context, "company_name_add", ""), Pref.getValue(context, "phone1_add", ""), Pref.getValue(context, "email1_add", ""), Pref.getValue(context, "phone2_add", ""), Pref.getValue(context, "email2_add", ""), Pref.getValue(context, "phone3_add", ""), Pref.getValue(context, "email3_add", ""), WebService.SINGLE_CONTACT, Pref.getValue(context, Constants.TOKEN, ""));
            Log.d("nnn", " Response : " + res);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("my_result", result + "---------");

            try {
                WebService.dismissProgress();
                JSONObject json2;
                json2 = new JSONObject(result);

                Pref.setValue(context, "updateID_add", "");
                Pref.setValue(context, "firstName_add", "");
                Pref.setValue(context, "lastName_add", "");
                Pref.setValue(context, "company_name_add", "");
                Pref.setValue(context, "phone1_add", "");
                Pref.setValue(context, "email1_add", "");
                Pref.setValue(context, "phone2_add", "");
                Pref.setValue(context, "email2_add", "");
                Pref.setValue(context, "phone3_add", "");
                Pref.setValue(context, "email3_add", "");
                Pref.setValue(context, "groups_add", "");


                Toast.makeText(getActivity(), "Contact updated successfully!", Toast.LENGTH_SHORT).show();


                getActivity().getSupportFragmentManager().popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}