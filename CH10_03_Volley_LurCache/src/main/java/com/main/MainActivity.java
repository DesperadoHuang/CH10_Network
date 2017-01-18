package com.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private GridView mGridView;
    private PhotoWallAdapter mPhotoWallAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mGridView = (GridView) findViewById(R.id.photo_wall);
        mPhotoWallAdapter = new PhotoWallAdapter(mContext);
        mGridView.setAdapter(mPhotoWallAdapter);
    }
}
