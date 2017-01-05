package com.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * Created by ki264 on 2017/1/5.
 */

public class PhotoWallAdapter extends BaseAdapter {
    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public PhotoWallAdapter(Context mContext) {
        this.mContext = mContext;
        mRequestQueue = Volley.newRequestQueue(mContext);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
    }

    @Override
    public int getCount() {
        return Images.imageUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return Images.imageUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String url = (String) getItem(position);
        final ViewHolder gridViewImageHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.photo_layout, null);
            gridViewImageHolder = new ViewHolder();
            gridViewImageHolder.photo = (NetworkImageView) view.findViewById(R.id.photo);
            view.setTag(gridViewImageHolder);
        } else {
            gridViewImageHolder = (ViewHolder) view.getTag();
        }
        gridViewImageHolder.photo.setImageUrl(url, mImageLoader);
        return view;
    }

    static class ViewHolder {
        NetworkImageView photo;
    }
}
