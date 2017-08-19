package com.jlouistechnology.Jazzro.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jlouistechnology.Jazzro.Adapter.ColorAdapter;
import com.jlouistechnology.Jazzro.Adapter.EditGroupAdapter;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.GroupListDataDetailModel;
import com.jlouistechnology.Jazzro.Model.PeticularGroupContactModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.FragmentEditGroupBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by aipxperts on 9/8/17.
 */

public class EditGroupFragment extends BaseFragment {

    Context context;
    FragmentEditGroupBinding mBinding;
    TextView txt_title, txtSave, txtCancel;
    EditText edName;
    TextView txtBackgruondColor;
    ImageView img_down;
    View rootView;
    Spinner spinner1;
    int colorPosition = 0;
    GroupListDataDetailModel groupData;
    boolean isFirstTime = true;
    private ArrayList<PeticularGroupContactModel> groupList = new ArrayList<>();
    private ArrayList<PeticularGroupContactModel> myList = new ArrayList<>();
    ArrayList<ColorModel> colorList = new ArrayList<>();
    EditGroupAdapter adapter1;
    boolean isHavingData = true;
    int pageNumber = 1;
    int limit = 30;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_edit_group, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle args = getArguments();
        Gson gson = new Gson();
        groupData = gson.fromJson((String) args.getSerializable("data"), GroupListDataDetailModel.class);

        new ExecuteTasktWO(pageNumber, limit).execute();
        setup(groupData);
    }

    private void setup(GroupListDataDetailModel groupData) {
        context = getActivity();
        mBinding.edName.setText(groupData.label);
        mBinding.txtBackgruondColor.setText(groupData.color);
        Log.e("mmm", "color : " + groupData.color);

        preview();

        colorList = Utils.colorList();

        ArrayList<String> colorName = new ArrayList<>();
        for (int i = 0; i < colorList.size(); i++) {
            colorName.add(colorList.get(i).color);

            if (groupData.color.equalsIgnoreCase(colorList.get(i).name)) {
                mBinding.txtBackgruondColor.setTextColor(Color.parseColor(colorList.get(i).background));
            }
        }

        mBinding.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this group?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new deleteGroupTask().execute();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle(getResources().getString(R.string.app_name));
                alert.show();

            }
        });
        mBinding.txtBackgruondColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.spinner1.performClick();

            }
        });


        ColorAdapter adapter = new ColorAdapter(context, colorName);
        final ArrayList<ColorModel> finalColorList = colorList;
        mBinding.spinner1.setAdapter(adapter);
        Log.e("222", "" + ResourcesCompat.getColor(getResources(), R.color.new_black, null));
        mBinding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!isFirstTime) {
                    if (position == 0) {
                        mBinding.txtBackgruondColor.setText(finalColorList.get(position).background);
                        mBinding.txtBackgruondColor.setTextColor(getResources().getColor(R.color.black_40));

                    } else {
                        mBinding.txtBackgruondColor.setError(null);

                        colorPosition = position;
                        if (Pref.getValue(getActivity(), "from_group", "").equalsIgnoreCase("1")) {
                            if (!Pref.getValue(getActivity(), "Edit_color", "").equalsIgnoreCase("")) {

                                if (!Pref.getValue(getActivity(), "Edit_label", "").equalsIgnoreCase("")) {
                                    for (int i = 0; i < finalColorList.size(); i++) {
                                        Log.e("background", "" + finalColorList.get(i).background);
                                        Log.e("edit name", "" + finalColorList.get(i).name);

                                        Log.e("edit color", "" + finalColorList.get(i).color);
                                        if ((Pref.getValue(getActivity(), "Edit_color", "")).equals(finalColorList.get(i).name)) {
                                            Pref.setValue(getActivity(), "Edit_color", finalColorList.get(i).color);
                                            Log.e("test", "compare");
                                            colorPosition = i;
                                            break;
                                        }
                                    }

                                    mBinding.txtBackgruondColor.setText(Pref.getValue(getActivity(), "Edit_label", ""));
                                }
                                if (Pref.getValue(getActivity(), "Edit_color", "").startsWith("#")) {
                                    mBinding.txtBackgruondColor.setTextColor(Color.parseColor(Pref.getValue(getActivity(), "Edit_color", "")));
                                    Log.e("color", "111" + Pref.getValue(getActivity(), "Edit_color", ""));
                                } else {
                                    if (!(Pref.getValue(getActivity(), "Edit_color", "").startsWith("groupcolor")) && !(Pref.getValue(getActivity(), "Edit_color", "").startsWith("level"))) {
                                        mBinding.txtBackgruondColor.setTextColor(Color.parseColor("#" + Pref.getValue(getActivity(), "Edit_color", "")));
                                        Log.e("color", "111" + "#" + Pref.getValue(getActivity(), "Edit_color", ""));

                                    }

                                }
                                Pref.setValue(getActivity(), "Edit_color", "");
                                Pref.setValue(getActivity(), "Edit_label", "");
                            } else {
                                mBinding.txtBackgruondColor.setTextColor(Color.parseColor(finalColorList.get(position).background));
                                mBinding.txtBackgruondColor.setText(finalColorList.get(position).name);
                                Log.e("color", "111" + finalColorList.get(position).background);

                            }
                        } else {
                            mBinding.txtBackgruondColor.setTextColor(Color.parseColor(finalColorList.get(position).background));
                            mBinding.txtBackgruondColor.setText(finalColorList.get(position).name);
                            Log.e("color", "111" + finalColorList.get(position).background);

                        }
                    }
                }
                isFirstTime = false;
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ((DashboardNewActivity) context).mBinding.header.txtTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkInternetConnection(getActivity())) {
                    if (TextUtils.isEmpty(mBinding.edName.getText().toString().trim())) {
                        mBinding.edName.setError("Please provide groupname!");

                    } else if (mBinding.spinner1.getSelectedItemPosition() == 0) {
                        mBinding.txtBackgruondColor.setError("Please select color");
                    } else {
                        new updateTask().execute();
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ((DashboardNewActivity) context).mBinding.header.txtTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        adapter1 = new EditGroupAdapter(getActivity(), groupList, EditGroupFragment.class);
        mBinding.contactListview.setAdapter(adapter1);

    }

    private void preview() {
        ((DashboardNewActivity) context).visibilityTxtTitleleft(View.VISIBLE);
        ((DashboardNewActivity) context).visibilityTxtTitleright(View.VISIBLE);
        ((DashboardNewActivity) context).SettextTxtTitle("Edit Group");
        ((DashboardNewActivity) context).SettextTxtTitleLeft("Cancel");
        ((DashboardNewActivity) context).SettextTxtTitleRight("Save");


    }

    @Override
    public void onPause() {
        super.onPause();

        ((DashboardNewActivity) context).Set_header_visibility();
    }

    public class updateTask extends AsyncTask<String, Integer, String> {

        private String res;
        private String edName, background;
        private String id;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(getActivity());

            edName = mBinding.edName.getText().toString();
            this.id = groupData.id;
            this.background = mBinding.txtBackgruondColor.getText().toString();

            Utils.groupName = edName;
            Utils.groupColor = background;

        }

        @Override
        protected String doInBackground(String... params) {

            res = WebService.updateFGroup(WebService.BASE_URL + "group", id, edName, background, Pref.getValue(getActivity(), Constants.TOKEN, ""));

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // WebService.dismissProgress();
            try {
                JSONObject jsonObject = new JSONObject(res);
                String status = jsonObject.getString("status");
                if (status.equals("400")) {
                } else {

                    Toast.makeText(getActivity(), "Group updated successfully!", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                    WebService.dismissProgress();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class deleteGroupTask extends AsyncTask<String, Integer, String> {

        private String res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(getActivity());

        }

        @Override
        protected String doInBackground(String... params) {
            res = WebService.deleteGroup(WebService.DELETEGROUP, groupData.id, Pref.getValue(getActivity(), Constants.TOKEN, ""));
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("my_new_response", s + "--");
            WebService.dismissProgress();
            try {
                JSONObject jsonObject = new JSONObject(res);

                if (jsonObject.getString("status").equals("400")) {

                } else {

                    Toast.makeText(getActivity(), "Group deleted successfully!", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class ExecuteTasktWO extends AsyncTask<String, Integer, String> {
        int index;
        int length;

        public ExecuteTasktWO(int index, int length) {

            this.index = index;
            this.length = length;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            res = WebService.GetData1(WebService.GROUP + "/" + groupData.id + "/? withContacts=" + 1 + "& page=" + index + "&perpage=" + length + "&sortfield=fname" + "&sortdir=asc", Pref.getValue(getActivity(), Constants.TOKEN, ""));
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("my_result", result + "----");
            WebService.dismissProgress();
            try {
                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.optJSONObject("data");
                JSONObject contectObject = json2.getJSONObject("contacts");
                JSONArray jsonArray = contectObject.getJSONArray("data");
                if (jsonArray.length() == 0) {
                    isHavingData = false;

                } else {
                    isHavingData = true;
                }
                /*if(jsonArray.length()<limit)
                {
                    isHavingData=false;
                }*/

                // groupList.clear();
                Gson gson = new Gson();
                // String json = gson.toJson(facebookuserzeebaListModelArrayList);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject dataObject = jsonArray.getJSONObject(i);

                    PeticularGroupContactModel facebookuserzeebaListModel = gson.fromJson(dataObject.toString(), PeticularGroupContactModel.class);

                    groupList.add(facebookuserzeebaListModel);
                    myList.add(facebookuserzeebaListModel);
                    adapter1.notifyDataSetChanged();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
