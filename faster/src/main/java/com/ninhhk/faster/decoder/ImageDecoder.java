package com.ninhhk.faster.decoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ninhhk.faster.RequestOption;
import com.ninhhk.faster.pool.FasterBitmapPool;
import com.ninhhk.faster.transformer.Transformation;

public abstract class ImageDecoder {
    protected BitmapFactory.Options opts = new BitmapFactory.Options();
    protected RequestOption requestOption;
    private static final String TAG = ImageDecoder.class.getSimpleName();
    public ImageDecoder(){

    }

    abstract protected void config(byte[] bytes);

    public Bitmap decode(byte[] bytes, RequestOption requestOption) {
        Bitmap result;
        this.requestOption = requestOption;

        config(bytes);
        opts.inJustDecodeBounds = false;
        Bitmap reuseBitmap;
        reuseBitmap = FasterBitmapPool.getInstance().get(requestOption.getFinalWidth(), requestOption.getFinalHeight(), Bitmap.Config.ARGB_8888);
        opts.inBitmap = reuseBitmap;
        result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

        Log.i(TAG, "Bitmap after down sample size: " + result.getWidth() + " x " + result.getHeight());
        result = applyTransformation(result);
        return result;
    }

    private Bitmap applyTransformation(Bitmap source) {
        Transformation transformation = requestOption.getTransformation();
        return transformation.transform(source, requestOption.getFinalWidth(), requestOption.getFinalHeight());
    }
}
