package com.jlouistechnology.Jazzro.Fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.jlouistechnology.Jazzro.Adapter.ColorAdapter;
import com.jlouistechnology.Jazzro.Adapter.GrouListAdapter;
import com.jlouistechnology.Jazzro.Adapter.ListContactAdapter;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.FontCustom;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardActivity;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.GroupListDatModel;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.Model.GroupListServiceModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.FragmentGrouplistBinding;
import com.jlouistechnology.Jazzro.network.ApiClient;
import com.jlouistechnology.Jazzro.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import se.emilsjolander.stickylistheaders.WrapperView;
import se.emilsjolander.stickylistheaders.WrapperViewList;

import static com.jlouistechnology.Jazzro.Adapter.GrouListAdapter.datalist;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.edt_search;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_cancel_search;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_search;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.ivAdd;

/**
 * Created by aipxperts on 3/2/17.
 */


public class GroupListFragment extends BaseFragment implements StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

    FragmentGrouplistBinding mBinding;
    private ArrayList<GroupListDataDetailModel> griupList = new ArrayList<>();
    private GrouListAdapter adapter;
    private int pageNumber = 1;
    boolean isHavingMoreData = true;
    private int colorPosition = 0;
    int limit = 1;
    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    public static Filter filter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_grouplist, container, false);

        ((DashboardActivity) getActivity()).isHideLogout(true);
        Pref.setValue(getActivity(),"from_group","1");

        mBinding.txtName.setText("Groups");
        mBinding.txtName.setTypeface(FontCustom.setTitleFont(getActivity()));
        mBinding.listContacts.setDivider(this.getResources().getDrawable(R.color.login_bg_popup));
        if(!Pref.getValue(getActivity(), "total_group", "").equalsIgnoreCase("")) {
            limit = Integer.parseInt(Pref.getValue(getActivity(), "total_group", ""));
        }
        edt_search.setVisibility(View.VISIBLE);
        edt_search.setHint("Search Groups");
        img_search.setVisibility(View.VISIBLE);
        edt_search.setFocusableInTouchMode(true);
        edt_search.setText("");
        DashboardActivity.txt_menu.setImageResource(R.mipmap.home);
        DashboardActivity.txt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactGroupFragment fragment = new AddContactGroupFragment();
                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

            }
        });
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (edt_search.getText().toString().length() > 0) {
                        setSearch();
                        Utils.hideKeyboard(getActivity());
                    }
                    return true;
                }
                return false;
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_search.getText().toString().length() > 0) {
                    setSearch();
                    Utils.hideKeyboard(getActivity());
                }
            }
        });

        // setSearch();

        mBinding.listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("grouplistModel", datalist.get(position));
                GroupDetailListFargment fragment = new GroupDetailListFargment();
                fragment.setArguments(bundle);
                ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });


        setSlider();

        ivAdd.setVisibility(View.GONE);

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openDialog();
                Pref.setValue(getActivity(),"Editg","1");
                NewGroupFragment fragment2 = new NewGroupFragment();
                ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment2).addToBackStack(null).commit();

            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("SearchView", "OnPause");
        edt_search.setText("");
        edt_search.setVisibility(View.GONE);
        img_search.setVisibility(View.GONE);
        img_cancel_search.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("SearchView", "onResume");
        edt_search.setVisibility(View.VISIBLE);
        img_search.setVisibility(View.VISIBLE);


        if(!Pref.getValue(getActivity(), "total_group", "").equalsIgnoreCase("")) {
            limit = Integer.parseInt(Pref.getValue(getActivity(), "total_group", ""));
        }
        if (Utils.checkInternetConnection(getActivity())) {

            grouplistTask(pageNumber,limit);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
            WebService.dismissProgress();
        }
        setSearch();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        header.setAlpha(1);
    }

    private void setSearch() {

        if (edt_search.getText().toString().length() > 0) {
            if(griupList.size()>0) {
                filter.filter(edt_search.getText().toString());
            }
        }


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    img_cancel_search.setVisibility(View.VISIBLE);
                } else {
                    img_cancel_search.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = s.length() - 1; i >= 0; i--) {
                    if (s.charAt(i) == '\n') {
                        s.delete(i, i + 1);
                        return;
                    }
                }
            }
        });

        img_cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
                img_cancel_search.setVisibility(View.GONE);

                if (Utils.checkInternetConnection(getActivity())) {
                    grouplistTask(pageNumber,limit);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
                    WebService.dismissProgress();
                }
            }
        });
    }


    private void setSlider() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                    case 1:
                        createMenu1(menu);
                        break;

                }
            }

            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(getActivity());
                item1.setWidth(adapter.dp2px(64));
                item1.setTitle("Edit");
                item1.setTitleSize(12);
                item1.setTitleColor(Color.WHITE);
                item1.setBackground(R.color.gray);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(getActivity());
                item2.setWidth(adapter.dp2px(60));
                item2.setTitle("Delete");
                item2.setTitleColor(Color.WHITE);
                item2.setTitleSize(12);
                item2.setBackground(R.color.red);
                menu.addMenuItem(item2);
            }

        };

        mBinding.listContacts.getWrappedList().setMenuCreator(creator);

        mBinding.listContacts.getWrappedList().setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Pref.setValue(getActivity(),"Edit_label",datalist.get(position).label);
                        Pref.setValue(getActivity(),"Edit_color",datalist.get(position).color);
                        Pref.setValue(getActivity(),"Edit_id",datalist.get(position).id);
                        Pref.setValue(getActivity(),"position", String.valueOf(datalist.get(position)));
                        Pref.setValue(getActivity(),"Editg","1");
                        NewGroupFragment fragment2 = new NewGroupFragment();
                        ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment2).addToBackStack(null).commit();

                        break;
                    case 1:
                        Pref.setValue(getActivity(),"position", String.valueOf(datalist.get(position)));
                        openDialog("DELETE GROUP", datalist.get(position),Pref.getValue(getActivity(),"position",0));

                        break;
                }
                return false;
            }
        });

    }

    public class deleteGroupTask extends AsyncTask<String, Integer, String> {

        private String id;
        private String res;
        private int pos;

        public deleteGroupTask(String id, int position) {
            this.id = id;
            this.pos = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(getActivity());

        }

        @Override
        protected String doInBackground(String... params) {
            res = WebService.deleteGroup(WebService.BASE_URL + "group/delete", id, Pref.getValue(getActivity(), Constants.TOKEN, ""));
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            WebService.dismissProgress();
            try {
                JSONObject jsonObject = new JSONObject(res);

                if (jsonObject.getString("status").equals("400")) {

                } else {

                    griupList.remove(pos);
                    onResume();
                    Toast.makeText(getActivity(),"Group deleted successfully!", Toast.LENGTH_SHORT).show();
                   // AddContactGroupFragment.isCountExecute = true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void grouplistTask(int index, int length) {
        if (Utils.checkInternetConnection(getActivity())) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<GroupListServiceModel> call = apiService.groupList(Pref.getValue(getActivity(), Constants.TOKEN, ""),
                    String.valueOf(index), String.valueOf(length), "label", "asc");
            WebService.showProgress(getActivity());
            griupList.clear();
            call.enqueue(new Callback<GroupListServiceModel>() {
                @Override
                public void onResponse(Call<GroupListServiceModel> call, Response<GroupListServiceModel> response) {
                    WebService.dismissProgress();

                    if (response.body().status != 400) {
                        griupList.addAll(response.body().data.data);

                        mSectionIndices = getSectionIndices();
                        mSectionLetters = getSectionLetters();
                        ArrayList<GroupListDataDetailModel> first_letters=new ArrayList<>();
                        GroupListDataDetailModel[] groupListDataDetailModels=new GroupListDataDetailModel[mSectionLetters.length];

                        for(int i=0;i<mSectionLetters.length;i++)
                        {
                            groupListDataDetailModels[i]=new GroupListDataDetailModel();
                            groupListDataDetailModels[i].label=mSectionLetters[i].toString().toUpperCase();
                            groupListDataDetailModels[i].id="0";
                            groupListDataDetailModels[i].color="#ffffff";
                            first_letters.add(groupListDataDetailModels[i]);
                            griupList.add(groupListDataDetailModels[i]);
                        }
                        Collections.sort(griupList, new Comparator<GroupListDataDetailModel>(){
                            public int compare(GroupListDataDetailModel obj1, GroupListDataDetailModel obj2) {
                                return obj1.label.compareToIgnoreCase(obj2.label);
                            }
                        });

                        adapter = new GrouListAdapter(getActivity(), griupList);
                        mBinding.listContacts.setAdapter(adapter);
                        filter=adapter.getFilter();
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<GroupListServiceModel> call, Throwable t) {
                    WebService.dismissProgress();
                }
            });
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();

        }
    }


    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        if(griupList.size()>0) {
            if (griupList.get(0).label.length() > 0) {
                char lastFirstChar = griupList.get(0).label.charAt(0);
                sectionIndices.add(0);
                for (int i = 1; i < griupList.size(); i++) {
                    if (griupList.get(i).label.length() > 0) {
                        if (griupList.get(i).label.charAt(0) != lastFirstChar) {
                            lastFirstChar = griupList.get(i).label.charAt(0);
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
            letters[i] = griupList.get(mSectionIndices[i]).label.charAt(0);
        }
        return letters;
    }



    private void openDialog(final String str, final GroupListDataDetailModel groupListDataDetailModel, final int position) {

        final Dialog openDialog = new Dialog(getActivity());
        openDialog.setContentView(R.layout.row_group_add);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.gravity = Gravity.CENTER;

        Window window = openDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        openDialog.setTitle(str);
        final EditText edName = (EditText) openDialog.findViewById(R.id.edName);
        TextView txtCancel = null;
        TextView txtSave = null;
        TextView txtGroupName = null, txtTitle, txtBackgruondColor;
        Spinner spin;
        LinearLayout llSipner;

        llSipner = (android.widget.LinearLayout) openDialog.findViewById(R.id.llSipner);
        txtCancel = (TextView) openDialog.findViewById(R.id.txtCancel);
        txtTitle = (TextView) openDialog.findViewById(R.id.txtTitle);
        txtSave = (TextView) openDialog.findViewById(R.id.txtSave);
        txtGroupName = (TextView) openDialog.findViewById(R.id.txtGroupName);
        txtBackgruondColor = (TextView) openDialog.findViewById(R.id.txtBackgruondColor);
        spin = (Spinner) openDialog.findViewById(R.id.spinner1);

        if (str.contains("DELETE")) {
            txtTitle.setText("Are you sure you want to delete " + groupListDataDetailModel.label + " group ?");
            edName.setVisibility(View.GONE);
            llSipner.setVisibility(View.GONE);
            txtSave.setText("Delete");
            spin.setVisibility(View.GONE);
            txtSave.setTextColor((ContextCompat.getColor(getActivity(), R.color.red)));
            txtGroupName.setVisibility(View.GONE);
            txtGroupName.setText(groupListDataDetailModel.label);
        } else {
            spin.setVisibility(View.VISIBLE);
            txtTitle.setText("EDIT GROUP");
            llSipner.setVisibility(View.VISIBLE);
            edName.setText(groupListDataDetailModel.label);
            txtSave.setTextColor((ContextCompat.getColor(getActivity(), R.color.blue_txt_color)));
            edName.setVisibility(View.VISIBLE);
            txtGroupName.setVisibility(View.GONE);
        }
        ArrayList<ColorModel> colorList = new ArrayList<>();
        colorList = Utils.colorList();

        ArrayList<String> colorName = new ArrayList<>();
        for (int i = 0; i < colorList.size(); i++) {
            colorName.add(colorList.get(i).name);
        }

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, colorName);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        int spSelectionPosition = 0;
        if (!TextUtils.isEmpty(groupListDataDetailModel.color)) {
            for (int i = 0; i < colorList.size(); i++) {
                if (colorList.get(i).background.contains(groupListDataDetailModel.color)) {
                    spSelectionPosition = i;
                    break;
                }
            }
        } else {
            spin.setSelection(0);
        }
        if (spSelectionPosition != 0) {
            spin.setSelection(spSelectionPosition);
        } else {
            spin.setSelection(0);
        }


        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str.contains("DELETE")) {
                    if (Utils.checkInternetConnection(getActivity())) {
                        new deleteGroupTask(groupListDataDetailModel.id, position).execute();
                        openDialog.cancel();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.cancel();
            }
        });
        openDialog.show();
    }
}
