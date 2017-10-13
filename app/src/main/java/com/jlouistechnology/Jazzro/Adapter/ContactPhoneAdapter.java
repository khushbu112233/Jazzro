package com.jlouistechnology.Jazzro.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jlouistechnology.Jazzro.Interface.OnClickEditPhoneListener;
import com.jlouistechnology.Jazzro.Interface.OnClickPhoneDeleteListener;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.ListContactPhoneItemLayoutBinding;

import java.util.ArrayList;

/**
 * Created by aipxperts-ubuntu-01 on 10/8/17.
 */

public class ContactPhoneAdapter extends BaseAdapter {
    Context context;
    public ArrayList<String> list = new ArrayList<>();
    LayoutInflater inflater ;
    OnClickPhoneDeleteListener onClickDeleteListener;
    OnClickEditPhoneListener onClickEditPhoneListener;
    ArrayList<String> edit_value=new ArrayList<>();
    ArrayList<Contact> contactArrayList;
    public ContactPhoneAdapter(Context context, ArrayList<String> list, ArrayList<Contact> contactArrayList) {
        this.context = context;
        this.list = list;
        this.contactArrayList=contactArrayList;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public void onClickPhoneDeleteListener(OnClickPhoneDeleteListener onClickDeleteListener) {
        this.onClickDeleteListener = onClickDeleteListener;
    }
    public void OnClickEditPhoneListener(OnClickEditPhoneListener onClickEditPhoneListener) {
        this.onClickEditPhoneListener = onClickEditPhoneListener;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View rowView;


        final ListContactPhoneItemLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_contact_phone_item_layout, parent, false);
        rowView = binding.getRoot();
        binding.v1.setVisibility(View.VISIBLE);
        binding.edtPhone1.setText(list.get(position));
        if (position == 0) {
            binding.txtPhone1.setText("Mobile");

        } else if (position == 1) {
            binding.txtPhone1.setText("Home");

        } else if (position == 2) {
            binding.txtPhone1.setText("Work");

        }
        binding.ivPdelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.ivPdelete1.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                if(position==0) {

                    builder.setMessage("Are you sure you want to remove Mobile?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onClickDeleteListener.OnClickPhoneDeleteListener(position);
                                    notifyDataSetChanged();
                                    binding.ivPdelete1.setEnabled(true);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    binding.ivPdelete1.setEnabled(true);
                                }
                            });
                }else if(position==1)
                {
                    builder.setMessage("Are you sure you want to remove Home?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onClickDeleteListener.OnClickPhoneDeleteListener(position);
                                    notifyDataSetChanged();
                                    binding.ivPdelete1.setEnabled(true);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    binding.ivPdelete1.setEnabled(true);
                                }
                            });
                }else if(position==2)
                {
                    builder.setMessage("Are you sure you want to remove Work?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onClickDeleteListener.OnClickPhoneDeleteListener(position);
                                    notifyDataSetChanged();
                                    binding.ivPdelete1.setEnabled(true);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    binding.ivPdelete1.setEnabled(true);
                                }
                            });
                }
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle(context.getResources().getString(R.string.app_name));
                alert.show();
            }
        });
        binding.edtPhone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                onClickEditPhoneListener.OnClickEditPhoneListener(position,s.toString());
            }

        });
        return rowView;
    }
}
