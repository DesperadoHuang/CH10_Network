package com.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private OkHttpClient mOkHttpClient;
    private String[] imageUrls;
    private LinearLayout mImageLayout;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private class LoadImage extends Thread {
    }
}
