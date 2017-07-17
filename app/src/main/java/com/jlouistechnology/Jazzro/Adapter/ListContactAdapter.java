package com.jlouistechnology.Jazzro.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlouistechnology.Jazzro.Fragment.ContactDetailsFragment;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.R;

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

    public ListContactAdapter(Context context, ArrayList<Contact> list) {
        this.context = context;
        this.list = list;


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


    public class ViewHolder {
        TextView txt_username_contact;
        TextView txt_company_name_contact;
        LinearLayout ln_main_alluser_contact;
        ImageView mGroupBgColor;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((FragmentActivity) context).getLayoutInflater();

        View rowView = convertView;
        if (rowView == null) {

            rowView = inflater.inflate(
                    R.layout.list_contact_item_layout, null);

            ViewHolder viewHolder = new ViewHolder();

            viewHolder.txt_username_contact = (TextView) rowView
                    .findViewById(R.id.txt_username_contact);
            viewHolder.txt_company_name_contact = (TextView) rowView
                    .findViewById(R.id.txt_company_name_contact);
            viewHolder.ln_main_alluser_contact = (LinearLayout) rowView.findViewById(R.id.ln_main_alluser_contact);
            viewHolder.mGroupBgColor = (ImageView) rowView.findViewById(R.id.img_group_color);


            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
       /* if(!TextUtils.isEmpty(holder.txt_username_contact.getText().toString())){
            WebService.dismissProgress();
        }
        else {
            WebService.showProgress(context);
        }*/
        holder.mGroupBgColor.setBackgroundResource(R.drawable.circle_group_shape);

        GradientDrawable drawable = (GradientDrawable) holder.mGroupBgColor.getBackground();
        ArrayList<ColorModel> colorList = new ArrayList<>();
        colorList = Utils.colorList();


        //ColorAdapter adapter=new ColorAdapter(context,colorName);

        final ArrayList<ColorModel> finalColorList = colorList;

        if (list.get(position).getGroup_list().size() > 0) {

            for(int i=0;i<finalColorList.size();i++)
            {
              /*  Log.e("background",""+finalColorList.get(i).background);
                Log.e("edit name",""+finalColorList.get(i).name);

                Log.e("edit color",""+finalColorList.get(i).color);
            */
                if(list.get(position).getGroup_list().get(0).getColor1().equals(finalColorList.get(i).name))
                {
                    drawable.setColor(Color.parseColor(finalColorList.get(i).color));
                    break;
                }
            }


/*
            //for (int i = 0; i < list.get(position).getGroup_list().size(); i++) {
            if (list.get(position).getGroup_list().get(0).getColor1().startsWith("#")) {
                drawable.setColor(Color.parseColor(list.get(position).getGroup_list().get(0).getColor1()));
            } else {
                Log.e("color group",""+list.get(position).getGroup_list().get(0).getColor1());
                if(!(list.get(position).getGroup_list().get(0).getColor1().startsWith("groupcolor"))&&!(list.get(position).getGroup_list().get(0).getColor1().startsWith("level"))) {
                    drawable.setColor(Color.parseColor("#" + list.get(position).getGroup_list().get(0).getColor1()));
                }
            }
            //}*/

        } else {
            holder.mGroupBgColor.setBackgroundResource(R.mipmap.group_dark_img);
        }
        holder.txt_username_contact.setText(list.get(position).getFname() + " " + list.get(position).getLname());
     /*   if (!TextUtils.isEmpty(list.get(position).getCompany_name())) {
            holder.txt_company_name_contact.setText(list.get(position).getCompany_name());
            holder.txt_company_name_contact.setVisibility(View.VISIBLE);
        } else {
            holder.txt_company_name_contact.setVisibility(View.GONE);
        }*/
        holder.ln_main_alluser_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pref.setValue(context, Constants.PREF_ID, list.get(position).getId());
                Pref.setValue(context, "is_Profile", "false");
                Utils.hideKeyboard(context);
                ContactDetailsFragment fragment = new ContactDetailsFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();


            }
        });

/*
        if (Integer.parseInt(MyConnectFragment.totoalContact) > position + 1) {
            Log.e("III ", "size : " + MyConnectFragment.totoalContact + "   posion +1 " + position);
            if (MyConnectFragment.conatctArrayList.size() == position + 1) {
                offset++;
                MyConnectFragment.datasync();

                Pref.setValue(context, "offset0", MyConnectFragment.conatctArrayList.size());

                Log.e("list_a.size()", "" + MyConnectFragment.conatctArrayList.size());
                Log.e("groupPosition", "" + position);


            }
        }
*/


        return rowView;
    }

/*
    public void addalldata(ArrayList<Contact> list_new) {
        this.list.addAll(list_new);
        notifyDataSetChanged();
    }
*/


/*
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Contact> results = new ArrayList<Contact>();
                if (list_filter == null)
                    list_filter = list;
                if (constraint != null) {
                    if (list_filter != null && list_filter.size() > 0) {
                        for (final Contact g : list_filter) {
                            if (g.getFname().toString().toLowerCase()
                                    .startsWith(constraint.toString().toLowerCase()) || g.getLname().toString().toLowerCase().startsWith(constraint.toString().toLowerCase()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                list = (ArrayList<Contact>) results.values;

                Log.e("list...filter", "" + list);
                if (list.size() == 0) {
                    // Toast.makeText(_context, R.string.No_result_found, Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        };
    }
*/


}