package com.ninhhk.faster.decoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.ninhhk.faster.LogUtils;
import com.ninhhk.faster.RequestOption;
import com.ninhhk.faster.StringUtils;
import com.ninhhk.faster.pool.FasterBitmapPool;

import java.io.InputStream;
import java.nio.ByteBuffer;

public abstract class ImageDecoder {
    protected BitmapFactory.Options opts = new BitmapFactory.Options();
    protected RequestOption requestOption;
    private static final String TAG = ImageDecoder.class.getSimpleName();
    public ImageDecoder(){

    }

    protected int[] config(byte[] bytes) {
        return config(bytes, 0, bytes.length);
    }

    abstract protected int[] config(byte[] bytes, int offset, int length);

    @NonNull
    public Bitmap decode(@NonNull ByteBuffer byteBuffer,
                         @NonNull RequestOption requestOption){
        Bitmap result;
        byte[] bytes = byteBuffer.array();
        int length = byteBuffer.position();
        this.requestOption = requestOption;

        int[] decodedSize = config(bytes, 0, length);
        int originW = opts.outWidth;
        int originH = opts.outHeight;
        opts.inJustDecodeBounds = false;
        Bitmap reuseBitmap;

        // reuseBitmap must have decoded size dimension!!!
        reuseBitmap = FasterBitmapPool.getInstance().get(decodedSize[0], decodedSize[1], Bitmap.Config.ARGB_8888);
        opts.inBitmap = reuseBitmap;
        result = BitmapFactory.decodeByteArray(bytes, 0, length, opts);

        LogUtils.i(TAG, "Origin size =" + originW + "," + originH);
        LogUtils.i(TAG, "Expected size =" + decodedSize[0] + "," + decodedSize[1]);
        LogUtils.i(TAG, "Actual size =" + result.getWidth() + "," + result.getHeight());
        LogUtils.i(TAG," ");
//        LogUtils.i(TAG, StringUtils.concat("Bitmap after down sample size: ", String.valueOf(result.getWidth()), " x ", String.valueOf(result.getHeight())));
        return result;
    }

}
