package com.jlouistechnology.Jazzro.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.FontCustom;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardActivity;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.Model.Group;
import com.jlouistechnology.Jazzro.Model.GroupListServiceModel;
import com.jlouistechnology.Jazzro.Model.Group_g;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.databinding.NewGroupLayoutBinding;
import com.jlouistechnology.Jazzro.network.ApiClient;
import com.jlouistechnology.Jazzro.network.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aipxperts-ubuntu-01 on 22/4/17.
 */

public class NewGroupFragment extends Fragment {
    Context context;
    NewGroupLayoutBinding mBinding;
    TextView txt_title,txtSave,txtCancel;
    EditText edName;
    TextView txtBackgruondColor;
    ImageView img_down;
    View rootView;
    Spinner spinner1;
    int colorPosition = 0;
    int pageNumber=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.new_group_layout, container, false);
        rootView = mBinding.getRoot();
        context = getActivity();
        preview();
        ArrayList<ColorModel> colorList = new ArrayList<>();
        colorList = Utils.colorList();

        ArrayList<String> colorName = new ArrayList<>();
        for (int i = 0; i < colorList.size(); i++) {
            colorName.add(colorList.get(i).color);
        }
        mBinding.txtBackgruondColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.spinner1.performClick();

            }
        });
        ColorAdapter adapter=new ColorAdapter(context,colorName);
        final ArrayList<ColorModel> finalColorList = colorList;
        mBinding.spinner1.setAdapter(adapter);
        Log.e("222",""+ ResourcesCompat.getColor(getResources(),R.color.new_black, null));
        mBinding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                colorPosition = position;
                if(Pref.getValue(getActivity(),"from_group","").equalsIgnoreCase("1"))
                {
                    if(!Pref.getValue(getActivity(),"Edit_color","").equalsIgnoreCase(""))
                    {

                        if(!Pref.getValue(getActivity(),"Edit_label","").equalsIgnoreCase(""))
                        {
                            for(int i=0;i<finalColorList.size();i++)
                            {
                                Log.e("background",""+finalColorList.get(i).background);
                                Log.e("edit name",""+finalColorList.get(i).name);

                                Log.e("edit color",""+finalColorList.get(i).color);
                                if((Pref.getValue(getActivity(),"Edit_color","")).equals(finalColorList.get(i).name))
                                {
                                    Pref.setValue(getActivity(),"Edit_color",finalColorList.get(i).color);
                                    Log.e("test","compare");
                                    colorPosition=i;
                                    break;
                                }
                            }

                            mBinding.txtBackgruondColor.setText(Pref.getValue(getActivity(),"Edit_label",""));
                        }
                        if(Pref.getValue(getActivity(),"Edit_color","").startsWith("#"))
                        {
                            mBinding.txtBackgruondColor.setTextColor(Color.parseColor(Pref.getValue(getActivity(), "Edit_color", "")));
                            Log.e("color","111"+Pref.getValue(getActivity(), "Edit_color", ""));
                        }else {
                            if(!(Pref.getValue(getActivity(), "Edit_color", "").startsWith("groupcolor"))&&!(Pref.getValue(getActivity(), "Edit_color", "").startsWith("level"))) {
                                mBinding.txtBackgruondColor.setTextColor(Color.parseColor("#" + Pref.getValue(getActivity(), "Edit_color", "")));
                                Log.e("color","111"+"#"+Pref.getValue(getActivity(), "Edit_color", ""));

                            }

                        }
                        Pref.setValue(getActivity(),"Edit_color","");
                        Pref.setValue(getActivity(),"Edit_label","");
                    }
                    else {
                        mBinding.txtBackgruondColor.setTextColor(Color.parseColor(finalColorList.get(position).background));
                        mBinding.txtBackgruondColor.setText(finalColorList.get(position).name);
                        Log.e("color","111"+finalColorList.get(position).background);

                    }
                }else
                {
                    mBinding.txtBackgruondColor.setTextColor(Color.parseColor(finalColorList.get(position).background));
                    mBinding.txtBackgruondColor.setText(finalColorList.get(position).name);
                    Log.e("color","111"+finalColorList.get(position).background);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ((DashboardNewActivity)context).mBinding.header.txtTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkInternetConnection(getActivity())) {
                    if (TextUtils.isEmpty(mBinding.edName.getText().toString().trim())) {
                        mBinding.edName.setError("Please provide groupname!");

                    } else {
                        // Log.e("color code add",""+finalColorList.get(colorPosition).background);
                      /*  if(Pref.getValue(getActivity(),"from_group","").equalsIgnoreCase("1")) {

                            new updateTask(Pref.getValue(getActivity(),"Edit_id",""), edName.getText().toString(), Pref.getValue(getActivity(),"position",0), finalColorList.get(colorPosition).name).execute();
                        }else {*/
                            new createGroupTask(mBinding.edName.getText().toString(), finalColorList.get(colorPosition).name).execute();
                        //}

                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ((DashboardNewActivity)context).mBinding.header.txtTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        return rootView;
    }


    private void preview() {
        ((DashboardNewActivity) context).visibilityTxtTitleleft(View.VISIBLE);
        ((DashboardNewActivity) context).visibilityTxtTitleright(View.VISIBLE);
        ((DashboardNewActivity) context).SettextTxtTitle("New Group");
        ((DashboardNewActivity) context).SettextTxtTitleLeft("Cancel");
        ((DashboardNewActivity) context).SettextTxtTitleRight("Save");



    }

    @Override
    public void onPause() {
        super.onPause();
        ((DashboardNewActivity)context).Set_header_visibility();
    }

    public class createGroupTask extends AsyncTask<String, Integer, String> {

        private String res;
        private String edName;
        private String background;

        public createGroupTask(String s, String background) {
            edName = s;
            this.background = background;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(getActivity());

        }

        @Override
        protected String doInBackground(String... params) {

            res = WebService.cretaeGroup(WebService.BASE_URL + "group", edName, background, Pref.getValue(getActivity(), Constants.TOKEN, ""));

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(res);
                String status = jsonObject.getString("status");
                if (status.equals("400")) {
                } else {

                    Toast.makeText(getActivity(),"Group added successfully!", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                    new ExecuteTask_get_data().execute();
                    WebService.dismissProgress();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public class updateTask extends AsyncTask<String, Integer, String> {

        private String res;
        private String edName, background;
        private String id;
        private int position;

        public updateTask(String id, String s, int position, String background) {
            edName = s;
            this.id = id;
            this.background = background;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(getActivity());

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

                    Toast.makeText(getActivity(),"Group updated successfully!", Toast.LENGTH_SHORT).show();
                    GroupListFragment fragment = new GroupListFragment();
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

                    new ExecuteTask_get_data().execute();
                    WebService.dismissProgress();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class ExecuteTask_get_data extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(context);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            String token=Pref.getValue(context, Constants.TOKEN, "");

            try {
                res = WebService.getResponseUsingHeader(WebService.USER, token);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                WebService.dismissProgress();
                JSONObject json2;
                JSONObject json1 = new JSONObject(result);
                json2 = json1.optJSONObject("data");

                String num_groups = json2.optString("num_groups");
                Pref.setValue(context, "total_group", num_groups);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
