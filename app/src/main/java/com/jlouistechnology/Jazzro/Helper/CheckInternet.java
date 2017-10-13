package com.jlouistechnology.Jazzro.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jlouistechnology.Jazzro.R;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by aipxperts-ubuntu-01 on 6/10/17.
 */


public class CheckInternet {

    public static Process ipProcess;
    public static int  is_connected=0;
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {


            ExecutorService service = Executors.newFixedThreadPool(1);
            Future<Integer> is_connect = service.submit(new Check());
            service.shutdown();
            try {
                if(is_connect.get()==1)
                {

                    return true;
                }
                else {

                    return false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isInternetAvailable(String address, int port, int timeoutMs) {
        try {
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress(address, port);

            sock.connect(sockaddr, timeoutMs); // This will block no more than timeoutMs
            sock.close();

            return true;

        } catch (IOException e) { return false; }
    }

    public static class Check implements Callable<Integer> {

        Check() {
        }

        @Override
        public Integer call() {

            if (isInternetAvailable("8.8.8.8", 53, 350)) {
                return 1;
                // Internet available, do something
            } else {
                return 2;
                // Internet not available
            }

       /*     Runtime runtime = Runtime.getRuntime();
            try {
              //  ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                ipProcess = runtime.exec("ping -c 1 www.google.com");
                int     exitValue = ipProcess.waitFor();
                ipProcess.destroy();
                runtime.freeMemory();
                Log.e("is_connected","exit_value:"+exitValue);
                if(exitValue==0)
                {
                    //Global.dismissProgressDialog();
                   return 1;

                }
                else {
                    //Global.dismissProgressDialog();
                   return 2;
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return 0;*/



        }
    }
    public static void make_toast(Context context)
    {
        if(Pref.getValue(context,"show_internet_dialog","").equals("1")) {
            showAlertDialog(context, context.getResources().getString(R.string.txt_network_error));
        }
        // Toast.makeText(context,R.string.NO_INTERNET_CONNECTION,Toast.LENGTH_LONG).show();
    }
    public static void showAlertDialog(final Context context, String message) {
        Pref.setValue(context,"show_internet_dialog","0");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setTitle(context.getResources().getString(R.string.txt_title));
        builder.setPositiveButton(context.getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Pref.setValue(context,"show_internet_dialog","1");
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        if(!alert.isShowing()) {
            alert.show();
        }

    }

}