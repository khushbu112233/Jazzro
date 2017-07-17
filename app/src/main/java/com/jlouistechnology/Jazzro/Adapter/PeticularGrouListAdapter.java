package com.jlouistechnology.Jazzro.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Model.PeticularGroupContactModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.RowGroupList1Binding;
import com.jlouistechnology.Jazzro.databinding.RowGroupListBinding;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by aipxperts on 9/12/16.
 */
public class PeticularGrouListAdapter extends BaseAdapter {
    Context context;
    private ArrayList<PeticularGroupContactModel> datalist = new ArrayList<>();
    private ArrayList<PeticularGroupContactModel> myList = new ArrayList<>();

    public PeticularGrouListAdapter(Context context, ArrayList<PeticularGroupContactModel> datalist) {
        this.context = context;
        this.datalist = datalist;
        this.myList = datalist;


    }

    public void setList(ArrayList<PeticularGroupContactModel> myList) {
        this.myList = myList;
    }

    @Override
    public int getCount() {

        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RowGroupList1Binding binding;

        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.row_group_list1, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (RowGroupList1Binding) convertView.getTag();
        }

        binding.txtName.setText(datalist.get(position).fname + " " + datalist.get(position).lname);

        if (TextUtils.isEmpty(datalist.get(position).company_name)) {
            // binding.txtName2.setVisibility(View.GONE);
        } else {
            binding.txtName2.setText(datalist.get(position).company_name);

            //binding.txtName2.setVisibility(View.VISIBLE);
        }

        return convertView;

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        datalist.clear();
        if (charText.length() == 0) {
            datalist.addAll(myList);
        } else {
            /*for (PeticularGroupContactModel wp : myList) {

            }*/

                for (int i = 0; i < myList.size(); i++) {
                    if (myList.get(i).fname.toLowerCase(Locale.getDefault()).contains(charText)) {
                        datalist.add(myList.get(i));
                    }
                }
            if(datalist.size()==0)
            {
                Toast.makeText(context,"No results found!",Toast.LENGTH_LONG).show();
            }
        }
        notifyDataSetChanged();
    }
}
