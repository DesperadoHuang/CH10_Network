package com.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private Context mContext;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    public void btPOSTOnClick(View view) {
        new Upload().start();
    }

    private class Upload extends Thread {
        @Override
        public void run() {
            String imageStr = imageToString(mImageView);
            Log.i("mytest", "1");
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("imageStr", imageStr));
            try {
                String user_agent = System.getProperty("http_agent");
                HttpClient mHttpClient = AndroidHttpClient.newInstance(user_agent);
                HttpPost mHttpPost = new HttpPost(getResources().getString(R.string.image_url_post));
                Log.i("mytest", "2");
                mHttpPost.setEntity(new UrlEncodedFormEntity(pairs));
                Log.i("mytest", "3");
                HttpResponse mHttpResponse = mHttpClient.execute(mHttpPost);
                Log.i("mytest", "4");
                final String status = mHttpResponse.getStatusLine().toString();
                Log.i("mytest", status);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "上傳成功!" + status, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.i("mytest", "Execption" + e.toString());
                e.printStackTrace();
            }
        }
    }

    //將圖形格式轉編碼成字串格式
    private String imageToString(ImageView imageView) {
        BitmapDrawable mBitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
        Bitmap mBitmap = mBitmapDrawable.getBitmap();
        ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, mByteArrayOutputStream);
        byte[] byte_arr = mByteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byte_arr, Base64.DEFAULT);
    }
}
