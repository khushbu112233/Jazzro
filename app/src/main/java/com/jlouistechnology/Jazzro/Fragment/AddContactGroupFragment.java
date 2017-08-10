package com.jlouistechnology.Jazzro.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.jlouistechnology.Jazzro.Jazzro.NavigationActivity;
import com.jlouistechnology.Jazzro.Jazzro.NewCardScannerActivity;
import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.jlouistechnology.Jazzro.Model.Contact;
import com.jlouistechnology.Jazzro.Model.GroupListServiceModel;
import com.jlouistechnology.Jazzro.Model.Group_g;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.network.ApiClient;
import com.jlouistechnology.Jazzro.network.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.edt_search;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_right_header;
import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_search;


public class AddContactGroupFragment extends Fragment {
    Context context;
    TextView txt_count_contacts, txt_add_groups, txt_view_all_groups, txt_view_all_contacts,txt_add_contacts;
    int page = 1;
    TextView txt_title;
    public static ArrayList<Group_g> groupArrayList = new ArrayList<>();
    int colorPosition = 0;
    private int pageNumber = 1;
   // public static boolean isCountExecute = false;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_contact_group_layout, container, false);
        context = getActivity();
        init();
        preview();
        Pref.setValue(context,"scan_email","");
        Pref.setValue(context,"scan_pno","");
        Pref.setValue(context,"back_press","0");
        NewCardScannerActivity.lst_contact_details.clear();

        Pref.setValue(getActivity(),"Edit","0");

        DashboardActivity.txt_menu.setImageResource(R.mipmap.menu);
        DashboardActivity.txt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NavigationActivity.class);
                startActivityForResult(i, DashboardActivity.NAVIGATION_KEY);
            }
        });
        DashboardActivity.ivAdd.setVisibility(GONE);
        edt_search.setVisibility(GONE);
        img_search.setVisibility(GONE);

        txt_add_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pref.setValue(getActivity(),"Editg","0");
                Pref.setValue(getActivity(),"from_group","0");
                NewGroupFragment fragment2 = new NewGroupFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment2).addToBackStack(null).commit();

            }
        });

        /**
         * set font style
         */

    /*    img_right_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pref.setValue(getActivity(), "is_Profile", "true");
                ContactDetailsFragment fragment = new ContactDetailsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });
*/
        ((DashboardActivity) getActivity()).isHideLogout(false);

        txt_view_all_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pref.setValue(context,"scan_email","");
                Pref.setValue(context,"scan_pno","");
                MyConnectFragment fragment = new MyConnectFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });
        txt_view_all_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pref.setValue(getActivity(),"from_group","1");
                GroupListFragment fragment = new GroupListFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

                //  new ExecuteTask_group().execute();
            }
        });


        txt_title.setText("Welcome" + " " + Pref.getValue(context, "fname", ""));

        return rootView;


    }

    private void preview() {
        txt_title.setTypeface(FontCustom.setTitleFont(context));
        txt_view_all_contacts.setTypeface(FontCustom.setFontOpenSansSemibold(context));
        txt_add_contacts.setTypeface(FontCustom.setFontOpenSansSemibold(context));
        txt_view_all_groups.setTypeface(FontCustom.setFontOpenSansSemibold(context));
        txt_add_groups.setTypeface(FontCustom.setFontOpenSansSemibold(context));

    }

    public void init(){
        txt_view_all_groups = (TextView) rootView.findViewById(R.id.txt_view_all_groups);
        txt_view_all_contacts = (TextView) rootView.findViewById(R.id.txt_view_all_contacts);
        txt_add_contacts = (TextView)rootView.findViewById(R.id.txt_add_contacts);
        txt_title = (TextView) rootView.findViewById(R.id.txt_title);
        txt_add_groups = (TextView)rootView.findViewById(R.id.txt_add_groups);

    }

    @Override
    public void onResume() {
        super.onResume();
        img_right_header.setImageResource(R.mipmap.profile);
    }
}
