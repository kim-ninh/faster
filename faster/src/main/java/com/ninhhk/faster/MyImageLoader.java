package com.ninhhk.faster;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyImageLoader extends ImageLoader {

    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private ExecutorService executor;
    private Resources resources;

    public MyImageLoader(Context context) {
        super(context);
        executor = Executors.newCachedThreadPool();
        resources = context.getResources();
    }

    @Override
    public void handleRequest(Request request) {
        postRequest(request);
    }

    @Override
    public void onReady(Bitmap bitmap) {
        //
    }

    public void postRequest(Request request) {

        LoadImageTask loadImageTask = buildTask(request);
        executor.submit(loadImageTask);
    }

    private LoadImageTask buildTask(Request request) {
        return new LoadImageTask(request);
    }

    private class LoadImageTask implements Runnable, Callback<Bitmap> {
        private Request request;
        private ImageView targetView;
        private Callback<Bitmap> requestListener;

        public LoadImageTask(Request request) {
            this.request = request;
            targetView = request.getTargetView();
            requestListener = request.getListener();
        }

        @Override
        public void run() {
            Key bitmapKey = new BitmapKeyFactory().build(request);
            Bitmap bitmap = bitmapStore.load(bitmapKey, request);
            onReady(bitmap);
        }

        @Override
        public void onReady(Bitmap bitmap) {
            mainThreadHandler.post(() -> {
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                bitmap.setDensity(displayMetrics.densityDpi);
                if (targetView != null) {
                    targetView.setImageBitmap(bitmap);
                }

                if (requestListener != null) {
                    requestListener.onReady(bitmap);
                }

            });
        }
    }
}
