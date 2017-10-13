package com.jlouistechnology.Jazzro.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Helper.CheckInternet;
import com.jlouistechnology.Jazzro.Helper.ConnectionDetector;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.FontCustom;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Jazzro.DashboardActivity;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;

import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

import static com.jlouistechnology.Jazzro.Jazzro.DashboardActivity.img_right_header;

/**
 * Created by aipxperts on 8/12/16.
 */
public class ContactDetailsFragment extends Fragment {
    Context context;
    ImageView img_left;
    TextView txt_title_right, txt_contact_name, txt_mobile1, txt_email1,txtCancel;
    Dialog dialog;
    private String jsonString = "";
    TextView txt_call,txt_msg,txt_email_new,txt_delete;
    View rootView;
    ConnectionDetector connectionDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contact_list_new_layout, container, false);
        ((DashboardActivity) getActivity()).isHideLogout(false);
        context = getActivity();
        connectionDetector = new ConnectionDetector(context);
        /**
         * initial component
         */
        init();
        /**
         * set font style
         */

        preview();
        Utils.hideKeyboard(getActivity());
        DashboardActivity.ivAdd.setVisibility(View.GONE);





        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_delete.setEnabled(false);
                openDeleteDialog();

            }
        });
        if (Pref.getValue(getActivity(), "is_Profile", "").equals("true")) {
            txt_email_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Pref.getValue(getActivity(), Constants.PREF_EMAIL, "")});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Sent from Android");
                    intent.putExtra(Intent.EXTRA_TEXT, "");

                    startActivity(Intent.createChooser(intent, "Android"));
                }
            });
            txt_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("call","call");
                    Log.e("call_no",""+Pref.getValue(getActivity(), Constants.PREF_PHONE, ""));
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    if(Pref.getValue(getActivity(), Constants.PREF_PHONE, "").startsWith("+")) {
                        callIntent.setData(Uri.parse("tel:" + Pref.getValue(getActivity(), Constants.PREF_PHONE, "")));
                    }else
                    {
                        callIntent.setData(Uri.parse("tel:" + "+" + Pref.getValue(getActivity(), Constants.PREF_PHONE, "")));
                    }
                    startActivity(callIntent);


                }
            });
            txt_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    sendIntent.setData(Uri.parse("sms:" + Pref.getValue(getActivity(), Constants.PREF_PHONE, "")));
                    startActivity(sendIntent);
                }
            });

        } else {
            if(CheckInternet.isInternetConnected(context)) {

                new ExecuteTask().execute();
            }else {
                connectionDetector.showToast(getActivity(), R.string.NO_INTERNET_CONNECTION);
            }

            img_right_header.setVisibility(View.GONE);

        }

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        img_right_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationforLogout();

            }
        });


        return rootView;

    }

    private void preview() {
        txtCancel.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_mobile1.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_email1.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_call.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_msg.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_email_new.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_title_right.setTypeface(FontCustom.setFontOpenSansLight(context));
        txt_contact_name.setTypeface(FontCustom.setTitleFont(context));
        txt_delete.setTypeface(FontCustom.setFontOpenSansLight(context));


    }

    public void init()
    {

        img_left = (ImageView) rootView.findViewById(R.id.img_left);
        txt_title_right = (TextView) rootView.findViewById(R.id.txt_title_right);
        txt_contact_name = (TextView) rootView.findViewById(R.id.txt_contact_name);
        txt_mobile1 = (TextView) rootView.findViewById(R.id.txt_mobile1);
        txt_email1 = (TextView) rootView.findViewById(R.id.txt_email1);
        txtCancel = (TextView) rootView.findViewById(R.id.txtCancel);
        txt_delete = (TextView)rootView.findViewById(R.id.txt_delete);
        txt_email_new = (TextView)rootView.findViewById(R.id.txt_email_new);
        txt_msg = (TextView)rootView.findViewById(R.id.txt_msg);
        txt_call = (TextView)rootView.findViewById(R.id.txt_call);

    }

    @Override
    public void onPause() {
        super.onPause();
        img_right_header.setVisibility(View.GONE);
    }
    private void openDeleteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Do you want to delete this contact ?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(CheckInternet.isInternetConnected(context)) {

                            new ExecuteTask_delete().execute();
                        }else
                        {
                            connectionDetector.showToast(getActivity(), R.string.NO_INTERNET_CONNECTION);
                        }
                        Pref.srtIsDeleteContact(getActivity(), true);

                        txt_delete.setEnabled(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                        txt_delete.setEnabled(true);
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(getResources().getString(R.string.app_name));
        alert.show();
    }

//api for delete contact.

    class ExecuteTask_delete extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(context);
        }

        @Override
        protected String doInBackground(String... params) {


            String res = WebService.PostData1(Pref.getValue(context, Constants.PREF_ID, ""), Constants.Contact_Delete, params, WebService.CONTACT_DELETE, Pref.getValue(context, Constants.TOKEN, ""));
            Log.e("res....", "" + res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                WebService.dismissProgress();
                JSONObject json2;
                json2 = new JSONObject(result);

                FragmentManager manager = getActivity().getSupportFragmentManager();
                ContactDetailsFragment f = new ContactDetailsFragment();
                manager.beginTransaction().remove(f).commit();
                manager.popBackStack();
                // getActivity().onBackPressed();
                Toast.makeText(getActivity(),"Contact deleted successfully!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    class ExecuteTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(context);
        }

        @Override
        protected String doInBackground(String... params) {

            String res = WebService.GetData1(WebService.CONTACT + "/" + Pref.getValue(context, Constants.PREF_ID, ""), Pref.getValue(context, Constants.TOKEN, ""));

            Log.e("res....", "" + res);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject1 = new JSONObject(result);
                if(jsonObject1.optString("status").equalsIgnoreCase("400")) {
                    Toast.makeText(getActivity(),"Data is not found.",Toast.LENGTH_LONG).show();

                }else if(jsonObject1.optJSONObject("data").length()>0)
                {


                    JSONObject jsonObject;
                    jsonObject = jsonObject1.getJSONObject("data");

                    jsonString = jsonObject.toString();


                    String fname = jsonObject.optString("fname");
                    String id = jsonObject.optString("id");
                    String lname = jsonObject.optString("lname");
                    String streetaddress = jsonObject.optString("streetaddress");
                    String city = jsonObject.optString("city");
                    String state = jsonObject.optString("state");
                    String zipcode = jsonObject.optString("zipcode");
                    String note = jsonObject.optString("note");
                    String image = jsonObject.optString("image");
                    final String phone1 = jsonObject.optString("phone1");
                    final String phone2 = jsonObject.optString("phone2");
                    final String phone3 = jsonObject.optString("phone3");
                    final String email1 = jsonObject.optString("email1");
                    String email2 = jsonObject.optString("email2");
                    String email3 = jsonObject.optString("email3");
                    String txt_company_detail = jsonObject.optString("company_name");
                    String company_title = jsonObject.optString("company_title");
                    String uniqueId = jsonObject.optString("uniqueId");
                    String birthday = jsonObject.optString("birthday");
                    String work_anniversary = jsonObject.optString("work_anniversary");
                    String created_at_formatted = jsonObject.optString("created_at_formatted");
                    String updated_at_formatted = jsonObject.optString("updated_at_formatted");
                    Pref.setValue(getActivity(), Constants.PREF_EMAIL, email1);
                    Pref.setValue(getActivity(),Constants.PREF_PHONE,phone1);


                    txt_contact_name.setText(fname + " " + lname);
                    txt_mobile1.setText(phone1);
                    txt_email1.setText(email1);
                    txt_email_new.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("message/rfc822");
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Pref.getValue(getActivity(), Constants.PREF_EMAIL, "")});
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Sent from Android");
                            intent.putExtra(Intent.EXTRA_TEXT, "");

                            startActivity(Intent.createChooser(intent, "Android"));
                        }
                    });
                    txt_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            if(Pref.getValue(getActivity(), Constants.PREF_PHONE, "").startsWith("+")) {
                                callIntent.setData(Uri.parse("tel:" + Pref.getValue(getActivity(), Constants.PREF_PHONE, "")));
                            }else
                            {
                                callIntent.setData(Uri.parse("tel:" + "+" + Pref.getValue(getActivity(), Constants.PREF_PHONE, "")));
                            }
                            startActivity(callIntent);
                        }
                    });
                    txt_msg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            sendIntent.setType("vnd.android-dir/mms-sms");
                            sendIntent.setData(Uri.parse("sms:" + Pref.getValue(getActivity(), Constants.PREF_PHONE, "")));
                            startActivity(sendIntent);
                        }
                    });
                    String date = created_at_formatted.substring(0, 9);

                    SimpleDateFormat df = new SimpleDateFormat("MM/DD/yyyy");
                    int month = Integer.parseInt(created_at_formatted.substring(0, 2));
                    Log.e("RRR", "Month : " + getMonthForInt(month));
                    Log.e("RRR", "Date : " + created_at_formatted.substring(3, 5));
                    Log.e("RRR", "Year : " + created_at_formatted.substring(6, 10));

                }
                WebService.dismissProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        img_right_header.setImageResource(R.mipmap.logout_img);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e("BBB", "ON attach called");
        Pref.srtIsDeleteContact(getActivity(), false);
    }

    private void confirmationforLogout() {

        dialog = new Dialog(getActivity());
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.confirmation_logout_dialog);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        lp.width = width - 50;
        lp.height = height - 50;
        window.setAttributes(lp);

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView txt_ok_logout_dialog = (TextView) dialog.findViewById(R.id.txt_ok_logout_dialog);
        TextView txt_cancel_logout_dialog = (TextView) dialog.findViewById(R.id.txt_cancel_logout_dialog);
        txt_ok_logout_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        txt_cancel_logout_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }
}
