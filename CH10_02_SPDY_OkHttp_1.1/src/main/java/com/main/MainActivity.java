package com.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.okhttp.OkHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mButton = (Button) findViewById(R.id.btGET);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setImageBitmap(null);
                new LoadImage().start();
            }
        });
    }

    private class LoadImage extends Thread {
        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                double sum = (Double) msg.obj;
                setTitle(sum + " %");
            }
        };
        private Bitmap mBitmap;
        private InputStream mInputStream;
        private ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();

        @Override
        public void run() {
            try {
                String imageUrl = getResources().getString(R.string.image_url);
                URL mUrl = new URL(imageUrl);

                //替換成 OkHttp 1.0---------------------------------------------------
                HttpURLConnection mHttpURLConnection = new OkHttpClient().open(mUrl);
                //-------------------------------------------------------------------

                mHttpURLConnection.setRequestMethod("GET");
                mHttpURLConnection.connect();

                mInputStream = mHttpURLConnection.getInputStream();
                double fullSize = mHttpURLConnection.getContentLength();
                byte[] buffer = new byte[64];
                int readSize = 0;
                double sum = 0;
                while ((readSize = mInputStream.read(buffer)) != -1) {
                    mByteArrayOutputStream.write(buffer, 0, readSize);
                    sum += (readSize / fullSize) * 100;
                    Message message = mHandler.obtainMessage(1, sum);
                    mHandler.sendMessage(message);
                }

                byte[] result = mByteArrayOutputStream.toByteArray();
                mBitmap = BitmapFactory.decodeByteArray(result, 0, result.length);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(mBitmap);
                        mImageView.setVisibility(View.VISIBLE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    mInputStream.close();
                    mByteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
