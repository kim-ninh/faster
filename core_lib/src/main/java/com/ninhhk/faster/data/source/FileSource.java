package com.ninhhk.faster.data.source;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ninhhk.faster.Request;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FileSource extends DataSource<File> {
    public FileSource(File model) {
        super(model);
    }

    @NonNull
    @Override
    public ByteBuffer loadToBuffer(@NonNull Context context,
                                   @NonNull Request request) {
        try {
            request.orientationTag = ExifUtils.getOrientationTag(model);
            FileInputStream fileInputStream = new FileInputStream(model);

            return StreamUtils.readToBuffer(fileInputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ByteBuffer.allocate(0);
    }

    @Override
    public String name() {
        return model.getAbsolutePath();
    }

}
