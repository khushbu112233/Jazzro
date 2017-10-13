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

import com.jlouistechnology.Jazzro.Interface.OnClickDeleteListener;
import com.jlouistechnology.Jazzro.Interface.OnClickEditEmailListener;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.ListContactEmailItemLayoutBinding;

import java.util.ArrayList;

/**
 * Created by aipxperts-ubuntu-01 on 10/8/17.
 */

public class ContactEmailAdapter extends BaseAdapter {
    Context context;
    public ArrayList<String> list = new ArrayList<>();
    LayoutInflater inflater ;
    //inside adapter
    OnClickDeleteListener onClickDeleteListener;
    OnClickEditEmailListener onClickEditEmailListener;
    ArrayList<Contact> contactArrayList;

    public ContactEmailAdapter(Context context, ArrayList<String> list, ArrayList<Contact> contactArrayList) {
        this.context = context;
        this.list = list;
        this.contactArrayList=contactArrayList;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public void onClickDeleteListener(OnClickDeleteListener onClickDeleteListener) {
        this.onClickDeleteListener = onClickDeleteListener;
    }
    public void onClickEditEmailListener(OnClickEditEmailListener onClickEditEmailListener) {
        this.onClickEditEmailListener = onClickEditEmailListener;
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


        final ListContactEmailItemLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_contact_email_item_layout, parent, false);
        rowView = binding.getRoot();
        binding.edtEmail1.setText(list.get(position));

            if (position == 0) {
                binding.txtEmail1.setText("Primary");
            } else if (position == 1) {
                binding.txtEmail1.setText("Secondary");
            } else if (position == 2) {
                binding.txtEmail1.setText("Tertiary");
        }
        binding.ivEdelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ivEdelete1.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                if(position==0) {
                    builder.setMessage("Are you sure you want to remove Primary?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onClickDeleteListener.OnClickDeleteListener(position);
                                    notifyDataSetChanged();

                                    binding.ivEdelete1.setEnabled(true);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();

                                    binding.ivEdelete1.setEnabled(true);
                                }
                            });
                }else if(position==1)
                {

                    builder.setMessage("Are you sure you want to remove Secondary?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onClickDeleteListener.OnClickDeleteListener(position);
                                    notifyDataSetChanged();

                                    binding.ivEdelete1.setEnabled(true);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();

                                    binding.ivEdelete1.setEnabled(true);
                                }
                            });

                }else if(position==2)
                {
                    builder.setMessage("Are you sure you want to remove Tertiary?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onClickDeleteListener.OnClickDeleteListener(position);
                                    notifyDataSetChanged();

                                    binding.ivEdelete1.setEnabled(true);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();

                                    binding.ivEdelete1.setEnabled(true);
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
        binding.edtEmail1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                onClickEditEmailListener.OnClickEditEmailListener(position,s.toString());
            }
        });
        return rowView;
    }
}
