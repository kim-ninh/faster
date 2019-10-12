package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.MainThread;

public class ImageLoader implements Callback<Bitmap> {

    private BitmapRepo bitmapRepo = new BitmapRepo();
    private ImageView targetView;
    private Callback<Bitmap> requestListener;
    private RequestManager requestManager = RequestManager.getInstance();

    public ImageLoader(){
        bitmapRepo.setCallback(this);
    }

    public void handleRequest(Request request){
        targetView = request.getTargetView();
        requestListener = request.getListener();

        Key bitmapKey = new BitmapKeyFactory(request).build();
        requestManager.addRequest(bitmapKey, request);
        bitmapRepo.load(bitmapKey);
    }

    @MainThread
    @Override
    public void onReady(Bitmap bitmap) {
        if (targetView != null){
            targetView.setImageBitmap(bitmap);
        }

        if (requestListener != null){
            requestListener.onReady(bitmap);
        }
    }
}
