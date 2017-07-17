package com.jlouistechnology.Jazzro.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aipxperts-ubuntu-01 on 9/5/17.
 */

public class SyncDetail {

    //private variables

    ArrayList<String> email_sync;
    ArrayList<String> isauto_sync;
    Context context;

    public SyncDetail(ArrayList<String> i, ArrayList<String> string) {
        this.email_sync = i;
        this.isauto_sync = string;
    }
    public SyncDetail(Context context)
    {
        this.context =context;
    }

    public ArrayList<String> getEmail_sync() {
        return email_sync;
    }

    public void setEmail_sync(ArrayList<String> email_sync) {
        this.email_sync = email_sync;
    }

    public ArrayList<String> getIsauto_sync() {
        return isauto_sync;
    }

    public void setIsauto_sync(ArrayList<String> isauto_sync) {
        this.isauto_sync = isauto_sync;
    }
}
