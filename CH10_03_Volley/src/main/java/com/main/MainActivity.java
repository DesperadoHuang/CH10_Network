package com.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Button mButton;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mButton = (Button) findViewById(R.id.btStart);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue mQueue = Volley.newRequestQueue(mContext);
                ImageRequest mImageRequest = new ImageRequest(
                        getResources().getString(R.string.image_url),
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                mImageView.setVisibility(View.VISIBLE);
                                mImageView.setImageBitmap(bitmap);
                            }
                        },
                        0,
                        0,
                        Bitmap.Config.RGB_565,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                mImageView.setImageResource(R.drawable.android_icon);
                            }
                        }

                );

                mQueue.add(mImageRequest);
            }
        });


    }
}
