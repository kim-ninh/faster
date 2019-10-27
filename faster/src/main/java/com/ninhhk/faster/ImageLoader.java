package com.ninhhk.faster;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.MainThread;

import com.ninhhk.faster.data.store.BitmapStore;

public class ImageLoader implements Callback<Bitmap> {

    private Context context;
    private ImageView targetView;
    private Callback<Bitmap> requestListener;

    protected BitmapStore bitmapStore;

    public ImageLoader(){
    }

    public ImageLoader(Context context) {
        this.context = context;
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
        bitmap.setDensity(context.getResources().getDisplayMetrics().densityDpi);
        if (targetView != null){
            targetView.setImageBitmap(bitmap);
        }

        if (requestListener != null){
            requestListener.onReady(bitmap);
        }
    }
}
