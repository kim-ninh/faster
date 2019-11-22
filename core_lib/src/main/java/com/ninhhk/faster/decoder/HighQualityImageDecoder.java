package com.ninhhk.faster.decoder;

import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class HighQualityImageDecoder extends ImageDecoder {

    public HighQualityImageDecoder() {
    }

    @Override
    protected int[] config(byte[] bytes) {
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length , opts);
        int originWidth = opts.outWidth;
        int originHeight = opts.outHeight;

        opts.inSampleSize = calculateSampleSize(originWidth, originHeight);
        return new int[]{originWidth / opts.inSampleSize, originHeight / opts.inSampleSize};
    }

    @Override
    protected int[] config(InputStream is) {
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, opts);
        try {
            is.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int originWidth = opts.outWidth;
        int originHeight = opts.outHeight;

        opts.inSampleSize = calculateSampleSize(originWidth, originHeight);
        return new int[]{originWidth / opts.inSampleSize, originHeight / opts.inSampleSize};
    }

    private int calculateSampleSize(int originWidth, int originHeight){

        float targetHeight = requestOption.getFinalHeight();
        float targetWidth = requestOption.getFinalWidth();
        int inSampleSize = 1;

        if (originHeight > targetHeight || originWidth > targetWidth){
            float halfHeight = originHeight / 2;
            float halfWidth = originWidth / 2;

            while ((halfHeight / inSampleSize) >= targetHeight
                && (halfWidth / inSampleSize) >= targetWidth){
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
