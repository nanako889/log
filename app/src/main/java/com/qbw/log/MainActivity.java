package com.qbw.log;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qbw.l.L;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L log =new L();
        log.setEnabled(true);
        log.d("aaa");
    }
}
