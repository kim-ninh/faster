package com.example.bitmapplayround;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private static final String IMAGE_SOURCE_PNG = "https://dummyimage.com/600x400/000/fff.png&text=PNG+Image";
    private static final String IMAGE_SOURCE_JPG = "https://dummyimage.com/600x400/000/fff.jpg&text=JPG+Image";
    private static final String IMAGE_SOURCE_WEBP = "";
    private static final String IMAGE_SOURCE_HEIC = "";

    private static final int MAX_BUFFER_IN_MB = 2;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;
    private static final String TAG = MainActivity.class.getName();

    private ImageView imageView;
    private Button btnLoadImage;
    private boolean isLoading = false;
    private Button btnTestResponsive;
    private EditText txtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        txtUrl = findViewById(R.id.txtUrl);

        btnLoadImage = findViewById(R.id.button);
        btnLoadImage.setOnClickListener((View v) -> {
            if (!isLoading) {
                String url = txtUrl.getText().toString();
                fetchImage(url);
                isLoading = true;
            }
        });

        btnTestResponsive = findViewById(R.id.button2);
        btnTestResponsive.setOnClickListener((View v) -> {
            Toast.makeText(MainActivity.this, "This button is responsive", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void fetchImage(String imageSource) {
        final Handler handler = new Handler(
                getMainLooper() /* explicit main looper */,
                message -> {
                    if (message != null && message.obj instanceof Bitmap) {
                        Bitmap bm = (Bitmap) message.obj;
                        imageView.setImageBitmap(bm);
                        isLoading = false;
                    }
                    return true;
                });

        Thread t = new Thread(() -> {
            // download image
            try {
                byte[] bytes = getByteData(new URL(imageSource));

                // decode image
//                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.webp_format);

                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Message message = Message.obtain();
                message.obj = bm;
                handler.sendMessage(message);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        t.start();
    }


    public byte[] getByteData(URL url) {
        InputStream inputStream;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(MAX_BUFFER_IN_BYTE);
        HttpURLConnection httpURLConnection = null;


        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

            readFromStream(inputStream, byteArrayOutputStream);


            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return byteArrayOutputStream.toByteArray();
    }

    private void readFromStream(InputStream inputStream, ByteArrayOutputStream array) throws IOException {
        int MAX_BYTE_READ_WRITE = 1024;
        int nByteRead;
        byte[] bytes = new byte[MAX_BYTE_READ_WRITE];
        do {
            nByteRead = inputStream.read(bytes, 0, MAX_BYTE_READ_WRITE);
            if (nByteRead == -1)
                break;

            array.write(bytes, 0, nByteRead);
        } while (true);


        int totalByte = array.size();
        Log.i(TAG, "readFromStream: " + totalByte + " bytes");
    }
}
