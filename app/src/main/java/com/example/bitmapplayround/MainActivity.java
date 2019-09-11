package com.example.bitmapplayround;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private static final String IMAGE_SOURCE_PNG = "https://dummyimage.com/600x400/000/fff.png&text=PNG+Image";
    private static final String IMAGE_SOURCE_JPG = "https://dummyimage.com/600x400/000/fff.jpg&text=PNG+Image";
    private static final String IMAGE_SOURCE_WEBP = "";
    private static final String IMAGE_SOURCE_HEIC = "";

    private ImageView imageView;
    private Button btnLoadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        btnLoadImage = findViewById(R.id.button);
        btnLoadImage.setOnClickListener((View v) -> fetchImage(IMAGE_SOURCE_WEBP));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        fetchImage(IMAGE_SOURCE_PNG);
    }

    private void fetchImage(String imageSource) {
        final Handler handler = new Handler(
                getMainLooper() /* explicit main looper */,
                message -> {
                    if (message != null && message.obj instanceof Bitmap) {
                        Bitmap bm = (Bitmap) message.obj;
                        imageView.setImageBitmap(bm);
                    }
                    return true;
                });

        Thread t = new Thread(() -> {
            // download image

            // decode image
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.webp_format);

            Message message = Message.obtain();
            message.obj = bm;
            handler.sendMessage(message);
        });

        t.start();
    }
}
