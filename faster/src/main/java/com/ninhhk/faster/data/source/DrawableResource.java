package com.ninhhk.faster.data.source;

import android.content.Context;

import androidx.annotation.DrawableRes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DrawableResource extends DataSource<Integer> {

    private static final int MAX_BUFFER_IN_MB = 4;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;

    public DrawableResource(@DrawableRes int resId) {
        super(resId);
    }

    @Override
    public void load(Context context) {
        Thread t = new Thread(() -> {
            InputStream is;
            try {
                is = context.getResources().openRawResource(model);
                readFromStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        t.start();
    }

    @Override
    public String name() {
        return "resId_" + model;
    }

    private void readFromStream(InputStream is) throws IOException {
        int MAX_BYTE_READ_WRITE = 1024;
        int nByteRead;
        ByteArrayOutputStream array = new ByteArrayOutputStream(MAX_BUFFER_IN_BYTE);

        byte[] bytes = new byte[MAX_BYTE_READ_WRITE];
        do {
            nByteRead = is.read(bytes, 0, MAX_BYTE_READ_WRITE);
            if (nByteRead == -1)
                break;

            array.write(bytes, 0, nByteRead);
        } while (true);

        byteLoad.onReady(array.toByteArray());
    }
}
