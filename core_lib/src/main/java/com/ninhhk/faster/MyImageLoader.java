package com.ninhhk.faster;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyImageLoader extends ImageLoader {
    private static final String TAG = MyImageLoader.class.getSimpleName();
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private ThreadPoolExecutor executor;
    private Resources resources;

    public MyImageLoader(Context context) {
        super(context);
        executor = new ThreadPoolExecutor(1, 1, 0L,
                TimeUnit.MICROSECONDS,
                new LinkedBlockingQueue<>());

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
        private WeakReference<ImageView> targetView;
        private Callback<Bitmap> requestListener;
        private ImageView.ScaleType scaleType;

        public LoadImageTask(Request request) {
            this.request = request;
            targetView = request.getTargetView();
            requestListener = request.getListener();
            scaleType = request.getRequestOption().getScaleType();
        }

        @Override
        public void run() {
            try{
                Key bitmapKey = new BitmapKeyFactory().build(request);
                Bitmap bitmap = bitmapStore.load(bitmapKey, request);
                onReady(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onReady(Bitmap bitmap) {
            mainThreadHandler.post(() -> {
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                bitmap.setDensity(displayMetrics.densityDpi);

                ImageView imageView = targetView.get();
                if (imageView != null) {
//                    imageView.setScaleType(scaleType);
                    imageView.setImageBitmap(bitmap);
                }

                if (requestListener != null) {
                    requestListener.onReady(bitmap);
                }

            });
        }
    }
}
