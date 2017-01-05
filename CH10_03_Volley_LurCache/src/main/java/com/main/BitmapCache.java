package com.main;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by ki264 on 2017/1/5.
 */

public class BitmapCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> mBitmapLruCache;

    public BitmapCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mBitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String s) {
        return mBitmapLruCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        synchronized (mBitmapLruCache) {
            if (mBitmapLruCache.get(s) == null) {
                mBitmapLruCache.put(s, bitmap);
            }
        }
    }
}
