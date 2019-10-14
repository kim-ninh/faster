package com.ninhhk.faster.decoder;

import android.graphics.BitmapFactory;

import com.ninhhk.faster.RequestOption;

public class MatchTargetDimensionImageDecoder extends ImageDecoder {

    public MatchTargetDimensionImageDecoder(RequestOption requestOption) {
        super(requestOption);
    }

    public MatchTargetDimensionImageDecoder() {
    }

    @Override
    protected void config(byte[] bytes) {
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length , opts);
        int originWidth = opts.outWidth;
        int originHeight = opts.outHeight;

        opts.inSampleSize = calculateSampleSize(originWidth, originHeight);
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
