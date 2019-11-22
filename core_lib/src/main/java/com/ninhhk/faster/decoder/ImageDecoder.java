package com.ninhhk.faster.decoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ninhhk.faster.LogUtils;
import com.ninhhk.faster.RequestOption;
import com.ninhhk.faster.StringUtils;
import com.ninhhk.faster.pool.FasterBitmapPool;

import java.io.InputStream;

public abstract class ImageDecoder {
    protected BitmapFactory.Options opts = new BitmapFactory.Options();
    protected RequestOption requestOption;
    private static final String TAG = ImageDecoder.class.getSimpleName();
    public ImageDecoder(){

    }

    abstract protected int[] config(byte[] bytes);

    abstract protected int[] config(InputStream is);

    public Bitmap decode(byte[] bytes, RequestOption requestOption) {
        Bitmap result;
        this.requestOption = requestOption;

        int[] decodedSize = config(bytes);
        opts.inJustDecodeBounds = false;
        Bitmap reuseBitmap;

        // reuseBitmap must have decoded size dimension!!!
        reuseBitmap = FasterBitmapPool.getInstance().get(decodedSize[0], decodedSize[1], Bitmap.Config.ARGB_8888);
        opts.inBitmap = reuseBitmap;
        result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

        LogUtils.i(TAG, StringUtils.concat("Bitmap after down sample size: ", String.valueOf(result.getWidth()), " x ", String.valueOf(result.getHeight())));
        return result;
    }


    public Bitmap decode(InputStream is, RequestOption requestOption) {
        Bitmap result;
        this.requestOption = requestOption;

        int[] decodedSize = config(is);
        opts.inJustDecodeBounds = false;
        Bitmap reuseBitmap;

        // reuseBitmap must have decoded size dimension!!!
        reuseBitmap = FasterBitmapPool.getInstance().get(decodedSize[0], decodedSize[1], Bitmap.Config.ARGB_8888);
        opts.inBitmap = reuseBitmap;
        result = BitmapFactory.decodeStream(is, null, opts);

        LogUtils.i(TAG, StringUtils.concat("Bitmap after down sample size: ", String.valueOf(result.getWidth()), " x ", String.valueOf(result.getHeight())));
        return result;
    }

}
