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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jlouistechnology.Jazzro.Fragment.ContactFragment;
import com.jlouistechnology.Jazzro.Fragment.DetailContactFragment;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Interface.OnClickAddContactListener;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.ListContactItemLayoutBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aipxperts on 9/12/16.
 */
public class ListContactAdapter extends BaseAdapter {
    Context context;
    public ArrayList<Contact> list = new ArrayList<>();
    ArrayList<Contact> copyList = new ArrayList<>();
    LayoutInflater inflater;
    Class fragment;
    OnClickAddContactListener onClickAddContactListener;

    public ListContactAdapter(Context context, ArrayList<Contact> list, Class fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void setOnClickAddContactListener(OnClickAddContactListener onClickAddContactListener) {
        this.onClickAddContactListener = onClickAddContactListener;
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
      //  list.get(position).setFname(list.get(position).getFname().toUpperCase());
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final View rowView;


        final ListContactItemLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_contact_item_layout, viewGroup, false);
        rowView = binding.getRoot();


        ArrayList<ColorModel> colorList = new ArrayList<>();
        colorList = Utils.colorList();


        //ColorAdapter adapter=new ColorAdapter(context,colorName);
        if (fragment == ContactFragment.class) {
            binding.ivPluse.setVisibility(View.GONE);
            binding.llGroupColor.setVisibility(View.VISIBLE);
            binding.lnMainAlluserContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pref.setValue(context, "Detail_id", list.get(position).getId());

                    DetailContactFragment fragment = new DetailContactFragment();
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();


                }
            });

        } else {
            binding.ivPluse.setVisibility(View.VISIBLE);
            binding.llGroupColor.setVisibility(View.GONE);
            binding.ivPluse.setTextColor(Color.parseColor(Pref.getValue(context,"add_selected_group_color","")));
            binding.lnMainAlluserContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        if (list.get(position).isSelected) {
            binding.ivPluse.setText("−");
            binding.ivPluse.setTextSize(31);
        } else {
            binding.ivPluse.setText("+");
            binding.ivPluse.setTextSize(31);
        }

        binding.ivPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickAddContactListener.onClick(position);
            }
        });


        final ArrayList<ColorModel> finalColorList = colorList;


        if (list.get(position).getGroup_list().size() > 0) {


            Log.e("finalColorList", "" + finalColorList.size());
            for (int i = 0; i < list.get(position).getGroup_list().size(); i++) {

                for (int j = 0; j < finalColorList.size(); j++) {
                    if (list.get(position).getGroup_list().get(i).getColor1().equalsIgnoreCase(finalColorList.get(j).name)) {
                        Log.e("background", "" + finalColorList.get(j).background);
                        Log.e("edit name", "" + finalColorList.get(j).name);

                        Log.e("edit color", "" + finalColorList.get(j).color);
                        ImageView imageView = new ImageView(context);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(25, 25);
                        lp.setMargins(5, 5, 5, 5);
                        imageView.setLayoutParams(lp);
                        binding.llGroupColor.addView(imageView);
                        imageView.setBackgroundResource(R.drawable.circle_group_shape);
                        GradientDrawable drawable = (GradientDrawable) imageView.getBackground();
                /*if(list.get(position).getGroup_list().get(i).getColor1().equals(finalColorList.get(i).name))
                {*/
                        drawable.setColor(Color.parseColor(finalColorList.get(j).color));
                        //}

                    }
                }


            }


            //for (int i = 0; i < list.get(position).getGroup_list().size(); i++) {
           /* if (list.get(position).getGroup_list().get(0).getColor1().startsWith("#")) {
                drawable.setColor(Color.parseColor(list.get(position).getGroup_list().get(0).getColor1()));
            } else {
                Log.e("color group",""+list.get(position).getGroup_list().get(0).getColor1());
                if(!(list.get(position).getGroup_list().get(0).getColor1().startsWith("groupcolor"))&&!(list.get(position).getGroup_list().get(0).getColor1().startsWith("level"))) {
                    drawable.setColor(Color.parseColor("#" + list.get(position).getGroup_list().get(0).getColor1()));
                }
            }*/
            //}

        } else {
            binding.llGroupColor.setVisibility(View.GONE);
        }
        Picasso.with(context).load(list.get(position).getImage_url()).into(binding.imgContact);
        binding.txtUsernameContact.setText(list.get(position).getFname() + " " + list.get(position).getLname());
        if (!list.get(position).getPhone1().equalsIgnoreCase("")) {
            binding.txtPhoneNumber.setText(list.get(position).getPhone1());
        } else {
            binding.txtPhoneNumber.setText(list.get(position).getEmail1());
        }


        return rowView;
    }


}