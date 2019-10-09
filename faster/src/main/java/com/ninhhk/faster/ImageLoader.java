package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.Dimension;

public class ImageLoader {

    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private ImageDecoder imageDecoder;
    private Cache<DataSource<?>, Bitmap> memCache;

    public ImageLoader(Cache memCache, ImageDecoder imageDecoder) {
        this.memCache = memCache;
        this.imageDecoder = imageDecoder;
    }

    public void handleRequest(Request request){
        ImageView targetView = request.getTargetView();
        DataSource<?> dataSource = request.getDataSource();
        Bitmap cachedBitmap = memCache.get(dataSource);
        ResponseDelegate<Bitmap> listener = request.getListener();


        if (cachedBitmap != null) {
            targetView.setImageBitmap(cachedBitmap);

            if (listener != null)
                listener.onReady(cachedBitmap);
        }
        else{
            imageDecoder = new MatchTargetDimensionImageDecoder(request.getRequestOption());
            loadDataSource(request);
        }


    }

    private void loadDataSource(Request request) {
        DataSource<?> dataSource = request.getDataSource();
        dataSource.setByteLoadSuccess((bytes)->{
            Bitmap bm = imageDecoder.decode(bytes);
            memCache.put(dataSource, bm);
            mainThreadHandler.post(()->{handleRequest(request);});
        });
        dataSource.load();
    }
}
