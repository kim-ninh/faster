package com.ninhhk.faster.decoder;

import android.graphics.BitmapFactory;

public class HighQualityImageDecoder extends ImageDecoder {

    public HighQualityImageDecoder() {
    }

    @Override
    protected int[] config(byte[] bytes, int offset, int length) {
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, offset, length, opts);
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
            float halfHeight = originHeight / 2f;
            float halfWidth = originWidth / 2f;

            while ((halfHeight / inSampleSize) >= targetHeight
                && (halfWidth / inSampleSize) >= targetWidth){
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
