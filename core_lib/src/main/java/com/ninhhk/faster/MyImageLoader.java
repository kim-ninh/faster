package com.ninhhk.faster;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyImageLoader extends ImageLoader {
    public static final int TOTAL_THREAD_AVAILABLE = 6;
    private static final String TAG = MyImageLoader.class.getSimpleName();
    private static final int FADE_IN_TIME = 300;
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private ThreadPoolExecutor executor;
    private Resources resources;
    private ConcurrentHashMap<ImageView, Future<?>> targetImageViewMap = new ConcurrentHashMap<>();

    public MyImageLoader(Context context) {
        super(context);
        executor = new ThreadPoolExecutor(TOTAL_THREAD_AVAILABLE, TOTAL_THREAD_AVAILABLE, 60L,
                TimeUnit.SECONDS,
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

        ImageView targetView = request.getTargetView().get();
        if (targetView != null) {
            Future<?> oldTask = targetImageViewMap.get(targetView);
            if (oldTask != null) {
                oldTask.cancel(true);
            }
        }

        LoadImageTask loadImageTask = buildTask(request);
        Future<?> currentTask = executor.submit(loadImageTask);

        if (targetView != null) {
            targetImageViewMap.put(targetView, currentTask);
        }
    }

    private LoadImageTask buildTask(Request request) {
        return new LoadImageTask(request);
    }

    private void removeTaskFromMap(ImageView imageView) {
        targetImageViewMap.remove(imageView);
    }

    private class LoadImageTask implements Runnable, Callback<Bitmap> {
        private Request request;
        private WeakReference<ImageView> targetView;
        private Callback<Bitmap> requestListener;
        private ImageView.ScaleType scaleType;
        private final Drawable placeHolderDrawable;


        public LoadImageTask(Request request) {
            this.request = request;
            targetView = request.getTargetView();
            requestListener = request.getListener();
            scaleType = request.getRequestOption().getScaleType();
            placeHolderDrawable = request.getRequestOption().getPlaceHolderDrawable();
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
            BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
            mainThreadHandler.post(() -> {

                ImageView imageView = targetView.get();
                if (imageView != null) {
//                    imageView.setScaleType(scaleType);

                    if (placeHoldIsSet() && !request.isLoadForMem) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                                placeHolderDrawable,
                                bitmapDrawable
                        });
                        imageView.setImageDrawable(td);
                        td.startTransition(FADE_IN_TIME);
                    } else {
                        imageView.setImageDrawable(bitmapDrawable);
                    }
                    removeTaskFromMap(imageView);
                }

                if (requestListener != null) {
                    requestListener.onReady(bitmap);
                }

            });
        }

        private boolean placeHoldIsSet() {
            return placeHolderDrawable != null;
        }
    }
}
