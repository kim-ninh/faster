package com.ninhhk.faster.decoder;

import android.graphics.BitmapFactory;

public class MatchAreaImageDecoder extends ImageDecoder {
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

        int targetW = (int) Math.sqrt( areaLimit * originWidth / (float) originHeight);
        int targetH = (int) Math.sqrt( areaLimit * originHeight / (float) originWidth);

        int sampleSize = calculateSampleSize(originWidth, originHeight, areaLimit);

        opts.inSampleSize = sampleSize;
        opts.inDensity = Math.max(originWidth, originHeight);
        opts.inTargetDensity = Math.max(targetW, targetH) * opts.inSampleSize;
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
