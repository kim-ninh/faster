package com.example.bitmapplayround;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ninhhk.faster.Callback;
import com.ninhhk.faster.Faster;

public class MainActivity extends AppCompatActivity {


    private static final String IMAGE_SOURCE_PNG = "https://dummyimage.com/600x400/000/fff.png&text=600+400";
    private static final String IMAGE_SOURCE_JPG = "https://dummyimage.com/300x100/000/fff.jpg&text=JPG+Image";
    private static final String IMAGE_SOURCE_WEBP = "https://www.gstatic.com/webp/gallery/4.webp";
    private static final String IMAGE_SOURCE_HEIC = "https://nokiatech.github.io/heif/content/images/old_bridge_1440x960.heic";
    public static final String HEAVY_IMAGE_PNG = "https://www.pexels.com/photo/417074/download/?search_query=high%2520resolution&tracking_id=k4uuwlyp5jo";
    public static final String HEAVY_IMAGE_2 = "https://www.pexels.com/photo/461862/download/?search_query=high%2520resolution&tracking_id=k4uuwlyp5jo";

    private static final int MAX_BUFFER_IN_MB = 2;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;
    private static final String TAG = MainActivity.class.getName();

    private ImageView imageView;
    private Button btnLoadImage;
    private boolean isLoading = false;
    private Button btnTestResponsive;
    private EditText txtUrl;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);

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

        Glide.with(MainActivity.this)
                .load(IMAGE_SOURCE_WEBP)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        int w = resource.getIntrinsicWidth();
                        int h = resource.getIntrinsicHeight();
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void fetchImage(String imageSource) {
        Faster.init(MainActivity.this)
                .load(imageSource)
                .resize(600)
                .setListener(new Callback<Bitmap>() {
                    @Override
                    public void onReady(Bitmap bitmap) {
                        isLoading = false;
                    }
                })
                .setScaleType(ImageView.ScaleType.CENTER_CROP)
                .into(imageView);

/*
        Glide.with(this)
                .load(imageSource)
                .into(imageView);
*/
        Faster.init(MainActivity.this)
                .load(HEAVY_IMAGE_PNG)
                .resize(300, 300)
                .setScaleType(ImageView.ScaleType.CENTER_CROP)
                .into(imageView2);
    }
}
