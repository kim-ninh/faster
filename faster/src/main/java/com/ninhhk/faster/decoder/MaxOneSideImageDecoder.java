package com.ninhhk.faster.decoder;

import android.graphics.BitmapFactory;

public class MaxOneSideImageDecoder extends ImageDecoder {
    @Override
    protected void config(byte[] bytes) {
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length , opts);
        int originWidth = opts.outWidth;
        int originHeight = opts.outHeight;

        int targetWidth = requestOption.getFinalWidth();
        int targetHeight = requestOption.getFinalHeight();

        if (originHeight < targetHeight && originWidth < targetWidth)
            return;

        if (originWidth > originHeight){
            opts.inSampleSize = calculateSampleSize(originWidth, targetWidth);
            opts.inDensity = originWidth;
            opts.inTargetDensity = targetWidth * opts.inSampleSize;

            int scaledSize = Math.round((float) originHeight / opts.inSampleSize * opts.inTargetDensity / opts.inDensity)  ;
            requestOption.setFinalHeight(scaledSize + 1);

        }else {
            opts.inSampleSize = calculateSampleSize(originHeight, targetHeight);
            opts.inDensity = originHeight;
            opts.inTargetDensity = targetHeight * opts.inSampleSize;

            int scaledSize = Math.round((float) originHeight / opts.inSampleSize * opts.inTargetDensity / opts.inDensity)  ;
            requestOption.setFinalWidth(scaledSize + 1);
        }
    }

    private int calculateSampleSize(int originSize, int requiredSize) {
        int sampleSize = 1;

        while (originSize / sampleSize > requiredSize){
            sampleSize *= 2;
        }

        return sampleSize;
    }
}
