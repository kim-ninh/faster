package com.ninhhk.faster;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseIntArray;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;

import com.ninhhk.faster.data.store.ByteBufferPool;
import com.ninhhk.faster.data.store.DiskStore;
import com.ninhhk.faster.data.store.LruDiskStore;
import com.ninhhk.faster.data.store.LruMemoryStore;
import com.ninhhk.faster.data.store.MemoryStore;
import com.ninhhk.faster.decoder.ImageDecoder;
import com.ninhhk.faster.pool.BitmapPool;
import com.ninhhk.faster.transformer.Transformation;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.MemoryUtils;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageLoader implements Loadable<Bitmap> {

    @NonNull private final MemoryStore memoryStore;
    @Nullable private final DiskStore diskStore;

    public static final int TOTAL_THREAD_AVAILABLE = 4;
    private static final String TAG = ImageLoader.class.getSimpleName();
    private static final int FADE_IN_TIME = 300;
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private ThreadPoolExecutor executor;
    private Resources resources;
    private ConcurrentHashMap<ImageView, Future<?>> targetImageViewMap = new ConcurrentHashMap<>();

    private SparseIntArray orientationTags = new SparseIntArray();

    @NonNull private Config fasterConfig;

    public ImageLoader(@NonNull Context context, @NonNull Config fasterConfig) {

        this.memoryStore = new LruMemoryStore(LruMemoryStore.getSuitableSize(context));

        if (fasterConfig.isUseDiskCache()){
            this.diskStore = new LruDiskStore(context.getCacheDir());
        }else {
            this.diskStore = null;
        }

        this.executor = new ThreadPoolExecutor(TOTAL_THREAD_AVAILABLE, TOTAL_THREAD_AVAILABLE, 60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        this.resources = context.getResources();
        this.fasterConfig = fasterConfig;
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

    public void handleRequest(Request request) {
        postRequest(request);
    }

    private LoadImageTask buildTask(Request request) {
        return new LoadImageTask(request);
    }

    private void removeTaskFromMap(ImageView imageView) {
        targetImageViewMap.remove(imageView);
    }

    @Nullable
    @Override
    public Bitmap load(@NonNull Key key, @NonNull Request request) {
        Loadable<?> loadable = null;
        Loadable<?>[] theLoadable = null;

        int loadablePosition = -1;

        Object loadedData = null;
        ByteBuffer byteBuffer = null;
        Bitmap bitmap = null;

        if (fasterConfig.isUseDiskCache()) {
            theLoadable = new Loadable[]{memoryStore, diskStore, request.getDataSource()};
        } else {
            theLoadable = new Loadable[]{memoryStore, request.getDataSource()};
        }

        for (int i = 0; i < theLoadable.length; i++) {
            loadedData = theLoadable[i].load(key, request);
            if (loadedData != null) {
                loadablePosition = i;
                loadable = theLoadable[i];
                break;
            }
        }

        if (loadedData instanceof ByteBuffer){
            byteBuffer = (ByteBuffer) loadedData;
        }else if (loadedData instanceof Bitmap){
            bitmap = (Bitmap) loadedData;
        }

        // Cache to disk if allow
        if (fasterConfig.isUseDiskCache() && loadable == request.getDataSource()){
            if (diskStore != null && byteBuffer != null){
                diskStore.cache(key, byteBuffer);
            }
        }

        if (loadablePosition > 0 && byteBuffer != null){
            ImageDecoder imageDecoder = request.getImageDecoder();
            RequestOption requestOption = request.getRequestOption();
            bitmap = imageDecoder.decode(byteBuffer, requestOption);

            try {
                ByteBufferPool.getInstance(fasterConfig.getByteBufferCapacity())
                        .put(byteBuffer);

                memoryStore.cache(key, bitmap);

                synchronized (this){
                    orientationTags.put(
                            key.hashCode(),
                            request.orientationTag);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                bitmap = null;
            }
        }else if (loadablePosition == 0){
            request.isLoadFormMem = true;

            synchronized (this){
                request.orientationTag = orientationTags.get(
                        key.hashCode(),
                        ExifInterface.ORIENTATION_UNDEFINED);
            }
        }

        if (bitmap != null) {
            bitmap = applyExifOrientation(bitmap, request.orientationTag, request.getRequestOption());
            bitmap = applyTransformation(bitmap, request.getRequestOption());
        }

        return bitmap;
    }

    private Bitmap applyTransformation(Bitmap bitmap, RequestOption requestOption) {
        Transformation transformation = requestOption.getTransformation();
        return transformation.transform(bitmap, requestOption.getFinalWidth(), requestOption.getFinalHeight());
    }

    private Bitmap applyExifOrientation(Bitmap bitmap, int orientationTag, RequestOption requestOption) {
        Matrix matrix = ExifUtils.getTransformMatrix(orientationTag);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newBitmap;
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
            try {
                Key bitmapKey = new BitmapKeyFactory().build(request);
                Bitmap bitmap = load(bitmapKey, request);
                if (bitmap != null) {
                    onReady(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onReady(@NonNull Bitmap bitmap) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
            mainThreadHandler.post(() -> {

                ImageView imageView = targetView.get();
                if (imageView != null) {

                    if (placeHoldIsSet() && !request.isLoadFormMem) {
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

    public void clearCache() {
        memoryStore.clear();
        if (fasterConfig.isUseDiskCache() && diskStore != null){
            diskStore.clear();
        }
    }
}
