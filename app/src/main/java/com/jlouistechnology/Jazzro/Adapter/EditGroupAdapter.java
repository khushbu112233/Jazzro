package com.jlouistechnology.Jazzro.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.RowEditGropBinding;


/**
 * Created by aipxperts on 9/12/16.
 */

//brach testing
public class EditGroupAdapter extends BaseAdapter {
    Context context;

    public EditGroupAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return 20;
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

        RowEditGropBinding binding;

        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.row_edit_grop, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (RowEditGropBinding) convertView.getTag();
        }

        return convertView;

    }


}
