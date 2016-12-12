package com.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    public void onClickBtGET(View view) {
        mImageView.setImageBitmap(null);
        new LoadImage().start();
    }


    private class LoadImage extends Thread {//建立執行緒物件

        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                double sum = (Double) msg.obj;//取得 msg 所帶入的 object 參數
                setTitle("Progress:" + (int) sum + " %");//顯示下載進度
            }
        };

        private Bitmap mBitmap = null;
        private InputStream mInputStream = null;
        private ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();

        @Override
        public void run() {
            try {
                String imageUrl = getResources().getString(R.string.image_url_get);
                URL mUrl = new URL(imageUrl);
                HttpURLConnection mConnection = (HttpURLConnection) mUrl.openConnection();//建立 HttpURLConnection
                mConnection.setRequestMethod("GET");
                mConnection.connect();
                mInputStream = mConnection.getInputStream();
                double fullSize = mConnection.getContentLength();//總長度
                byte[] buffer = new byte[64];// buffer (每次讀取得長度)
                int readSize = 0;//當下讀取的長度

                double sum = 0;
                while ((readSize = mInputStream.read(buffer)) != -1) {
                    mByteArrayOutputStream.write(buffer, 0, readSize);
                    sum += (readSize / fullSize) * 100;//累計讀取進度
                    Message message = mHandler.obtainMessage(1, sum);
                    mHandler.sendMessage(message);//傳遞下載進度
                }

                //將 mByteArrayOutputStream 轉 byte[] 再轉 Bitmap
                byte[] result = mByteArrayOutputStream.toByteArray();
                mBitmap = BitmapFactory.decodeByteArray(result, 0, result.length);

                //資料處理完畢修改 View
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(mBitmap);//將 Bitmap 注入 ImageView
                        mImageView.setVisibility(View.VISIBLE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    mInputStream.close();
                    mByteArrayOutputStream.close();
                } catch (Exception e) {

                }
            }
        }
    }
}
