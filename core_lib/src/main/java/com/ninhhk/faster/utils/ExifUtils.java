package com.ninhhk.faster.utils;

import android.graphics.Matrix;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ExifUtils {

    private static int getOrientationTagInternal(@NonNull ExifInterface exifInterface) {
        int orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
        );
        return orientation;
    }

    public static int getOrientationTag(@NonNull InputStream is) {
        int orientationTag = ExifInterface.ORIENTATION_UNDEFINED;
        try {
            ExifInterface exifInterface = new ExifInterface(is);
            orientationTag = getOrientationTagInternal(exifInterface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orientationTag;
    }

    public static int getOrientationTag(@NonNull File file) {
        int orientationTag = ExifInterface.ORIENTATION_UNDEFINED;
        try {
            ExifInterface exifInterface = new ExifInterface(file);
            orientationTag = getOrientationTagInternal(exifInterface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orientationTag;
    }

    public static int getOrientationTag(@NonNull String filePath) {
        int orientationTag = ExifInterface.ORIENTATION_UNDEFINED;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            orientationTag = getOrientationTagInternal(exifInterface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orientationTag;
    }

    public static Matrix getTransformMatrix(int orientationTag) {
        Matrix matrix = new Matrix();

        switch (orientationTag) {
            case ExifInterface.ORIENTATION_NORMAL:
            case ExifInterface.ORIENTATION_UNDEFINED:
                break;

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.postScale(-1, 1);
                break;

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.postScale(1, -1);
                break;

            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.postScale(-1, 1);
                matrix.postRotate(270);
                break;

            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.postScale(-1, 1);
                matrix.postRotate(90);
                break;
        }

        return matrix;
    }

    public static boolean isNeedSwapRatio(int orientationTag) {
        return orientationTag == ExifInterface.ORIENTATION_ROTATE_90 ||
                orientationTag == ExifInterface.ORIENTATION_ROTATE_270 ||
                orientationTag == ExifInterface.ORIENTATION_TRANSPOSE ||
                orientationTag == ExifInterface.ORIENTATION_TRANSVERSE;
    }
}
