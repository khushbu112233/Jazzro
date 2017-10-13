package com.jlouistechnology.Jazzro.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Interface.OnClickEditGroupListener;
import com.jlouistechnology.Jazzro.Interface.OnClickGetPosotionListener;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.NewGroupListLayoutBinding;

import java.util.ArrayList;

/**
 * Created by aipxperts-ubuntu-01 on 4/8/17.
 */

public class NewGroupListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater mInflater;
    ArrayList<GroupListDataDetailModel> datalist;
    ImageView mHeaderRight;
    ArrayList<String> SelectedGroupList = new ArrayList<>();
    ArrayList<String> selectedGroup_id = new ArrayList<>();
    String type;
    OnClickEditGroupListener onClickEditGroupListener;
    OnClickGetPosotionListener onClickGetPosotionListener;
    boolean isFirstTime = true;
    int count=0;


    public NewGroupListAdapter(Context context, ArrayList<GroupListDataDetailModel> datalist, ImageView mHeaderRight, String type, ArrayList<String> SelectedGroupList,ArrayList<String> selectedGroup_id) {
        this.context = context;
        mInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.type = type;
        this.datalist = datalist;
        this.mHeaderRight = mHeaderRight;
        this.SelectedGroupList = SelectedGroupList;
        this.selectedGroup_id =selectedGroup_id;
    }

    @Override
    public int getCount() {
        return datalist.size();
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


        final NewGroupListLayoutBinding binding = DataBindingUtil.inflate(mInflater, R.layout.new_group_list_layout, parent, false);
        rowView = binding.getRoot();

        ArrayList<ColorModel> colorList = new ArrayList<>();
        colorList = Utils.colorList();
        final ArrayList<ColorModel> finalColorList = colorList;


        if (isFirstTime) {
            isFirstTime = false;
            for (int i = 0; i < SelectedGroupList.size(); i++) {
                for (int j = 0; j < datalist.size(); j++) {

                    if (selectedGroup_id.get(i).equalsIgnoreCase(datalist.get(j).id)) {
                        datalist.get(j).isClick = "true";
                    }
                }
            }
        }

        if (type.equalsIgnoreCase("main")) {

            mHeaderRight.setVisibility(View.GONE);
            binding.lnMainAlluserContact.setEnabled(false);
            binding.ivTick.setVisibility(View.GONE);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay_Semibold.ttf");
            binding.txtGroupName.setTypeface(font, Typeface.NORMAL);
        } else {
            if (datalist.get(position).isClick.equalsIgnoreCase("false")) {
                binding.ivTick.setVisibility(View.GONE);
                Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Sf_Regular.otf");
                binding.txtGroupName.setTypeface(font, Typeface.NORMAL);


            } else {

                binding.ivTick.setVisibility(View.VISIBLE);
                Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay_Semibold.ttf");
                binding.txtGroupName.setTypeface(font, Typeface.NORMAL);


            }
            mHeaderRight.setVisibility(View.VISIBLE);
            binding.lnMainAlluserContact.setEnabled(true);
        }



        if (!type.equalsIgnoreCase("main")) {

            binding.lnMainAlluserContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int clickCount = 0;
                    for (int i = 0; i < datalist.size(); i++) {
                        if (datalist.get(i).isClick.equalsIgnoreCase("true")) {
                            clickCount++;
                        }
                    }

                    if (clickCount < 5) {
                        if (datalist.get(position).isClick.equalsIgnoreCase("true")) {
                            datalist.get(position).isClick = "false";
                        } else {
                            datalist.get(position).isClick = "true";
                        }

                        notifyDataSetChanged();
                    }else
                    {
                        if (datalist.get(position).isClick.equalsIgnoreCase("true")) {
                            datalist.get(position).isClick = "false";
                        }else
                        {
                            Toast.makeText(context,"0 Group remaining. Contact can be added in maximum 5 groups",Toast.LENGTH_SHORT).show();
                        }

                        notifyDataSetChanged();

                    }
                }
            });
        }

        Log.e("label", "" + datalist.get(position).label);
        binding.txtGroupName.setText((datalist.get(position).label));

        if(datalist.get(position).number_of_contacts.equalsIgnoreCase("0"))
        {
            binding.txtMember.setText(datalist.get(position).number_of_contacts+" member");
        }else if(datalist.get(position).number_of_contacts.equalsIgnoreCase("1"))
        {
            binding.txtMember.setText(datalist.get(position).number_of_contacts+" member");
        }else
        {
            binding.txtMember.setText(datalist.get(position).number_of_contacts+" members");
        }



        binding.imgGroup.setBackgroundResource(R.drawable.circle_group_shape);
        binding.txtFirstLetter.setText(datalist.get(position).label.charAt(0) + "");
        GradientDrawable drawable = (GradientDrawable) binding.imgGroup.getBackground();
        for (
                int i = 0; i < finalColorList.size(); i++)

        {
            if (!datalist.get(position).color.equalsIgnoreCase("Choose a color")) {
                if (datalist.get(position).color.equals(finalColorList.get(i).name)) {

                    drawable.setColor(Color.parseColor(finalColorList.get(i).color));

                }
            } else {
                binding.imgGroup.setVisibility(View.GONE);
            }
        }

        mHeaderRight.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {


                onClickGetPosotionListener.onClick(position);


            }
        });

        return rowView;
    }

    public void onClickEdit(OnClickEditGroupListener onClickEditGroupListener) {
        this.onClickEditGroupListener = onClickEditGroupListener;
    }

    public void onClickGetPosotionListener(OnClickGetPosotionListener onClickGetPosotionListener)
    {
        this.onClickGetPosotionListener = onClickGetPosotionListener;
    }

}
