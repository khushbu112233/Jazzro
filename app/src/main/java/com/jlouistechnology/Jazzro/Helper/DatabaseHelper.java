package com.jlouistechnology.Jazzro.Helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper {
    private static DbHelper ourHelper;
    private Context ourContext;
    private static SQLiteDatabase ourDatabase;

    public DatabaseHelper(Context c) {
        ourContext = c;
    }

    public static class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, "mainDB", null, 1);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
                 db.execSQL("create table sync (email text primary key,is_sync text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

            db.execSQL("drop table if exists sync");
            onCreate(db);
        }
    }

    public void DbHelper(Context c) {
        ourContext = c;
    }

    public DatabaseHelper open() {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return DatabaseHelper.this;
    }

    public void close() {
        ourHelper.close();
    }
    //Temperory Insertion By us


    public void insert_sync(String email, String is_sync) {
        ContentValues cv = new ContentValues();

        cv.put("email", email);
        cv.put("is_sync", is_sync);
        long a = ourDatabase.insert("sync", null, cv);
        Log.e("inserted", "1");
        //	Toast.makeText(ourContext, "pt"+a, Toast.LENGTH_SHORT).show();
    }



    public void update_sync(String email, String is_sync) {

        ContentValues cv = new ContentValues();
        cv.put("is_sync", is_sync);
      //  Log.v("updated", " " + msgSeenStatus);
        long a = ourDatabase.update("sync", cv, "email=?", new String[]{email});
        Log.e("updated", "1");
        //	Toast.makeText(ourContext, "Your Profile is Updated", Toast.LENGTH_SHORT).show();

    }
    public Cursor get_sync(String email) {
        // TODO Auto-generated method stub
        Cursor c;
        c = ourDatabase.rawQuery("select * from sync where email='" + email + "'", null);
        return c;
    }



}