package com.jlouistechnology.Jazzro.Adapter;

/**
 * Created by aipxperts on 3/2/16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlouistechnology.Jazzro.R;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> navDrawerItems;
    String user;
    String image_url;
    public static ImageView image;

    Bitmap bmp;
    String activity_name;

    public NavDrawerListAdapter(Context context, ArrayList<String> navDrawerItems, String user, String image_url, String activity_name){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.user=user;
        this.image_url=image_url;

        this.activity_name=activity_name;

        Log.v("activity_name", activity_name + "");
    }

    public NavDrawerListAdapter(Context context, ArrayList<String> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;



        Log.v("activity_name", activity_name + "");
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list, null);

        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
       // TextView txtTitle1 = (TextView) convertView.findViewById(R.id.title1);
       // RelativeLayout layout=(RelativeLayout)convertView.findViewById(R.id.layout);
       image = (ImageView) convertView.findViewById(R.id.image);
      //  TextView edit_profile=(TextView) convertView.findViewById(R.id.edit_profile);
       // TextView usertxt = (TextView) convertView.findViewById(R.id.usertxt);
        View v1=(View)convertView.findViewById(R.id.v1);
      //  usertxt.setTypeface(FontCustom.setFont(context));
       // edit_profile.setTypeface(FontCustom.setFont(context));
        //txtTitle.setTypeface(FontCustom.setFont(context));

        txtTitle.setText(navDrawerItems.get(position));


        if(position==0)
        {
            txtTitle.setBackgroundColor(Color.WHITE);
        }
        else if(position%2==0)
        {
            txtTitle.setBackgroundColor(Color.WHITE);
        }
        else {
            txtTitle.setBackgroundColor(Color.TRANSPARENT);
        }


            txtTitle.setTextColor(Color.parseColor("#565658"));
            v1.setBackgroundColor(Color.parseColor("#ffffff"));






        return convertView;
    }

}