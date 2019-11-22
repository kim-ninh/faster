package com.ninhhk.faster.decoder;

import android.graphics.BitmapFactory;

import com.ninhhk.faster.LogUtils;
import com.ninhhk.faster.StringUtils;

import java.io.IOException;
import java.io.InputStream;

public class MatchAreaImageDecoder extends ImageDecoder {
    private static final String TAG = MatchAreaImageDecoder.class.getSimpleName();

    @Override
    protected int[] config(byte[] bytes) {
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        int originWidth = opts.outWidth;
        int originHeight = opts.outHeight;

        int requiredSize = requestOption.getFinalHeight();
        int areaLimit = requiredSize * requiredSize;

        int decodedWidth, decodedHeight;
        decodedWidth = originWidth;
        decodedHeight = originHeight;

        if (originWidth * originHeight <= areaLimit) {
            return new int[]{decodedWidth, decodedHeight};
        }

        int targetW = Math.round((float) Math.sqrt(areaLimit * originWidth / (double) originHeight));
        int targetH = Math.round((float) Math.sqrt(areaLimit * originHeight / (double) originWidth));

        int sampleSize = calculateSampleSize(originWidth, originHeight, areaLimit);

        opts.inSampleSize = sampleSize;
        opts.inDensity = Math.max(originWidth, originHeight);
        opts.inTargetDensity = (Math.max(targetW, targetH)) * opts.inSampleSize;


        LogUtils.i(TAG, StringUtils.concat("Area limit: ", String.valueOf(areaLimit)));
        LogUtils.i(TAG, StringUtils.concat("Target (w_h): ", String.valueOf(targetW), " ", String.valueOf(targetH)));

        decodedWidth = targetW;
        decodedHeight = targetH;
        return new int[]{decodedWidth + 1, decodedHeight + 1};
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

        int requiredSize = requestOption.getFinalHeight();
        int areaLimit = requiredSize * requiredSize;

        int decodedWidth, decodedHeight;
        decodedWidth = originWidth;
        decodedHeight = originHeight;

        if (originWidth * originHeight <= areaLimit) {
            return new int[]{decodedWidth, decodedHeight};
        }

        int targetW = Math.round((float) Math.sqrt(areaLimit * originWidth / (double) originHeight));
        int targetH = Math.round((float) Math.sqrt(areaLimit * originHeight / (double) originWidth));

        int sampleSize = calculateSampleSize(originWidth, originHeight, areaLimit);

        opts.inSampleSize = sampleSize;
        opts.inDensity = Math.max(originWidth, originHeight);
        opts.inTargetDensity = (Math.max(targetW, targetH)) * opts.inSampleSize;


        LogUtils.i(TAG, StringUtils.concat("Area limit: ", String.valueOf(areaLimit)));
        LogUtils.i(TAG, StringUtils.concat("Target (w_h): ", String.valueOf(targetW), " ", String.valueOf(targetH)));

        decodedWidth = targetW;
        decodedHeight = targetH;
        return new int[]{decodedWidth + 1, decodedHeight + 1};
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
