package com.ninhhk.faster.decoder;

import android.graphics.BitmapFactory;
import android.util.Log;

public class MatchAreaImageDecoder extends ImageDecoder {
    private static final String TAG = MatchAreaImageDecoder.class.getSimpleName();

    @Override
    protected void config(byte[] bytes) {
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        int originWidth = opts.outWidth;
        int originHeight = opts.outHeight;

        int requiredSize = requestOption.getFinalHeight();
        int areaLimit = requiredSize * requiredSize;

        if (originWidth * originHeight <= areaLimit)
            return;

        int targetW = Math.round((float) Math.sqrt(areaLimit * originWidth / (double) originHeight));
        int targetH = Math.round((float) Math.sqrt(areaLimit * originHeight / (double) originWidth));

        int sampleSize = calculateSampleSize(originWidth, originHeight, areaLimit);

        opts.inSampleSize = sampleSize;
        opts.inDensity = Math.max(originWidth, originHeight);
        opts.inTargetDensity = (Math.max(targetW, targetH) - 1) * opts.inSampleSize;

        Log.i(TAG, "Area limit: " + areaLimit);
        Log.i(TAG, "Target (w_h) : " + targetW + " " + targetH);
    }

    private int calculateSampleSize(int originWidth, int originHeight, int areaLimit) {
        int sampleSize = 1;

        while (originWidth * originHeight > areaLimit){
            originWidth /= 2;
            originHeight /= 2;
            sampleSize *= 2;
        }

        return sampleSize;
    }
}
