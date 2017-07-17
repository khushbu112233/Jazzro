package com.jlouistechnology.Jazzro.Jazzro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jlouistechnology.Jazzro.Helper.Pref;
import com.jlouistechnology.Jazzro.R;

public class MainActivity extends Activity {

    Intent intent;
    TextView txt_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_title = (TextView)findViewById(R.id.txt_title);

        txt_title.setText(Pref.getValue(MainActivity.this, "result", ""));
    }
}


