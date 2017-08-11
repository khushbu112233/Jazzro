package com.jlouistechnology.Jazzro.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jlouistechnology.Jazzro.Model.ColorModel;
import com.loopj.android.http.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by innovify on 23/9/15.
 */
public class Utils {


    public static File Copy_sourceLocation;
    public static File Paste_Target_Location;
    public static File MY_IMG_DIR, Default_DIR;
    public static Uri uri;
    public static Intent pictureActionIntent = null;
    public static final int CAMERA_PICTURE = 1;
    public static final int GALLERY_PICTURE = 2;

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static boolean askForPermission(Activity context, String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
            }
        } else {
            //  Toast.makeText(context, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }





    public static ShapeDrawable drawCircle (Context context, int width, int height, int color) {

//////Drawing oval & Circle programmatically /////////////

        ShapeDrawable oval = new ShapeDrawable (new OvalShape());
        oval.setIntrinsicHeight (height);
        oval.setIntrinsicWidth (width);
        oval.getPaint ().setColor (color);
        return oval;
    }


    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }




    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public final static boolean isValidEmail(String hex) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return checkValidation(hex, EMAIL_PATTERN);
    }

    public final static boolean isValidPhone(String hex) {
        String PHONE_PATTERN = "^[+]?[0-9]{10,13}$";
        return checkValidation(hex, PHONE_PATTERN);
    }

    private static boolean checkValidation(String hex, String EMAIL_PATTERN) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public static int getPixel(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static boolean checkInternetConnection(Context context) {

        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isOnline(Context context, boolean isDialogEnabled) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        if (isDialogEnabled) {
            showNewtworkAlert(context);
        }
        return false;
    }

    public static void showNewtworkAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Internet connection is not available.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void showAlert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static Bitmap base64ToImage(String basepath) {
        byte[] decodedString = Base64.decode(basepath, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String Get_Random_File_Name() {
        final Calendar c = Calendar.getInstance();
        int myYear = c.get(Calendar.YEAR);
        int myMonth = c.get(Calendar.MONTH);
        int myDay = c.get(Calendar.DAY_OF_MONTH);
        String Random_Image_Text = "" + myDay + myMonth + myYear + "_" + Math.random();
        return Random_Image_Text;
    }

    // Copy your image into specific folder
    public static File copyFile(File current_location, File destination_location) {
        Copy_sourceLocation = new File("" + current_location);
        Paste_Target_Location = new File("" + destination_location + "/" + Utils.Get_Random_File_Name() + ".jpg");

        Log.v("Purchase-File", "sourceLocation: " + Copy_sourceLocation);
        Log.v("Purchase-File", "targetLocation: " + Paste_Target_Location);
        try {
            // 1 = move the file, 2 = copy the file
            int actionChoice = 2;
            // moving the file to another directory
            if (actionChoice == 1) {
                if (Copy_sourceLocation.renameTo(Paste_Target_Location)) {
                    Log.i("Purchase-File", "Move file successful.");
                } else {
                    Log.i("Purchase-File", "Move file failed.");
                }
            }

            // we will copy the file
            else {
                // make sure the target file exists
                if (Copy_sourceLocation.exists()) {

                    InputStream in = new FileInputStream(Copy_sourceLocation);
                    OutputStream out = new FileOutputStream(Paste_Target_Location);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.i("copyFile", "Copy file successful.");

                } else {
                    Log.i("copyFile", "Copy file failed. Source file missing.");
                }
            }

        } catch (NullPointerException e) {
            Log.i("copyFile", "" + e);

        } catch (Exception e) {
            Log.i("copyFile", "" + e);
        }
        return Paste_Target_Location;
    }

    // 	decode your image into bitmap format
    public static Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            Log.e("decodeFile", "" + e);
        }
        return null;
    }

    // Create New Dir (folder) if not exist
    public static File Create_MY_IMAGES_DIR() {
        try {
            // Get SD Card path & your folder name
            MY_IMG_DIR = new File(Environment.getExternalStorageDirectory(), "/My_Image/");

            // check if exist
            if (!MY_IMG_DIR.exists()) {
                // Create New folder
                MY_IMG_DIR.mkdirs();
                Log.i("path", ">>.." + MY_IMG_DIR);
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Create_MY_IMAGES_DIR", "" + e);
        }
        return MY_IMG_DIR;
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public static ArrayList<ColorModel> colorList() {

        ArrayList<ColorModel> list = new ArrayList<>();


        ColorModel model = new ColorModel();

        model.name = "groupcolor1";
        model.background = "#41cf34";
        model.color = "#41cf34";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor2";
        model.background = "#27b7aa";
        model.color = "#27b7aa";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor3";
        model.background = "#f3410a";
        model.color = "#f3410a";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor4";
        model.background = "#f7d018";
        model.color = "#f7d018";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor5";
        model.background = "#c9df87";
        model.color = "#c9df87";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor6";
        model.background = "#1c6f85";
        model.color = "#1c6f85";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor7";
        model.background = "#f69824";
        model.color = "#f69824";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor8";
        model.background = "#878a74";
        model.color = "#878a74";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor9";
        model.background = "#930943";
        model.color = "#930943";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor10";
        model.background = "#dc81ee";
        model.color = "#dc81ee";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor11";
        model.background = "#85d1f2";
        model.color = "#85d1f2";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor12";
        model.background = "#5a5209";
        model.color = "#5a5209";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor13";
        model.background = "#ee8671";
        model.color = "#ee8671";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor14";
        model.background = "#0619e0";
        model.color = "#0619e0";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor15";
        model.background = "#78573b";
        model.color = "#78573b";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor16";
        model.background = "#e849c8";
        model.color = "#e849c8";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor17";
        model.background = "#d7d9b3";
        model.color = "#d7d9b3";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor18";
        model.background = "#078c36";
        model.color = "#078c36";
        list.add(model);

        model = new ColorModel();
        model.name = "groupcolor19";
        model.background = "#0a031e";
        model.color = "#0a031e";
        list.add(model);


        model = new ColorModel();
        model.name = "groupcolor20";
        model.background = "#f05160";
        model.color = "#f05160";
        list.add(model);

        model = new ColorModel();
        model.name = "unassigned_group";
        model.background = "#f1f1f1";
        model.color = "#f1f1f1";
        list.add(model);

        return list;

    }

}
