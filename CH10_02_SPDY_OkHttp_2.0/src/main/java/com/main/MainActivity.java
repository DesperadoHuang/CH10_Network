package com.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private OkHttpClient mOkHttpClient;
    private String[] imageUrls;
    private LinearLayout mImageLayout;
    private Context mContext;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        imageUrls = getResources().getStringArray(R.array.image_urls_get);
        mImageLayout = (LinearLayout) findViewById(R.id.imageLayout);
        mButton = (Button) findViewById(R.id.button);
        mOkHttpClient = new OkHttpClient();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageLayout.removeAllViews();
                for (String url : imageUrls) {
                    ImageView imageView = new ImageView(mContext);
                    ProgressBar mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
                    mProgressBar.setMax(100);

                    mImageLayout.addView(imageView);
                    mImageLayout.addView(mProgressBar);

                    new LoadImage(url, imageView, mProgressBar).start();
                }
            }
        });


    }

    private class LoadImage extends Thread {
        private String image_url;
        private ImageView imageView;
        private ProgressBar progressBar;

        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                double sum = (Double) msg.obj;
                progressBar.setProgress((int) sum);
            }
        };

        private Bitmap bitmap = null;
        private InputStream inputStream = null;
        private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        public LoadImage(String image_url, ImageView imageView, ProgressBar progressBar) {
            this.image_url = image_url;
            this.imageView = imageView;
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(image_url);

                Request request = new Request.Builder().url(url).build();
                Response response = mOkHttpClient.newCall(request).execute();
                inputStream = response.body().byteStream();
                double fullSize = response.body().contentLength();

                byte[] buffer = new byte[64];
                int readSize = 0;

                double sum = 0;
                while ((readSize = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, readSize);
                    sum += (readSize / fullSize) * 100;
                    Message message = handler.obtainMessage(1, sum);
                    handler.sendMessage(message);
                }

                byte[] result = byteArrayOutputStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
//                        imageView.setAdjustViewBounds(true);
                        mImageLayout.removeView(progressBar);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    byteArrayOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
