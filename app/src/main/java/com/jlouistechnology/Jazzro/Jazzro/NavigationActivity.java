package com.jlouistechnology.Jazzro.Jazzro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.jlouistechnology.Jazzro.Helper.Constants;
import com.jlouistechnology.Jazzro.Helper.ParsedResponse;
import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.Helper.PrefManager;
import com.jlouistechnology.Jazzro.Helper.Utils;
import com.jlouistechnology.Jazzro.Model.CardScannerModel;
import com.jlouistechnology.Jazzro.R;
import com.jlouistechnology.Jazzro.Webservice.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NavigationActivity extends FragmentActivity {

    private TextView txtHome, txtContact, txtGroup, txtCardScanner, txtManualEntry, txtMyAccount, txtContactSync;
    android.support.v7.widget.SwitchCompat switch1;
    private int TAKE_PHOTO = 2;
    private String imagePath = "";
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setup();
        updateStatusBarColor();

        Pref.setValue(NavigationActivity.this,"from_group","1");
        Constants constants = new Constants(NavigationActivity.this);
        constants.StatusBar();


    }

    private void setup() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

      /*  switch1 = (SwitchCompat) findViewById(R.id.switch1);

        if (new PrefManager(NavigationActivity.this).isSync() == "1") {
            switch1.setChecked(true);
        } else {
            switch1.setChecked(false);
        }

        switch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    new PrefManager(NavigationActivity.this).setIsSync("1");
                    Log.e("TTT", "is sync true");
                } else {
                    Log.e("TTT", "is sync false");
                    new PrefManager(NavigationActivity.this).setIsSync("0");
                }
            }
        });*/


        txtHome = (TextView) findViewById(R.id.txtHome);
        txtContact = (TextView) findViewById(R.id.txtContact);
        txtGroup = (TextView) findViewById(R.id.txtGroup);
        txtCardScanner = (TextView) findViewById(R.id.txtCardScanner);
        txtManualEntry = (TextView) findViewById(R.id.txtManualEntry);
        txtMyAccount = (TextView) findViewById(R.id.txtMyAccount);
        txtContactSync = (TextView) findViewById(R.id.txtContactSync);

        findViewById(R.id.txt_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });

        txtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.putExtra("drawer_position", "0");
                setResult(RESULT_OK, i);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                Log.e("Dash111","111 ");
            }
        });
        txtContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Dash111","222 ");
                Intent i = new Intent();
                i.putExtra("drawer_position", "1");
                setResult(RESULT_OK, i);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });

        txtGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Dash111","333 ");

                Intent i = new Intent();
                i.putExtra("drawer_position", "2");
                setResult(RESULT_OK, i);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });

     /*   txtCardScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Dash111","444");
                Intent i = new Intent();
                i.putExtra("drawer_position", "3");
                setResult(RESULT_OK, i);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                //NewContactFragment fragment = new NewContactFragment();
                //  getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                Pref.setValue(NavigationActivity.this,"scan_email","");
                Pref.setValue(NavigationActivity.this,"scan_pno","");
                Intent intent=new Intent(NavigationActivity.this,NewCardScannerActivity.class);
                startActivity(intent);

                //   Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //  startActivityForResult(cameraIntent, TAKE_PHOTO);


            }
        });*/

    /*    txtManualEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Dash111","555 ");
                Intent i = new Intent();
                i.putExtra("drawer_position", "4");
                setResult(RESULT_OK, i);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });
*/
    /*    txtMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.putExtra("drawer_position", "5");
                setResult(RESULT_OK, i);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });
*/
        txtContactSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Dash111","666 ");
                if (Utils.askForPermission(NavigationActivity.this, Manifest.permission.READ_CONTACTS, 0)) {
                    Intent i = new Intent();
                    i.putExtra("drawer_position", "3");
                    setResult(RESULT_OK, i);
                    finish();
                    overridePendingTransition(R.anim.stay, R.anim.slide_down);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            photo = Bitmap.createScaledBitmap(photo, 350, 350, false);
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePath = getRealPathFromURI(tempUri);
            try {
                setImage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void setImage() throws FileNotFoundException {

        InputStream inputStream = new FileInputStream(imagePath);//You can get an inputStream using any IO API
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        encodedString = encodedString.replace("\n", "");
        Log.e("RRR", encodedString);


        if (Utils.askForPermission(NavigationActivity.this, Manifest.permission.CAMERA, 0)) {
            Intent i = new Intent();
            i.putExtra("drawer_position", "3");
            i.putExtra("encodeString", encodedString);
            setResult(RESULT_OK, i);
            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_down);
        }

        //   new cardRader1(encodedString).execute();


    }


    public void updateStatusBarColor() {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#3F51B5"));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    public class cardRader1 extends AsyncTask<String, Integer, String> {

        private String key, res;

        public cardRader1(String key) {
            this.key = key;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(NavigationActivity.this);

        }

        @Override
        protected String doInBackground(String... params) {

            res = WebService.cardReaderApi1(key);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            WebService.dismissProgress();
            try {
                JSONObject jsonObject = new JSONObject(res);
                Log.e("Res : ", "" + res);
                String status = jsonObject.getString("status");
                String id = jsonObject.getString("id");
                if (status.equals("202")) {
                    // Toast.makeText(getActivity(), jsonObject.getString("detail"), Toast.LENGTH_SHORT).show();
                    new cardRader3(id).execute();
                } else {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public class cardRader2 extends AsyncTask<String, Integer, String> {

        private String id, res;

        public cardRader2(String id) {
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(NavigationActivity.this);

        }

        @Override
        protected String doInBackground(String... params) {

            res = WebService.cardReaderApi2(id);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            WebService.dismissProgress();
            try {
                JSONObject jsonObject = new JSONObject(res);
                Log.e("Res : ", "" + res);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                ArrayList<CardScannerModel> list = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    CardScannerModel model = new CardScannerModel();

                    model.id = jsonArray.getJSONObject(i).getString("id");
                    model.lastWebhookAttempt = jsonArray.getJSONObject(i).getString("lastWebhookAttempt");
                    model.status = jsonArray.getJSONObject(i).getString("status");
                    model.webhookAttempts = jsonArray.getJSONObject(i).getString("webhookAttempts");
                    model.webhookUrl = jsonArray.getJSONObject(i).getString("webhookUrl");
                    model.quality = jsonArray.getJSONObject(i).getString("quality");
                    model.submitted = jsonArray.getJSONObject(i).getString("submitted");

                    list.add(model);

                }

                String status = jsonObject.getString("status");
                String id = jsonObject.getString("id");
                if (status.equals("202")) {
                    // Toast.makeText(getActivity(), jsonObject.getString("detail"), Toast.LENGTH_SHORT).show();
                } else {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public class cardRader3 extends AsyncTask<String, Integer, String> {

        private String key, res;
        ParsedResponse p;
        boolean error;
        ArrayList<CardScannerModel> arrayList = new ArrayList<>();


        public cardRader3(String key) {
            this.key = key;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebService.showProgress(NavigationActivity.this);

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                p = WebService.cardReader2(key);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            WebService.dismissProgress();

            error = p.error;
            if (!error) {
                arrayList = (ArrayList<CardScannerModel>) p.o;

                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {

                        new cardRader3(key).execute();
                        timer.cancel();
                    }
                }, 30000, 30000);
            }

        }
    }


}
