package com.jlouistechnology.Jazzro.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Fragment.ContactDetailsFragment;
import com.jlouistechnology.Jazzro.Fragment.GroupDetailListFargment;
import com.jlouistechnology.Jazzro.Fragment.MyConnectFragment;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.FontCustom;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.Model.PeticularGroupContactModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.databinding.RowGroupListBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import static com.jlouistechnology.Jazzro.Fragment.MyConnectFragment.offset;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.edt_search;

/**
 * Created by aipxperts on 9/12/16.
 */
public class GrouListAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer,Filterable {
    Context context;
    public static ArrayList<GroupListDataDetailModel> datalist = new ArrayList<>();
    private ArrayList<GroupListDataDetailModel> myList = new ArrayList<>();
    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    private LayoutInflater mInflater;

    public GrouListAdapter(Context context, ArrayList<GroupListDataDetailModel> datalist) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.datalist = datalist;
        this.myList = datalist;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        if (datalist.size() > 0) {
            if (datalist.get(0).label.length() > 0) {
                char lastFirstChar = datalist.get(0).label.charAt(0);
                sectionIndices.add(0);
                for (int i = 1; i < datalist.size(); i++) {
                    if (datalist.get(i).label.length() > 0) {
                        if (datalist.get(i).label.charAt(0) != lastFirstChar) {
                            lastFirstChar = datalist.get(i).label.charAt(0);
                            sectionIndices.add(i);
                        }
                    }
                }
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private Character[] getSectionLetters() {
        Character[] letters = new Character[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = datalist.get(mSectionIndices[i]).label.charAt(0);
        }
        return letters;
    }

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public void setList(ArrayList<GroupListDataDetailModel> myList) {
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

        RowGroupListBinding binding;

        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.row_group_list, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (RowGroupListBinding) convertView.getTag();
        }

        datalist.get(position).label = Utils.capitalize(datalist.get(position).label);
        if(datalist.get(position).id.equals("0"))
        {
            binding.txtName.setVisibility(View.GONE);
            binding.imgGroupColor.setVisibility(View.GONE);
            binding.viewdevider.setBackgroundColor(Color.TRANSPARENT);
        }
        else {
            binding.txtName.setVisibility(View.VISIBLE);
            binding.imgGroupColor.setVisibility(View.VISIBLE);
            binding.viewdevider.setBackgroundResource(R.color.viewfinder_mask);
        }
        binding.txtName.setText(datalist.get(position).label);
        binding.txtName2.setVisibility(View.GONE);
        binding.imgGroupColor.setBackgroundResource(R.drawable.circle_group_shape);

        ArrayList<ColorModel> colorList = new ArrayList<>();
        colorList = Utils.colorList();
        final ArrayList<ColorModel> finalColorList = colorList;


        GradientDrawable drawable = (GradientDrawable) binding.imgGroupColor.getBackground();

            for(int i=0;i<finalColorList.size();i++)
        {
            if(datalist.get(position).color.equals(finalColorList.get(i).name))
            {
                drawable.setColor(Color.parseColor(finalColorList.get(i).color));
                break;
            }
        }




        return convertView;

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            holder.text.setTypeface(FontCustom.setTitleFont(context));

            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }


        // set header text as first char in name
        if (datalist.get(position).label.length() > 0) {
            String firstLetter = String.valueOf(datalist.get(position).label.subSequence(0, 1)).toUpperCase();
            CharSequence headerChar = firstLetter;
            //   Log.e("ListContact", "&&& " + headerChar);


            holder.text.setText(headerChar);
        }

        return convertView;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        if (!TextUtils.isEmpty(datalist.get(position).label)) {
            return datalist.get(position).label.subSequence(0, 1).charAt(0);
        } else {
            return 0;
        }
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        datalist.clear();

        if (charText.length() == 0) {
            datalist.addAll(myList);
        } else {

            for (int i = 0; i < myList.size(); i++) {
                if (myList.get(i).label.toLowerCase(Locale.getDefault()).contains(charText) || myList.get(i).label.toUpperCase(Locale.getDefault()).contains(charText)) {
                    datalist.add(myList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<GroupListDataDetailModel> results = new ArrayList<GroupListDataDetailModel>();
                ArrayList<GroupListDataDetailModel> results_temp = new ArrayList<GroupListDataDetailModel>();
                if(myList != null && myList.size()<datalist.size() )
                {
                    myList = datalist;
                }
                if (myList == null)
                    myList = datalist;

                if (constraint != null) {
                    if (myList != null && myList.size() > 0) {
                        for (final GroupListDataDetailModel g : myList) {
                            if (g.label.toLowerCase().startsWith(constraint.toString(),0))
// .contains(constraint.toString()))
                                results.add(g);

                        }
                        results_temp.clear();
                        results_temp.addAll(results);
                        results.clear();

                        GroupListDataDetailModel[] groupListDataDetailModels=new GroupListDataDetailModel[results_temp.size()];
                        for(int i=0;i<results_temp.size();i++)
                        {
                            groupListDataDetailModels[0]=new GroupListDataDetailModel();
                            groupListDataDetailModels[0].label= String.valueOf(results_temp.get(i).label.charAt(0));
                            groupListDataDetailModels[0].id="0";
                            groupListDataDetailModels[0].color="#ffffff";
                            results.add(groupListDataDetailModels[0]);
                            break;
                            //griupList.add(groupListDataDetailModels[i]);
                        }

                        results.addAll(results_temp);
                    }else {
                        Toast.makeText(context,"No results found!",Toast.LENGTH_LONG).show();
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                datalist = (ArrayList<GroupListDataDetailModel>) results.values;
                if(datalist.size()==0)
                {
                    Toast.makeText(context,"No results found!",Toast.LENGTH_LONG).show();
                }
                notifyDataSetChanged();
            }
        };
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    class HeaderViewHolder {
        TextView text;
    }

}
