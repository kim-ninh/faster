package com.ninhhk.faster;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.MainThread;

import com.ninhhk.faster.data.store.BitmapStore;

import java.lang.ref.WeakReference;

public class ImageLoader implements Callback<Bitmap> {

    private WeakReference<ImageView> targetView;
    private Callback<Bitmap> requestListener;

    protected BitmapStore bitmapStore;

    public ImageLoader(){
    }

    public ImageLoader(Context context) {
        bitmapStore = new BitmapStore(context);
    }

    public void handleRequest(Request request){
        targetView = request.getTargetView();
        requestListener = request.getListener();

        Key bitmapKey = new BitmapKeyFactory().build(request);
        bitmapStore.load(bitmapKey, request);
    }

    @MainThread
    @Override
    public void onReady(Bitmap bitmap) {
        ImageView imageView = targetView.get();
        if (imageView != null){
            imageView.setImageBitmap(bitmap);
        }

        if (requestListener != null){
            requestListener.onReady(bitmap);
        }
    }

    public void clearCache() {
        bitmapStore.clearCache();
    }

    public void setConfig(Config config){
        bitmapStore.setConfig(config);
    }
}
