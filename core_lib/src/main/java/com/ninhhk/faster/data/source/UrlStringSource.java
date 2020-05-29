package com.ninhhk.faster.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.utils.StreamUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

public class UrlStringSource extends DataSource<String> {

    private static final String TAG = UrlStringSource.class.getSimpleName();
    private static final int MAX_BUFFER_IN_MB = 4;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;

    public UrlStringSource(String model) {
        super(model);
    }

    @Override
    public String name() {
        String url = model;
        String remoteFileName;
        int lastIndexOfSlash;

        lastIndexOfSlash = url.lastIndexOf('/');
        remoteFileName = url.substring(lastIndexOfSlash + 1);

        return remoteFileName;
    }

    @Nullable
    @Override
    public ByteBuffer load(@NonNull Key key, @NonNull Request request) {
        HttpURLConnection connection = null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
        InputStream is;

        try {
            URL url = new URL(UrlStringSource.this.model);
            connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            byteBuffer = StreamUtils.readToBuffer(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return byteBuffer;
    }
}
