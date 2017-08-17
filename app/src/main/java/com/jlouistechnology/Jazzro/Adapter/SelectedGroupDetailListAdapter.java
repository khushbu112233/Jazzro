package com.jlouistechnology.Jazzro.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Interface.OnClickEditGroupListener;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.Group;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.NewGroupListLayoutBinding;
import com.jlouistechnology.Jazzro.databinding.SelectedGroupListLayoutBinding;

import java.util.ArrayList;

/**
 * Created by aipxperts-ubuntu-01 on 4/8/17.
 */

public class SelectedGroupDetailListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater mInflater;
    ArrayList<Group> datalist;
    String type;
    ArrayList<String> selectedGroud1;
    ArrayList<String> selectedGroup_label1;

    public SelectedGroupDetailListAdapter(Context context, ArrayList<String> selectedGroud1, ArrayList<String> selectedGroup_label1) {
        this.context = context;
        mInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.selectedGroud1=selectedGroud1;
        this.selectedGroup_label1=selectedGroup_label1;
        ArrayList<ColorModel> colorList = new ArrayList<>();
        colorList = Utils.colorList();
        ArrayList<String> colorName = new ArrayList<>();
        for (int i = 0; i < (colorList.size()); i++) {

            colorName.add(colorList.get(i).color);

        }
    }

    @Override
    public int getCount() {
        return selectedGroud1.size();
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


        final SelectedGroupListLayoutBinding binding = DataBindingUtil.inflate(mInflater, R.layout.selected_group_list_layout, parent, false);
        rowView = binding.getRoot();

        ArrayList<ColorModel> colorList = new ArrayList<>();
        colorList = Utils.colorList();

        binding.txtGroupName.setText(selectedGroup_label1.get(position));
        for(int i=0;i<colorList.size();i++)
        {
                if(colorList.get(i).name.equalsIgnoreCase(selectedGroud1.get(position)))
                {
                    binding.txtGroupName.setTextColor(Color.parseColor(colorList.get(i).color));
                }

        }

        return rowView;
    }



}
