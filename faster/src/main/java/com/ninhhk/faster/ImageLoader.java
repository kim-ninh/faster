package com.ninhhk.faster;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.MainThread;

import com.ninhhk.faster.data.store.BitmapStore;

public class ImageLoader implements Callback<Bitmap> {

    private Context context;
    private BitmapStore bitmapStore;
    private ImageView targetView;
    private Callback<Bitmap> requestListener;
    private RequestManager requestManager = RequestManager.getInstance();

    public ImageLoader(){
        bitmapStore.setCallback(this);
    }

    public ImageLoader(Context context) {
//        this();
        this.context = context;
        bitmapStore = new BitmapStore(context);
        bitmapStore.setCallback(this);
    }

    public void handleRequest(Request request){
        targetView = request.getTargetView();
        requestListener = request.getListener();

        Key bitmapKey = new BitmapKeyFactory(request).build();
        requestManager.addRequest(bitmapKey, request);
        bitmapStore.load(bitmapKey);
    }

    @MainThread
    @Override
    public void onReady(Bitmap bitmap) {
        requestManager.clearAllRequests();
        if (targetView != null){
            targetView.setImageBitmap(bitmap);
        }

        if (requestListener != null){
            requestListener.onReady(bitmap);
        }
    }
}
