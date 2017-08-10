package com.jlouistechnology.Jazzro.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jlouistechnology.Jazzro.Adapter.ContactEmailAdapter;
import com.jlouistechnology.Jazzro.Adapter.ContactPhoneAdapter;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Interface.OnClickDeleteListener;
import com.jlouistechnology.Jazzro.Interface.OnClickPhoneDeleteListener;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.Model.Group;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.Widget.Edittext_Regular;
import com.jlouistechnology.Jazzro.Widget.TextView_Regular;
import com.jlouistechnology.Jazzro.databinding.DetailContactLayoutBinding;
import com.jlouistechnology.Jazzro.databinding.EditContactLayoutBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by aipxperts-ubuntu-01 on 9/8/17.
 */

public class EditContactFragment extends Fragment {
    Context context;
    EditContactLayoutBinding mBinding;
    View rootView;
    ArrayList<Contact> ContactArrayList = new ArrayList<>();
    ArrayList<String> phone_arraylist= new ArrayList<>();
    ArrayList<String> email_arraylist= new ArrayList<>();
    ContactPhoneAdapter contactPhoneAdapter;
    ContactEmailAdapter contactEmailAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.edit_contact_layout, container, false);
        rootView = mBinding.getRoot();
        context = getActivity();
        preview();
        OnClickDeleteListener onClickDeleteListener=new OnClickDeleteListener() {
            @Override
            public void OnClickDeleteListener(int pos) {
                if (email_arraylist.size() > 0) {
                    for (int i = 0; i < email_arraylist.size(); i++) {

                        if (i == pos) {
                            email_arraylist.remove(i);
                            break;
                        }
                    }
                }
            }
        };
        OnClickPhoneDeleteListener onClickPhoneDeleteListener=new OnClickPhoneDeleteListener() {
            @Override
            public void OnClickPhoneDeleteListener(int pos) {
                if (phone_arraylist.size() > 0) {
                    for (int i = 0; i < phone_arraylist.size(); i++) {

                        if (i == pos) {
                            phone_arraylist.remove(i);
                            break;
                        }
                    }
                }
            }
        };
        contactPhoneAdapter = new ContactPhoneAdapter(context,phone_arraylist);
        mBinding.listContactPhone.setAdapter(contactPhoneAdapter);

        contactPhoneAdapter.onClickPhoneDeleteListener(onClickPhoneDeleteListener);

        contactEmailAdapter = new ContactEmailAdapter(context,email_arraylist);
        mBinding.listContactEmail.setAdapter(contactEmailAdapter);

        contactEmailAdapter.onClickDeleteListener(onClickDeleteListener);
        mBinding.txtDeleteContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog();
            }
        });

        if(email_arraylist.size()==3)
        {
            mBinding.llAddEmail.setVisibility(View.GONE);
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



       /* if(phone_arraylist.size()==3)
        {
            mBinding.llAddContact.setVisibility(View.GONE);
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
        mBinding.ivPdelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Edit###","@@@@" + phone_arraylist.size());
                for(int i=0;i<phone_arraylist.size();i++){
                    if(i==0){
                        phone_arraylist.remove(i);
                    }
                }
                Log.e("Edit###","$$$ " +phone_arraylist.size());

                phone_display_after_remove();
            }
        });
        mBinding.ivPdelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<phone_arraylist.size();i++){
                    if(i==1){
                        phone_arraylist.remove(i);
                    }
                }
                phone_display_after_remove();
            }
        });

        mBinding.ivPdelete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<phone_arraylist.size();i++){
                    if(i==2){
                        phone_arraylist.remove(i);
                    }
                }
                phone_display_after_remove();
            }
        });

        mBinding.ivEdelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<email_arraylist.size();i++){
                    if(i==0){
                        email_arraylist.remove(i);
                    }
                }
                email_display_after_remove();
            }
        });

        mBinding.ivEdelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<email_arraylist.size();i++){
                    if(i==1){
                        email_arraylist.remove(i);
                    }
                }
                email_display_after_remove();
            }
        });
        mBinding.ivEdelete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<email_arraylist.size();i++){
                    if(i==2){
                        email_arraylist.remove(i);
                    }
                }
                email_display_after_remove();
            }
        });*/
        mBinding.txtGroups1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SelectedGropListFragment fragment = new SelectedGropListFragment();
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

        }
    });

        return rootView;
}

    private void preview() {
        ((DashboardNewActivity)context).visibilityTxtTitleleft(View.VISIBLE);
        ((DashboardNewActivity)context).visibilityTxtTitleright(View.VISIBLE);
        ((DashboardNewActivity)context).SettextTxtTitle("Edit Contact");
        ((DashboardNewActivity)context).SettextTxtTitleLeft("Cancel");
        ((DashboardNewActivity)context).SettextTxtTitleRight("Save");

    }

    @Override
    public void onPause() {
        super.onPause();
        ((DashboardNewActivity)context).Set_header_visibility();
    }
    public void email_display()
    {
        Log.e("phoneDelete","000  "+email_arraylist.size());
        if(email_arraylist.size()>=0)
        {
            if(email_arraylist.size()==0)
            {
                Log.e("phoneDelete","111  "+email_arraylist.size());
                email_arraylist.add("");
            }else if(email_arraylist.size()==1)
            {
                Log.e("phoneDelete","222  "+email_arraylist.size());
                email_arraylist.add("");
            }else if(email_arraylist.size()==2)
            {
                Log.e("phoneDelete","333  "+email_arraylist.size());
                email_arraylist.add("");
            }

            contactEmailAdapter.notifyDataSetChanged();
        }
        if(email_arraylist.size()==3)
        {
            mBinding.llAddEmail.setVisibility(View.GONE);
        }
    }
    public void phone_display()
    {
        Log.e("phoneDelete","000  "+phone_arraylist.size());
        if(phone_arraylist.size()>=0)
        {
            if(phone_arraylist.size()==0)
            {
                Log.e("phoneDelete","111  "+phone_arraylist.size());
                phone_arraylist.add("");
            }else if(phone_arraylist.size()==1)
            {
                Log.e("phoneDelete","222  "+phone_arraylist.size());
                phone_arraylist.add("");
            }else if(phone_arraylist.size()==2)
            {
                Log.e("phoneDelete","333  "+phone_arraylist.size());
                phone_arraylist.add("");
            }
            contactPhoneAdapter.notifyDataSetChanged();

        }
        if(phone_arraylist.size()==3)
        {
            mBinding.llAddContact.setVisibility(View.GONE);
        }
    }
    /* public void email_display()
     {
         Log.e("phoneDelete","000  "+email_arraylist.size());
         if(email_arraylist.size()>=0)
         {
             if(email_arraylist.size()==0)
             {
                 Log.e("phoneDelete","111  "+email_arraylist.size());
                 mBinding.llEmail1.setVisibility(View.VISIBLE);
                 email_arraylist.add("");
             }else if(email_arraylist.size()==1)
             {
                 Log.e("phoneDelete","222  "+email_arraylist.size());
                 mBinding.llEmail2.setVisibility(View.VISIBLE);
                 email_arraylist.add("");
             }else if(email_arraylist.size()==2)
             {
                 Log.e("phoneDelete","333  "+email_arraylist.size());
                 mBinding.llEmail3.setVisibility(View.VISIBLE);
                 email_arraylist.add("");
             }
         }
     }
     public void phone_display()
     {
         Log.e("phoneDelete","000  "+phone_arraylist.size());
         if(phone_arraylist.size()>=0)
         {
             if(phone_arraylist.size()==0)
             {
                 Log.e("phoneDelete","111  "+phone_arraylist.size());
                 mBinding.llPhone1.setVisibility(View.VISIBLE);
                 phone_arraylist.add("");
             }else if(phone_arraylist.size()==1)
             {
                 Log.e("phoneDelete","222  "+phone_arraylist.size());
                 mBinding.llPhone2.setVisibility(View.VISIBLE);
                 phone_arraylist.add("");
             }else if(phone_arraylist.size()==2)
             {
                 Log.e("phoneDelete","333  "+phone_arraylist.size());
                 mBinding.llPhone3.setVisibility(View.VISIBLE);
                 phone_arraylist.add("");
             }
         }
     }
     public void email_display_after_remove()
     {
         Log.e("phoneDelete","000  "+phone_arraylist.size());
         if(email_arraylist.size()>=0)
         {
             if(email_arraylist.size()==0)
             {
                 Log.e("phoneDelete","111  "+email_arraylist.size());
                 mBinding.llEmail1.setVisibility(View.GONE);
                 mBinding.llEmail2.setVisibility(View.GONE);
                 mBinding.llEmail3.setVisibility(View.GONE);

             }else if(email_arraylist.size()==1)
             {
                 Log.e("phoneDelete","222  "+email_arraylist.size());
                 mBinding.llEmail1.setVisibility(View.VISIBLE);
                 mBinding.edtEmail1.setText(email_arraylist.get(0));
                 mBinding.llEmail2.setVisibility(View.GONE);
                 mBinding.llEmail3.setVisibility(View.GONE);
             }else if(email_arraylist.size()==2)
             {
                 Log.e("phoneDelete","333  "+email_arraylist.size());
                 mBinding.llEmail1.setVisibility(View.VISIBLE);
                 mBinding.llEmail2.setVisibility(View.VISIBLE);
                 mBinding.edtEmail1.setText(email_arraylist.get(0));
                 mBinding.edtEmail2.setText(email_arraylist.get(1));
                 mBinding.llEmail3.setVisibility(View.GONE);
             }
         }
     }
     public void phone_display_after_remove()
     {
         Log.e("phoneDelete","000  "+phone_arraylist.size());
         if(phone_arraylist.size()>=0)
         {
             if(phone_arraylist.size()==0)
             {
                 Log.e("phoneDelete","111  "+phone_arraylist.size());
                 mBinding.llPhone1.setVisibility(View.GONE);
                 mBinding.llPhone2.setVisibility(View.GONE);
                 mBinding.llPhone3.setVisibility(View.GONE);

             }else if(phone_arraylist.size()==1)
             {
                 Log.e("phoneDelete","222  "+phone_arraylist.size());
                 mBinding.llPhone1.setVisibility(View.VISIBLE);
                 mBinding.edtPhone1.setText(phone_arraylist.get(0));
                 mBinding.llPhone2.setVisibility(View.GONE);
                 mBinding.llPhone3.setVisibility(View.GONE);
             }else if(phone_arraylist.size()==2)
             {
                 Log.e("phoneDelete","333  "+phone_arraylist.size());
                 mBinding.llPhone1.setVisibility(View.VISIBLE);
                 mBinding.llPhone2.setVisibility(View.VISIBLE);
                 mBinding.edtPhone1.setText(phone_arraylist.get(0));
                 mBinding.edtPhone1.setText(phone_arraylist.get(1));
                 mBinding.llPhone3.setVisibility(View.GONE);
             }
         }
     }*/
    @Override
    public void onResume() {
        super.onResume();

        String group="";
        Gson gson = new Gson();
        if (!Pref.getValue(context, "ContactArrayList", "").equalsIgnoreCase("")) {
            String json = Pref.getValue(context, "ContactArrayList", "");
            Type type = new TypeToken<ArrayList<Contact>>() {
            }.getType();
            ContactArrayList = gson.fromJson(json, type);

            if(!ContactArrayList.get(0).getPhone1().equalsIgnoreCase("")) {
                phone_arraylist.add(ContactArrayList.get(0).getPhone1());
            }
            if(!ContactArrayList.get(0).getPhone2().equalsIgnoreCase("")) {
                phone_arraylist.add(ContactArrayList.get(0).getPhone2());
            }
            if(!ContactArrayList.get(0).getPhone3().equalsIgnoreCase("")) {
                phone_arraylist.add(ContactArrayList.get(0).getPhone3());
            }
            if(!ContactArrayList.get(0).getEmail1().equalsIgnoreCase("")) {
                email_arraylist.add(ContactArrayList.get(0).getEmail1());
            }
            if(!ContactArrayList.get(0).getEmail2().equalsIgnoreCase("")) {
                email_arraylist.add(ContactArrayList.get(0).getEmail2());
            }
            if(!ContactArrayList.get(0).getEmail3().equalsIgnoreCase("")) {
                email_arraylist.add(ContactArrayList.get(0).getEmail3());
            }
            contactPhoneAdapter.notifyDataSetChanged();
            contactEmailAdapter.notifyDataSetChanged();
            if(email_arraylist.size()==3)
            {
                mBinding.llAddEmail.setVisibility(View.GONE);
            }
            if(phone_arraylist.size()==3)
            {
                mBinding.llAddContact.setVisibility(View.GONE);
            }
/*
            ContactPhoneAdapter contactPhoneAdapter = new ContactPhoneAdapter(context,phone_arraylist);
            mBinding.listContactPhone.setAdapter(contactPhoneAdapter);
            ContactEmailAdapter contactEmailAdapter = new ContactEmailAdapter(context,email_arraylist);
            mBinding.listContactEmail.setAdapter(contactEmailAdapter);*/
            Log.e("phone_arraylist",""+phone_arraylist.size());
            mBinding.edtFirstName.setText(ContactArrayList.get(0).getFname());
            mBinding.edtLastName.setText(ContactArrayList.get(0).getLname());
            Picasso.with(context).load(ContactArrayList.get(0).getImage_url()).into(mBinding.imgContact);

            // setRuntimeVisibility(ContactArrayList.get(0).getPhone1(),ContactArrayList.get(0).getPhone2(),ContactArrayList.get(0).getPhone3(),ContactArrayList.get(0).getEmail1(),ContactArrayList.get(0).getEmail2(),ContactArrayList.get(0).getEmail3());
            ArrayList<Group> group_list = new ArrayList<>();
            group_list = ContactArrayList.get(0).getGroup_list();
            for (int i=0;i<group_list.size();i++)
            {
                if(i==0)
                {
                    group=group_list.get(i).getLabel();
                }else
                {
                    group=group+","+group_list.get(i).getLabel();
                }
            }
            mBinding.txtGroups1.setText(group);

           /* if(!ContactArrayList.get(0).getPhone1().equalsIgnoreCase("")&&!ContactArrayList.get(0).getPhone2().equalsIgnoreCase("")&&!ContactArrayList.get(0).getPhone3().equalsIgnoreCase(""))
            {
                mBinding.llAddContact.setVisibility(View.GONE);
            }else
            {
                mBinding.llAddContact.setVisibility(View.VISIBLE);
            }
            if(!ContactArrayList.get(0).getEmail1().equalsIgnoreCase("")&&!ContactArrayList.get(0).getEmail2().equalsIgnoreCase("")&&!ContactArrayList.get(0).getEmail3().equalsIgnoreCase(""))
            {
                mBinding.llAddEmail.setVisibility(View.GONE);
            }else
            {
                mBinding.llAddEmail.setVisibility(View.VISIBLE);
            }*/
        }
    }

  /*  private void setRuntimeVisibility(String phone1, String phone2, String phone3, String email1, String email2, String email3) {
        if(!phone1.equalsIgnoreCase(""))
        {
            mBinding.llPhone1.setVisibility(View.VISIBLE);
            phone(phone1,mBinding.edtPhone1,mBinding.txtPhone1);
        }else
        {
            mBinding.llPhone1.setVisibility(View.GONE);
        }
        if(!phone2.equalsIgnoreCase(""))
        {
            mBinding.llPhone2.setVisibility(View.VISIBLE);
            phone(phone2,mBinding.edtPhone2, mBinding.txtPhone2);
        }else
        {
            mBinding.llPhone2.setVisibility(View.GONE);
        }

        if(!phone3.equalsIgnoreCase(""))
        {
            mBinding.llPhone3.setVisibility(View.VISIBLE);
            phone(phone3,mBinding.edtPhone3, mBinding.txtPhone3);
        }else
        {
            mBinding.llPhone3.setVisibility(View.GONE);
        }
        if(!email1.equalsIgnoreCase(""))
        {
            mBinding.llEmail1.setVisibility(View.VISIBLE);
            Email(email1,mBinding.edtEmail1,mBinding.txtEmail1);
        }else
        {
            mBinding.llEmail1.setVisibility(View.GONE);
        }
        if(!email2.equalsIgnoreCase(""))
        {
            mBinding.llEmail2.setVisibility(View.VISIBLE);
            Email(email2,mBinding.edtEmail2,mBinding.txtEmail2);
        }else
        {
            mBinding.llEmail2.setVisibility(View.GONE);
        }
        if(!email3.equalsIgnoreCase(""))
        {
            mBinding.llEmail3.setVisibility(View.VISIBLE);
            Email(email3,mBinding.edtEmail3,mBinding.txtEmail3);
        }else
        {
            mBinding.llEmail3.setVisibility(View.GONE);
        }
    }*/
    /**
     * set run time value of email
     */
    private void Email(String email, Edittext_Regular txtEmailValue, TextView_Regular txtEmailValue1) {
        txtEmailValue.setText(email);
        txtEmailValue1.setText("Email");

    }

    /**
     * set run time value of phone
     */
    private void phone(String phone, Edittext_Regular txtPhone, TextView_Regular txtPhone1) {
        txtPhone.setText(phone);
        txtPhone1.setText("Phone");
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

            getActivity().getSupportFragmentManager().popBackStack();
            Toast.makeText(getActivity(),"Contact deleted successfully!", Toast.LENGTH_SHORT).show();

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
}
