package com.jlouistechnology.Jazzro.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlouistechnology.Jazzro.Fragment.ContactDetailsFragment;
import com.jlouistechnology.Jazzro.Fragment.DetailContactFragment;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.ListContactItemLayoutBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by aipxperts on 9/12/16.
 */
public class ListContactAdapter extends BaseAdapter {
    Context context;
    public ArrayList<Contact> list = new ArrayList<>();

    HashMap<String, String> map;
    String acc_no;
    private ArrayList<Contact> myList = new ArrayList<>();
    ArrayList<Contact> copyList = new ArrayList<>();
    LayoutInflater inflater ;

    public ListContactAdapter(Context context, ArrayList<Contact> list) {
        this.context = context;
        this.list = list;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void updateCpyArraylist(ArrayList<Contact> list2) {

        myList = list2;
        copyList = list2;

    }

    public void filter(String charText) {
        myList.size();
        list = new ArrayList<>();
        charText = charText.toLowerCase(Locale.getDefault());
        myList.size();
        copyList.size();
        if (charText.length() == 0) {
            list.addAll(myList);
        } else {
            /*for (PeticularGroupContactModel wp : myList) {

            }*/
            for (int i = 0; i < myList.size(); i++) {
                if (myList.get(i).fname.toLowerCase(Locale.getDefault()).contains(charText) || (myList.get(i).lname.toLowerCase(Locale.getDefault()).contains(charText))) {
                    list.add(myList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        list.get(position).setFname(list.get(position).getFname().toUpperCase());
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

        final ArrayList<ColorModel> finalColorList = colorList;



        if (list.get(position).getGroup_list().size() > 0) {


            Log.e("finalColorList",""+finalColorList);
            for(int i=0;i<list.get(position).getGroup_list().size();i++)
            {
                Log.e("background",""+finalColorList.get(i).background);
                Log.e("edit name",""+finalColorList.get(i).name);

                Log.e("edit color",""+finalColorList.get(i).color);
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
                lp.setMargins(5, 5, 5, 5);
                imageView.setLayoutParams(lp);
                binding.llGroupColor.addView(imageView);
                imageView.setBackgroundResource(R.drawable.circle_group_shape);
                GradientDrawable drawable = (GradientDrawable) imageView.getBackground();
                /*if(list.get(position).getGroup_list().get(i).getColor1().equals(finalColorList.get(i).name))
                {*/
                drawable.setColor(Color.parseColor(finalColorList.get(i).color));
                //}


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

        }else
        {
            binding.llGroupColor.setVisibility(View.GONE);
        }
        Picasso.with(context).load(list.get(position).getImage_url()).into(binding.imgContact);
        binding.txtUsernameContact.setText(list.get(position).getFname() + " " + list.get(position).getLname());
        binding.txtPhoneNumber.setText(list.get(position).getPhone1());
        binding.lnMainAlluserContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.setValue(context,"Detail_id",list.get(position).getId());

                DetailContactFragment fragment = new DetailContactFragment();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();


            }
        });




        return rowView;
    }



}