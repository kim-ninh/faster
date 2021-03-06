package com.ninhhk.faster.decoder;

import android.graphics.BitmapFactory;

import com.ninhhk.faster.LogUtils;

public class MaxOneSideImageDecoder extends ImageDecoder {
    private static final String TAG = MaxOneSideImageDecoder.class.getSimpleName();

    @Override
    protected int[] config(byte[] bytes, int offset, int length) {
        LogUtils.i(TAG, " ");
        LogUtils.i(TAG, " ");

        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, offset, length , opts);
        int originWidth = opts.outWidth;
        int originHeight = opts.outHeight;

        int targetWidth = requestOption.getFinalWidth();
        int targetHeight = requestOption.getFinalHeight();

        int decodedWidth, decodedHeight;

        decodedWidth = targetWidth;
        decodedHeight = targetHeight;

        if (originHeight < targetHeight && originWidth < targetWidth) {
            return new int[]{decodedWidth, decodedHeight};
        }

        if (originWidth > originHeight) {
            opts.inSampleSize = calculateSampleSize(originWidth, targetWidth);
            opts.inDensity = originWidth;
            opts.inTargetDensity = targetWidth * opts.inSampleSize;

            int scaledSize = Math.round((float) originHeight / opts.inSampleSize * opts.inTargetDensity / opts.inDensity);
            decodedHeight = scaledSize + 1;

        } else {
            opts.inSampleSize = calculateSampleSize(originHeight, targetHeight);
            opts.inDensity = originHeight;
            opts.inTargetDensity = targetHeight * opts.inSampleSize;

            int scaledSize = Math.round((float) originWidth / opts.inSampleSize * opts.inTargetDensity / opts.inDensity);
            decodedWidth = scaledSize + 1;
        }

        return new int[]{decodedWidth, decodedHeight};
    }

    private int calculateSampleSize(int originSize, int requiredSize) {
        int sampleSize = 1;

        while (originSize / sampleSize > requiredSize){
            sampleSize *= 2;
        }

        if (sampleSize > 1){
            sampleSize /= 2;
        }

        return sampleSize;
    }
}
