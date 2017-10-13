package com.jlouistechnology.Jazzro.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jlouistechnology.Jazzro.Fragment.DetailContactFragment;
import com.jlouistechnology.Jazzro.Fragment.FriendsListFragment;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Interface.OnClickDeleteContactListener;
import com.jlouistechnology.Jazzro.Model.PeticularGroupContactModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.RowEditGropBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by aipxperts on 9/12/16.
 */

//brach testing
public class EditGroupAdapter extends BaseAdapter {
    Context context;
    ArrayList<PeticularGroupContactModel> groupData;
    Class fragment;
    OnClickDeleteContactListener onClickDeleteContactListener;

    public void setOnClickDeleteContactListener(OnClickDeleteContactListener onClickDeleteContactListener) {
        this.onClickDeleteContactListener = onClickDeleteContactListener;
    }

    public EditGroupAdapter(Context context, ArrayList<PeticularGroupContactModel> groupData, Class fragment) {
        this.context = context;
        this.groupData = groupData;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return groupData.size();
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
        binding.txtname.setText(groupData.get(position).fname + " " + groupData.get(position).lname);
        if (!groupData.get(position).phone1.equalsIgnoreCase("")) {
            binding.txtNumber.setText(groupData.get(position).phone1);
        } else {
            binding.txtNumber.setText(groupData.get(position).email1);
        }
        Picasso.with(context).load(groupData.get(position).image_url).into(binding.imgContactPhoto);

        if (fragment == FriendsListFragment.class) {
            binding.ivMinus.setVisibility(View.GONE);
            binding.lnMainAlluserContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pref.setValue(context, "Detail_id", groupData.get(position).id);

                    DetailContactFragment fragment = new DetailContactFragment();
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();


                }
            });
        }
        if (groupData.get(position).isSelected) {
            binding.ivMinus.setImageResource(R.mipmap.add);
        } else {
            binding.ivMinus.setImageResource(R.mipmap.delete);
        }
        binding.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickDeleteContactListener.onClick(position);
            }
        });

        return convertView;

    }


}