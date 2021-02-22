package com.ninhhk.faster.decoder;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;

import com.ninhhk.faster.LogUtils;
import com.ninhhk.faster.utils.MathUtils;

public class MatchAreaImageDecoder extends ImageDecoder {
    private static final String TAG = MatchAreaImageDecoder.class.getSimpleName();

    @SuppressLint("DefaultLocale")
    @Override
    protected int[] config(byte[] bytes, int offset, int length) {
        LogUtils.i(TAG, " ");
        LogUtils.i(TAG, " ");

        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, offset, length, opts);
        int originWidth = opts.outWidth;
        int originHeight = opts.outHeight;

        if (originWidth == -1 || originHeight == -1){
            LogUtils.e(TAG, "Cannot decode bitmap");
        }

        int requiredSize = requestOption.getFinalHeight();
        int areaLimit = requiredSize * requiredSize;

        int decodedWidth, decodedHeight;
        decodedWidth = originWidth;
        decodedHeight = originHeight;

        if (originWidth * originHeight <= areaLimit) {
            return new int[]{decodedWidth, decodedHeight};
        }

        int sampleSize = calculateSampleSize(originWidth, originHeight, areaLimit);
        float targetW = (float) originWidth / sampleSize;
        float targetH = (float) originHeight /sampleSize;

        int[] ratio = new int[]{1, 1};
        int gcd = MathUtils.gcd(originWidth, originHeight);
        ratio[0] = originWidth / gcd;
        ratio[1] = originHeight / gcd;

        while (targetH - ratio[0] > 0 &&
                targetW - ratio[1] > 0 &&
                targetW * targetH > areaLimit){
            targetW -= ratio[0];
            targetH -= ratio[1];
        }

        opts.inSampleSize = sampleSize;
        opts.inDensity = Math.max(originWidth, originHeight);
        opts.inTargetDensity = (int) Math.ceil(Math.max(targetW, targetH) * opts.inSampleSize);


        LogUtils.i(TAG, String.format("Area limit: %d", areaLimit));
        LogUtils.i(TAG, String.format("Target (w, h) = %f, %f", targetW, targetH));

        decodedWidth = (int) Math.ceil(targetW);
        decodedHeight = (int) Math.ceil(targetH);
        return new int[]{decodedWidth, decodedHeight};
    }


    /**
     * Find the largest sampleSize that (orgW / sampleSize) * (orgH / sampleSize) > areaLimit
     */
    private int calculateSampleSize(int originWidth, int originHeight, int areaLimit) {
        int sampleSize = 1;

        while ((originWidth / (float) sampleSize) * (originHeight / (float) sampleSize) > areaLimit) {
            sampleSize *= 2;
        }

        if (sampleSize > 1) {
            sampleSize /= 2;
        }

        return sampleSize;
    }
}
