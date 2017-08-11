package com.jlouistechnology.Jazzro.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
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
    TextView mHeaderRight;
    ArrayList<String> selectedGroud = new ArrayList<>();
    ArrayList<String> selectedGroup_label = new ArrayList<>();
    ArrayList<String> SelectedGroupList=new ArrayList<>();
    String type;
    OnClickEditGroupListener onClickEditGroupListener;

    public NewGroupListAdapter(Context context, ArrayList<GroupListDataDetailModel> datalist, TextView mHeaderRight, String type, ArrayList<String> SelectedGroupList) {
        this.context = context;
        mInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.type = type;
        this.datalist = datalist;
        this.mHeaderRight = mHeaderRight;
        this.SelectedGroupList=SelectedGroupList;
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


        for(int i=0;i<SelectedGroupList.size();i++)
        {
            for(int j=0;j<datalist.size();j++) {

                if (SelectedGroupList.get(i).equalsIgnoreCase(datalist.get(j).label)) {
                    datalist.get(j).isClick = "true";
                }
            }
        }
        if (datalist.get(position).isClick.equalsIgnoreCase("false")) {
            binding.ivTick.setVisibility(View.GONE);
        } else {
            binding.ivTick.setVisibility(View.VISIBLE);
        }

        binding.lnMainAlluserContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (datalist.get(position).isClick.equalsIgnoreCase("true")) {
                    datalist.get(position).isClick = "false";
                } else {
                    datalist.get(position).isClick = "true";
                }
                notifyDataSetChanged();
            }
        });

        binding.lnBgColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEditGroupListener.onClick(position);
            }
        });

        if (type.equalsIgnoreCase("main")) {
            mHeaderRight.setVisibility(View.GONE);
            binding.lnMainAlluserContact.setEnabled(false);
        } else if (type.equalsIgnoreCase("selected")) {
            mHeaderRight.setVisibility(View.VISIBLE);
            binding.lnMainAlluserContact.setEnabled(true);
        }
        Log.e("label", "" + datalist.get(position).label);
        binding.txtGroupName.setText(Utils.capitalize(datalist.get(position).label));
        binding.txtMember.setText("1 members");

        binding.imgGroup.setBackgroundResource(R.drawable.circle_group_shape);
        binding.txtFirstLetter.setText(datalist.get(position).label.charAt(0) + "");
        GradientDrawable drawable = (GradientDrawable) binding.imgGroup.getBackground();
        for (int i = 0; i < finalColorList.size(); i++) {
            if (datalist.get(position).color.equals(finalColorList.get(i).name)) {
                drawable.setColor(Color.parseColor(finalColorList.get(i).color));
            }
        }

        mHeaderRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < datalist.size(); i++) {
                    if (datalist.get(i).isClick.equalsIgnoreCase("true")) {
                        selectedGroud.add(datalist.get(i).id);
                        selectedGroup_label.add(Utils.capitalize(datalist.get(i).label));
                    }
                }
                Gson gson = new Gson();


                //   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                //   SharedPreferences.Editor editor = sharedPrefs.edit();

                String json = gson.toJson(selectedGroud);
                String json1 = gson.toJson(selectedGroup_label);

                Pref.setValue(context, "selectedGroud", json);
                Pref.setValue(context, "selectedGroup_label", json1);

               /* editor.putString("selectedGroud", json);
                editor.putString("selectedGroup_label",json1);
                editor.commit();*/
                Log.e("Adaper###", "%%% " + selectedGroud.size());
                // notifyDataSetChanged();
                ((FragmentActivity) context).getSupportFragmentManager().popBackStack();

                Pref.setValue(context, "SelectedGroupList", "");
            }
        });

        return rowView;
    }

    public void onClickEdit(OnClickEditGroupListener onClickEditGroupListener) {
        this.onClickEditGroupListener = onClickEditGroupListener;
    }


}