package com.jlouistechnology.Jazzro.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jlouistechnology.Jazzro.Interface.OnClickDeleteListener;
import com.jlouistechnology.Jazzro.Interface.OnClickPhoneDeleteListener;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.ListContactItemLayoutBinding;
import com.jlouistechnology.Jazzro.databinding.ListContactPhoneItemLayoutBinding;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aipxperts-ubuntu-01 on 10/8/17.
 */

public class ContactPhoneAdapter extends BaseAdapter {
    Context context;
    public ArrayList<String> list = new ArrayList<>();
    LayoutInflater inflater ;
    OnClickPhoneDeleteListener onClickDeleteListener;
    public ContactPhoneAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public void onClickPhoneDeleteListener(OnClickPhoneDeleteListener onClickDeleteListener) {
        this.onClickDeleteListener = onClickDeleteListener;
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
        binding.edtPhone1.setText(list.get(position));
        binding.ivPdelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickDeleteListener.OnClickPhoneDeleteListener(position);
                notifyDataSetChanged();
            }
        });
        return rowView;
    }
}
