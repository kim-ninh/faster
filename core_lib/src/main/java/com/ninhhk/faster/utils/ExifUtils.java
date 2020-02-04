package com.ninhhk.faster.utils;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ExifUtils {
    public static int getImageRotation(@NonNull InputStream is) {
        int rotation = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(is);
            rotation = getImageRotationInternal(exifInterface);

        } catch (IOException e) {
            e.printStackTrace();
            rotation = -1;
        }
        return rotation;
    }

    public static int getImageRotation(@NonNull File file) {
        int rotation = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(file);
            rotation = getImageRotationInternal(exifInterface);

        } catch (IOException e) {
            e.printStackTrace();
            rotation = -1;
        }
        return rotation;
    }

    public static int getImageRotation(@NonNull String filePath) {
        int rotation = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            rotation = getImageRotationInternal(exifInterface);

        } catch (IOException e) {
            e.printStackTrace();
            rotation = -1;
        }
        return rotation;
    }

    private static int getImageRotationInternal(@NonNull ExifInterface exifInterface) {
        int rotation = 0;
        int orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
        );

        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                rotation = 0;
                break;

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotation = 90;
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotation = 180;
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotation = 270;
                break;
        }

        return rotation;
    }
}
