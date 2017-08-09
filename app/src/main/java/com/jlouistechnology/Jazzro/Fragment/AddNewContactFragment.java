package com.jlouistechnology.Jazzro.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jlouistechnology.Jazzro.Helper.ConnectionDetector;
import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.CountryToPhonePrefix;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Jazzro.DashboardNewActivity;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;
import com.jlouistechnology.Jazzro.Widget.TextView_Bold;
import com.jlouistechnology.Jazzro.Widget.TextView_Regular;
import com.jlouistechnology.Jazzro.databinding.AddNewContactLayoutBinding;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aipxperts-ubuntu-01 on 4/8/17.
 */

public class AddNewContactFragment extends Fragment {
    AddNewContactLayoutBinding mBinding;
    Context context;
    View rootView;
    public Dialog mDialogRowBoardList;
    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MY_REQUEST_CODE = 100;
    public static Uri fileUri;
    int REQUEST_CAMERA = 200;
    int SELECT_FILE = 201;
    private static final String IMAGE_DIRECTORY_NAME = "Camera";
    Bitmap rotatedBitmap;
    String encodedString = "";
    ConnectionDetector cd;
    ArrayList<String> selectedGroud = new ArrayList<>();

    ArrayList<String> selectedGroup_label = new ArrayList<>();
    String firstName, lastName, email, phone;
    int check_Valid_or_not = 0;

    private String countryCode = "";
    private String updateID = "";
    ArrayList<String> group_selected_id = new ArrayList<>();
    String group = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.add_new_contact_layout, container, false);
        rootView = mBinding.getRoot();
        context = getActivity();
        /*SharedPreferences preferences = context.getSharedPreferences("selectedGroud", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        SharedPreferences preferences1 = context.getSharedPreferences("selectedGroup_label", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.clear();
        editor1.commit();*/


        preview();

        TelephonyManager tm = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phNo = tm.getLine1Number();
        String country = tm.getSimCountryIso();

        String CON = country.toUpperCase();
        CountryToPhonePrefix.addDatatomap();
        if (!TextUtils.isEmpty(CON)) {
            CountryToPhonePrefix.prefixFor(CON);
            countryCode = CountryToPhonePrefix.prefixFor(CON);
        }
        mBinding.imgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog_camera();
            }
        });
        mBinding.edtGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedGropListFragment fragment = new SelectedGropListFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_container, fragment).addToBackStack(null).commit();

            }
        });
        ((DashboardNewActivity) context).mBinding.header.txtTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
            }
        });
        ((DashboardNewActivity) context).mBinding.header.txtTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check_Valid_or_not = 0;
                if (mBinding.edtFirstName.getText().toString().equals("") || TextUtils.isEmpty(mBinding.edtFirstName.getText().toString().trim())) {
                    check_Valid_or_not = 1;
                    mBinding.edtFirstName.setError(getString(R.string.please_provide_firstname));

                }

                if (mBinding.edtLastName.getText().toString().equals("") || TextUtils.isEmpty(mBinding.edtLastName.getText().toString().trim())) {
                    check_Valid_or_not = 1;
                    mBinding.edtLastName.setError(getString(R.string.please_provide_lastname));

                }
                if (mBinding.edtEmail.getText().toString().length() > 0) {
                    if (!isEmailValid(mBinding.edtEmail.getText().toString())) {
                        check_Valid_or_not = 1;
                        mBinding.edtEmail.setError(getString(R.string.please_provide_valid_email));
                    }
                } else {
                    check_Valid_or_not = 1;
                    mBinding.edtEmail.setError(getString(R.string.please_provide_email));
                }

                if (mBinding.edtPhone.getText().toString().length() > 0) {
                    if ((mBinding.edtPhone.getText().toString().length() < 10) || (mBinding.edtPhone.getText().toString().length() > 15)) {
                        check_Valid_or_not = 1;
                        mBinding.edtPhone.setError(getString(R.string.please_provide_valid_phone_number));

                    }
                } else {
                    check_Valid_or_not = 1;
                    mBinding.edtPhone.setError(getString(R.string.please_provide_phone_number));

                }
                int count = 0;

                if (check_Valid_or_not == 0) {
                    firstName = mBinding.edtFirstName.getText().toString();
                    lastName = mBinding.edtLastName.getText().toString();
                    email = mBinding.edtEmail.getText().toString();
                    phone = mBinding.edtPhone.getText().toString();
                    /**
                     * here when update id is empty mean new contact other wise update contact.
                     */
                    /*if (TextUtils.isEmpty(updateID)) {
                        displaySimpleDialog();

                    } else {

                        ArrayList<String> selectedIDS = new ArrayList<String>();
                        callAddnewContactAPI(selectedIDS);
                    }
*/
                    callAddnewContactAPI(selectedGroud);
                }

            }
        });
        return rootView;
    }

    private void callAddnewContactAPI(ArrayList<String> selectedIDS) {


        group_selected_id = selectedIDS;
        Log.e("group_selected_id", "" + group_selected_id);
        if (!TextUtils.isEmpty(updateID)) {
            Pref.setValue(context, "updateID_add", updateID);
            Log.e("updateID", "" + updateID);

        }
        Pref.setValue(context, "firstName_add", firstName);
        Pref.setValue(context, "lastName_add", lastName);
        Pref.setValue(context, "company_name_add", "");
        if (phone.length() > 0) {

            Pref.setValue(context, "phone1_add", countryCode + phone);
        }
        if (email.length() > 0) {

            Pref.setValue(context, "email1_add", email);
        }

        if (WebService.isNetworkAvailable(getActivity())) {
            new ExecuteTask().execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }
    }

    private void preview() {
        ((DashboardNewActivity) context).visibilityTxtTitleleft(View.VISIBLE);
        ((DashboardNewActivity) context).visibilityTxtTitleright(View.VISIBLE);
        ((DashboardNewActivity) context).SettextTxtTitle("New Contact");
        ((DashboardNewActivity) context).SettextTxtTitleLeft("Cancel");
        ((DashboardNewActivity) context).SettextTxtTitleRight("Save");

    }

    @Override
    public void onResume() {
        super.onResume();
        preview();
        // SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        // SharedPreferences.Editor editor = sharedPrefs.edit();
        Log.e("Adaper###", "$$$$  " + Pref.getValue(context, "selectedGroud", ""));
        Gson gson = new Gson();
        if (!Pref.getValue(context, "selectedGroud", "").equalsIgnoreCase("")) {
            String json = Pref.getValue(context, "selectedGroud", "");
            String json1 = Pref.getValue(context, "selectedGroup_label", "");
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            selectedGroud = gson.fromJson(json, type);
            selectedGroup_label = gson.fromJson(json1, type);

            // Log.e("Adaper###","^^^  "+ selectedGroud.size());

            for (int i = 0; i < selectedGroud.size(); i++) {
                Log.e("Adaper###", "****  " + selectedGroud.get(i) + " " + selectedGroup_label.get(i));
                if (i == (selectedGroud.size() - 1)) {
                    group = group + selectedGroup_label.get(i);
                } else {
                    group = group + selectedGroup_label.get(i) + ",";
                }

            }

            mBinding.edtGroup.setText(group);
           /* facebookFriendListAdapter = new FacebookFriendListAdapter(getActivity(), facebookuserzeebaListModelArrayList);
            mBinding.lvFacebookFrnd.setAdapter(facebookFriendListAdapter);*/
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((DashboardNewActivity) context).Set_header_visibility();
    }

    /**
     * image function
     */

    public void showInputDialog_camera() {

        mDialogRowBoardList = new Dialog(context);
        mDialogRowBoardList.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogRowBoardList.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialogRowBoardList.setContentView(R.layout.photo_dialog_layout);
        mDialogRowBoardList.setCancelable(false);
        mWindow = mDialogRowBoardList.getWindow();
        mLayoutParams = mWindow.getAttributes();
        mLayoutParams.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(mLayoutParams);
        final TextView_Regular txtgallery = (TextView_Regular) mDialogRowBoardList.findViewById(R.id.txtgallery);
        final TextView_Regular txtcamera = (TextView_Regular) mDialogRowBoardList.findViewById(R.id.txtcamera);
        final TextView_Bold cancel = (TextView_Bold) mDialogRowBoardList.findViewById(R.id.cancel);

        txtgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        getActivity().startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE);
                        mDialogRowBoardList.dismiss();
                    }
                } else {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    getActivity().startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                    mDialogRowBoardList.dismiss();
                }
            }
        });

        txtcamera.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                         public void onClick(View v) {


                                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                 if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                                                         != PackageManager.PERMISSION_GRANTED) {
                                                     getActivity().requestPermissions(new String[]{Manifest.permission.CAMERA},
                                                             MY_REQUEST_CODE);
                                                 } else {
                                                     Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                     fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                                     Log.v("fileUri", fileUri + "--");
                                                     intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                                     // start the image capture Intent
                                                     getActivity().startActivityForResult(intent, REQUEST_CAMERA);
                                                     mDialogRowBoardList.dismiss();
                                                 }
                                             } else {
                                                 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                 fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                                 Log.v("fileUri", fileUri + "--");
                                                 intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                                 // start the image capture Intent
                                                 getActivity().startActivityForResult(intent, REQUEST_CAMERA);
                                                 mDialogRowBoardList.dismiss();
                                             }


                                         }
                                     }
        );
        cancel.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {

                                          mDialogRowBoardList.dismiss();
                                      }
                                  }

        );


        mDialogRowBoardList.show();
    }

    public void onActivityGallery(Intent data) {
        Uri selectedImageUri = data.getData();
        if (selectedImageUri.toString().startsWith("content")) {
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = ((FragmentActivity) context).managedQuery(selectedImageUri, projection, null, null,
                    null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(selectedImagePath, options);

            rotatedBitmap = bm;

            if (bm.getWidth() > bm.getHeight()) {
                Matrix matrix = new Matrix();
                //matrix.postRotate(90);
                rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            }
        } else {

            String file_path = data.getData().toString();
            Log.e("rotatedBitmap", "---------------" + file_path.substring(5, file_path.length()));
            File imgFile = new File(file_path.substring(5, file_path.length()));
            Log.e("rotatedBitmap", "---------------" + imgFile + "--" + imgFile.exists());
            if (imgFile.exists()) {
                rotatedBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Log.e("rotatedBitmap", rotatedBitmap + "---");
            }
        }


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Bitmap b = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Bitmap bmpnew = Bitmap.createScaledBitmap(b, 500, 500, false);
        mBinding.imgContact.setImageBitmap(bmpnew);

        encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void onActivity(Intent data) {
        Bitmap thumbnail = null;
        Bitmap rotatedBitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            if (bitmap.getWidth() > bitmap.getHeight()) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } else {
                rotatedBitmap = bitmap;
            }

            // rotatedBitmap=bitmap;
            Log.v("rotatedBitmap", rotatedBitmap + "--");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Bitmap b = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Bitmap bmpnew = Bitmap.createScaledBitmap(b, 500, 500, false);

            encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            mBinding.imgContact.setImageBitmap(bmpnew);

            // file1= bitmapToFile(bmpnew);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    class ExecuteTask extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(context);

            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String res = WebService.PostData2(group_selected_id, Pref.getValue(context, "updateID_add", ""), Pref.getValue(context, "firstName_add", ""), Pref.getValue(context, "lastName_add", ""), Pref.getValue(context, "company_name_add", ""), Pref.getValue(context, "phone1_add", ""), Pref.getValue(context, "email1_add", ""), WebService.SINGLE_CONTACT, Pref.getValue(context, Constants.TOKEN, ""));
            Log.d("nnn", " Response : " + res);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("my_result", result + "---------");

            try {
                WebService.dismissProgress();
                JSONObject json2;
                json2 = new JSONObject(result);

                Pref.setValue(context, "updateID_add", "");
                Pref.setValue(context, "firstName_add", "");
                Pref.setValue(context, "lastName_add", "");
                Pref.setValue(context, "company_name_add", "");
                Pref.setValue(context, "phone1_add", "");
                Pref.setValue(context, "email1_add", "");
                Pref.setValue(context, "groups_add", "");

                if (Pref.getValue(context, "Edit", "").equalsIgnoreCase("1"))

                {
                    Toast.makeText(getActivity(), "Contact updated successfully!", Toast.LENGTH_SHORT).show();

                } else if (Pref.getValue(context, "Edit", "").equalsIgnoreCase("0")) {
                    Toast.makeText(getActivity(), "Contact added successfully!", Toast.LENGTH_SHORT).show();
                }


                getActivity().onBackPressed();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
